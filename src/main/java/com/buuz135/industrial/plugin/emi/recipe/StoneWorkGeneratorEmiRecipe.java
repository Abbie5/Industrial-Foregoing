package com.buuz135.industrial.plugin.emi.recipe;

import com.buuz135.industrial.plugin.emi.EMICustomPlugin;
import com.buuz135.industrial.recipe.StoneWorkGenerateRecipe;
import com.hrznstudio.titanium.client.screen.addon.SlotsScreenAddon;
import com.hrznstudio.titanium.client.screen.asset.DefaultAssetProvider;
import com.hrznstudio.titanium.util.LangUtil;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StoneWorkGeneratorEmiRecipe implements EmiRecipe {
    private final ResourceLocation id;
    private final EmiStack output;
    private final int waterNeed;
    private final int lavaNeed;
    private final int waterConsume;
    private final int lavaConsume;

    public StoneWorkGeneratorEmiRecipe(StoneWorkGenerateRecipe recipe) {
        this.id = recipe.getId();
        this.output = EmiStack.of(recipe.output);
        this.waterNeed = recipe.waterNeed;
        this.lavaNeed = recipe.lavaNeed;
        this.waterConsume = recipe.waterConsume;
        this.lavaConsume = recipe.lavaConsume;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EMICustomPlugin.STONE_WORK_GENERATOR_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(
                EmiStack.of(Fluids.WATER, waterConsume),
                EmiStack.of(Fluids.LAVA, lavaConsume)
        );
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return List.of(
                EmiStack.of(Fluids.WATER, waterNeed - waterConsume),
                EmiStack.of(Fluids.LAVA, lavaNeed - waterConsume)
        );
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public int getDisplayWidth() {
        return 130;
    }

    @Override
    public int getDisplayHeight() {
        return 9 * 6;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addDrawable(0, 0, 130, 9 * 6, (guiGraphics, mouseX, mouseY, delta) -> {
            List<Component> lines = new ArrayList<>();
            SlotsScreenAddon.drawAsset(guiGraphics, Minecraft.getInstance().screen, DefaultAssetProvider.DEFAULT_PROVIDER, 8, 9 * 2, 0, 0, 1, integer -> Pair.of(1, 1), integer -> ItemStack.EMPTY, true, integer -> new Color(DyeColor.ORANGE.getFireworkColor()), integer -> true, 1);
            lines.add(Component.literal(ChatFormatting.GOLD + LangUtil.getString("tooltip.industrialforegoing.needs")));
            lines.add(Component.literal(ChatFormatting.YELLOW + " - " + ChatFormatting.DARK_GRAY + waterNeed + ChatFormatting.DARK_AQUA + LangUtil.getString("tooltip.industrialforegoing.mb_of", LangUtil.getString("block.minecraft.water"))));
            lines.add(Component.literal(ChatFormatting.YELLOW + " - " + ChatFormatting.DARK_GRAY + lavaNeed + ChatFormatting.DARK_AQUA + LangUtil.getString("tooltip.industrialforegoing.mb_of", LangUtil.getString("block.minecraft.lava"))));
            lines.add(Component.literal(ChatFormatting.GOLD + LangUtil.getString("tooltip.industrialforegoing.consumes")));
            lines.add(Component.literal(ChatFormatting.YELLOW + " - " + ChatFormatting.DARK_GRAY + waterConsume + ChatFormatting.DARK_AQUA + LangUtil.getString("tooltip.industrialforegoing.mb_of", LangUtil.getString("block.minecraft.water"))));
            lines.add(Component.literal(ChatFormatting.YELLOW + " - " + ChatFormatting.DARK_GRAY + lavaConsume + ChatFormatting.DARK_AQUA + LangUtil.getString("tooltip.industrialforegoing.mb_of", LangUtil.getString("block.minecraft.lava"))));
            int y = 0;
            for (Component line : lines) {
                guiGraphics.drawString(Minecraft.getInstance().font, line.getString(), 36, y * Minecraft.getInstance().font.lineHeight, 0xFFFFFFFF, false);
                ++y;
            }
        });
        widgets.addSlot(output, 8, 18)
                .drawBack(false)
                .recipeContext(this);
    }
}
