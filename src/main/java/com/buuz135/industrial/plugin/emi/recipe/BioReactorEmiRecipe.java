package com.buuz135.industrial.plugin.emi.recipe;

import com.buuz135.industrial.plugin.emi.EMICustomPlugin;
import com.buuz135.industrial.utils.Reference;
import dev.emi.emi.api.forge.ForgeEmiStack;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TankWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BioReactorEmiRecipe implements EmiRecipe {
    private static final EmiTexture BACKGROUND =
            new EmiTexture(new ResourceLocation(Reference.MOD_ID, "textures/gui/jei.png"), 0, 27, 70, 50);
    private final EmiIngredient input;
    private final EmiStack output;

    public BioReactorEmiRecipe(TagKey<Item> itemTag, FluidStack fluid) {
        this.input = EmiIngredient.of(itemTag);
        this.output = ForgeEmiStack.of(fluid);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EMICustomPlugin.BIO_REACTOR_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return null;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(input);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public int getDisplayWidth() {
        return 70;
    }

    @Override
    public int getDisplayHeight() {
        return 50;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BACKGROUND, 0, 0);
        widgets.addSlot(input, 0, 16)
                .drawBack(false);
        widgets.add(new TankWidget(output, 56, 0, 14, 50, 1000))
                .drawBack(false)
                .recipeContext(this);
    }
}
