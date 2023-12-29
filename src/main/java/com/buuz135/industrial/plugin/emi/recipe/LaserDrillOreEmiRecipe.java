package com.buuz135.industrial.plugin.emi.recipe;

import com.buuz135.industrial.plugin.emi.EMICustomPlugin;
import com.buuz135.industrial.recipe.LaserDrillOreRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LaserDrillOreEmiRecipe implements EmiRecipe {
    private final ResourceLocation id;
    private final EmiIngredient catalyst;
    private final EmiStack output;

    public LaserDrillOreEmiRecipe(LaserDrillOreRecipe recipe) {
        this.id = recipe.getId();
        this.catalyst = EmiIngredient.of(recipe.catalyst);
        this.output = EmiStack.EMPTY; //EmiIngredient.of(recipe.output);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EMICustomPlugin.LASER_DRILL_ORE_CATEGORY;
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
        return 0;
    }

    @Override
    public int getDisplayHeight() {
        return 0;
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {

    }
}
