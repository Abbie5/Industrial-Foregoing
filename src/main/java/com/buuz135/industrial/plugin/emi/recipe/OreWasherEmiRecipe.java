package com.buuz135.industrial.plugin.emi.recipe;

import com.buuz135.industrial.api.recipe.ore.OreFluidEntryRaw;
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

public class OreWasherEmiRecipe implements EmiRecipe {
    private final EmiIngredient ore;
    private final EmiIngredient input;
    private final EmiStack output;

    public OreWasherEmiRecipe(OreFluidEntryRaw raw) {
        this.ore = EmiIngredient.of(raw.getOre());
        this.input = ForgeEmiStack.of(raw.getInput());
        this.output = ForgeEmiStack.of(raw.getOutput());
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EMICustomPlugin.ORE_WASHER_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return null;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(ore, input);
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
