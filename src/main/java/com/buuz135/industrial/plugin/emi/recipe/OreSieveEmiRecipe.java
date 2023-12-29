package com.buuz135.industrial.plugin.emi.recipe;

import com.buuz135.industrial.api.recipe.ore.OreFluidEntryFermenter;
import com.buuz135.industrial.api.recipe.ore.OreFluidEntrySieve;
import com.buuz135.industrial.plugin.emi.EMICustomPlugin;
import dev.emi.emi.api.forge.ForgeEmiStack;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OreSieveEmiRecipe implements EmiRecipe {
    private final EmiIngredient input;
    private final EmiIngredient sieveItem;
    private final EmiStack output;

    public OreSieveEmiRecipe(OreFluidEntrySieve sieve) {
        this.input = ForgeEmiStack.of(sieve.getInput());
        this.sieveItem = EmiIngredient.of(sieve.getSieveItem());
        this.output = EmiStack.of(sieve.getOutput());
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EMICustomPlugin.FLUID_SIEVE_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return null;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(input, sieveItem);
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
