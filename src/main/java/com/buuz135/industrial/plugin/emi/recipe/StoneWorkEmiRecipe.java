package com.buuz135.industrial.plugin.emi.recipe;

import com.buuz135.industrial.block.resourceproduction.tile.MaterialStoneWorkFactoryTile;
import com.buuz135.industrial.plugin.emi.EMICustomPlugin;
import com.buuz135.industrial.utils.Reference;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StoneWorkEmiRecipe implements EmiRecipe {
    private final EmiTexture BACKGROUND = new EmiTexture(new ResourceLocation(Reference.MOD_ID, "textures/gui/jei.png"), 94, 0, 160, 26);
    private final EmiIngredient input;
    private final EmiStack output;
    private final List<MaterialStoneWorkFactoryTile.StoneWorkAction> modes;

    public StoneWorkEmiRecipe(ItemStack input, List<MaterialStoneWorkFactoryTile.StoneWorkAction> modes, ItemStack output) {
        this.input = EmiStack.of(input);
        this.output = EmiStack.of(output);
        this.modes = modes;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EMICustomPlugin.STONE_WORK_CATEGORY;
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
        return 160;
    }

    @Override
    public int getDisplayHeight() {
        return 26;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BACKGROUND, 0, 0);

        widgets.addSlot(input, 0, 4)
                .drawBack(false);
        widgets.addSlot(output, 138, 4)
                .drawBack(false)
                .recipeContext(this);

        for (int i = 0; i < modes.size(); i++) {
            widgets.addSlot(EmiStack.of(modes.get(i).getIcon()), 28 + i * 24, 4)
                    .drawBack(false)
                    .catalyst(true);
        }
    }

    public List<MaterialStoneWorkFactoryTile.StoneWorkAction> getModes() {
        return modes;
    }
}
