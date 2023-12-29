package com.buuz135.industrial.plugin.emi.recipe;

import com.buuz135.industrial.plugin.emi.EMICustomPlugin;
import com.buuz135.industrial.recipe.FluidExtractorRecipe;
import com.buuz135.industrial.utils.Reference;
import dev.emi.emi.api.forge.ForgeEmiStack;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TankWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FluidExtractorEmiRecipe implements EmiRecipe {
    private final EmiTexture BACKGROUND =
            new EmiTexture(new ResourceLocation(Reference.MOD_ID, "textures/gui/jei.png"), 0, 27, 76, 50);
    private final ResourceLocation id;
    private final EmiIngredient input;
    private final EmiStack blockOutput;
    private final EmiStack fluidOutput;

    public FluidExtractorEmiRecipe(FluidExtractorRecipe recipe) {
        this.id = recipe.getId();
        this.input = EmiIngredient.of(recipe.input.getItems().stream().map(EmiStack::of).toList()).setChance(recipe.breakChance);
        this.blockOutput = EmiStack.of(recipe.result);
        this.fluidOutput = ForgeEmiStack.of(recipe.output);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EMICustomPlugin.FLUID_EXTRACTOR_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(input);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(blockOutput, fluidOutput);
    }

    @Override
    public int getDisplayWidth() {
        return 76 + 74;
    }

    @Override
    public int getDisplayHeight() {
        return 50;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BACKGROUND, 0, 0);
        widgets.addDrawable(0, 0, 76 + 74, 50, (guiGraphics, mouseX, mouseY, delta) -> {
            guiGraphics.drawString(Minecraft.getInstance().font, ChatFormatting.DARK_GRAY + "Production: ", 80, 6, 0xFFFFFF, false);
            guiGraphics.drawString(Minecraft.getInstance().font, ChatFormatting.DARK_GRAY + "" + fluidOutput.getAmount() + "mb/work", 80, 6 + (Minecraft.getInstance().font.lineHeight + 2) * 1, 0xFFFFFF, false);
            guiGraphics.drawString(Minecraft.getInstance().font, ChatFormatting.DARK_AQUA + "" + "Tripled when", 80, 6 + (Minecraft.getInstance().font.lineHeight + 2) * 2, 0xFFFFFF, false);
            guiGraphics.drawString(Minecraft.getInstance().font, ChatFormatting.DARK_AQUA + "" + "powered", 80, 6 + (Minecraft.getInstance().font.lineHeight + 2) * 3, 0xFFFFFF, false);
        });
        widgets.addSlot(input, 0, 16)
                .drawBack(false);
        widgets.addSlot(blockOutput, 26, 33)
                .recipeContext(this);
        widgets.add(new TankWidget(fluidOutput, 56, 0, 14, 50, 20))
                .drawBack(false)
                .recipeContext(this);
    }
}
