package com.buuz135.industrial.plugin.emi.recipe;

import com.buuz135.industrial.plugin.emi.EMICustomPlugin;
import com.buuz135.industrial.recipe.LaserDrillFluidRecipe;
import com.buuz135.industrial.recipe.LaserDrillRarity;
import com.buuz135.industrial.utils.Reference;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.client.screen.asset.DefaultAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LaserDrillFluidEmiRecipe implements EmiRecipe {
    private static final EmiTexture BACKGROUND = new EmiTexture(new ResourceLocation(Reference.MOD_ID, "textures/gui/jei.png"), 0, 0, 52, 26);
    private final ResourceLocation id;
    private final EmiIngredient catalyst;
    private final EmiStack output;
    private final int pointer;
    private final LaserDrillRarity[] rarity;
    private final ResourceLocation entity;

    public LaserDrillFluidEmiRecipe(LaserDrillFluidRecipe recipe) {
        this.id = recipe.getId();
        this.catalyst = EmiIngredient.of(recipe.catalyst);
        this.output = ForgeEmiStack.of(FluidStack.loadFluidStackFromNBT(recipe.output));
        this.pointer = recipe.pointer;
        this.rarity = recipe.rarity;
        this.entity = recipe.entity;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EMICustomPlugin.LASER_DRILL_FLUID_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of();
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return List.of(catalyst);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public int getDisplayWidth() {
        return 52 + 35 + 65;
    }

    @Override
    public int getDisplayHeight() {
        return 26 + 0 + 60;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addDrawable(0, 0, 52 + 35 + 65, 26 + 0 + 60, (guiGraphics, mouseX, mouseY, delta) -> {
            int recipeWidth = 82 + 35 + 35;
            if (pointer > 0)
                AssetUtil.drawAsset(guiGraphics, Minecraft.getInstance().screen, DefaultAssetProvider.DEFAULT_PROVIDER.getAsset(AssetTypes.BUTTON_ARROW_LEFT), 0, 70);
            if (pointer < rarity.length - 1)
                AssetUtil.drawAsset(guiGraphics, Minecraft.getInstance().screen, DefaultAssetProvider.DEFAULT_PROVIDER.getAsset(AssetTypes.BUTTON_ARROW_RIGHT), 137, 70);
            AssetUtil.drawAsset(guiGraphics, Minecraft.getInstance().screen, DefaultAssetProvider.DEFAULT_PROVIDER.getAsset(AssetTypes.TANK_SMALL), 60 + 35 + 3, 3);
            var toasts = new ResourceLocation("textures/gui/toasts.png");
            guiGraphics.blit(toasts, recipeWidth / 10 * 2, 30 + (Minecraft.getInstance().font.lineHeight + 2) * 3, 216, 0, 20, 20, 256, 256);
            guiGraphics.blit(toasts, recipeWidth / 10 * 7, 30 + (Minecraft.getInstance().font.lineHeight + 2) * 3, 216, 0, 20, 20, 256, 256);
            var icons =  new ResourceLocation("forge", "textures/gui/icons.png");
            guiGraphics.blit(icons, recipeWidth / 10 * 7 + 1, 30 + (Minecraft.getInstance().font.lineHeight + 2) * 3 + 3, 0, 16, 16, 16);

            String minY = Component.translatable("text.industrialforegoing.miny").getString() + " " + rarity[pointer].depth_min;
            String maxY = Component.translatable("text.industrialforegoing.maxy").getString() + " " + rarity[pointer].depth_max;
            String biomes = Component.translatable("text.industrialforegoing.biomes").getString();
            guiGraphics.drawString(Minecraft.getInstance().font, ChatFormatting.DARK_GRAY + minY, recipeWidth / 10, 30, 0, false);
            if (!LaserDrillFluidRecipe.EMPTY.equals(entity)) {
                String wight = "Over: " + Component.translatable("entity." + entity.toString().replace(":", ".")).getString();
                guiGraphics.drawString(Minecraft.getInstance().font, ChatFormatting.DARK_GRAY + wight, recipeWidth / 10, 30 + (Minecraft.getInstance().font.lineHeight + 2), 0, false);
            }
            guiGraphics.drawString(Minecraft.getInstance().font, ChatFormatting.DARK_GRAY + maxY, recipeWidth / 10 * 6, 30, 0, false);
            guiGraphics.drawString(Minecraft.getInstance().font, ChatFormatting.DARK_GRAY + "" + ChatFormatting.UNDERLINE + biomes, recipeWidth / 2 - Minecraft.getInstance().font.width(biomes) / 2, 30 + (Minecraft.getInstance().font.lineHeight + 2) * 2, 0, false);
        });

        widgets.addTexture(BACKGROUND, 35, 0);
        widgets.addSlot(catalyst, 35, 4)
                .drawBack(false)
                .catalyst(true);
        widgets.add(new TankWidget(output, 60 + 35 + 5, 5, 14, 15, 200))
                .drawBack(false)
                .recipeContext(this);
    }
}
