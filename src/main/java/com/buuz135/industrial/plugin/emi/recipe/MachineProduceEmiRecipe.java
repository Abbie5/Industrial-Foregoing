package com.buuz135.industrial.plugin.emi.recipe;

import com.buuz135.industrial.plugin.emi.EMICustomPlugin;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MachineProduceEmiRecipe implements EmiRecipe {
    public MachineProduceEmiRecipe(Block block, ItemStack... output) {
    }

    public MachineProduceEmiRecipe(Block block, TagKey<Item> output) {
    }

    public MachineProduceEmiRecipe(Block block, FluidStack output) {
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EMICustomPlugin.MACHINE_PRODUCE_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return null;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return null;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return null;
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
