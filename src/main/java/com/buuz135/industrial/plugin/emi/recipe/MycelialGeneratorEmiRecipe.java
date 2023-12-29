package com.buuz135.industrial.plugin.emi.recipe;

import com.buuz135.industrial.block.generator.mycelial.IMycelialGeneratorType;
import com.buuz135.industrial.plugin.emi.EMICustomPlugin;
import com.buuz135.industrial.plugin.jei.generator.MycelialGeneratorRecipe;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.client.screen.addon.SlotsScreenAddon;
import com.hrznstudio.titanium.client.screen.asset.DefaultAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
import dev.emi.emi.api.forge.ForgeEmiStack;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TankWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MycelialGeneratorEmiRecipe implements EmiRecipe {
    private final List<EmiIngredient> inputItems;
    private final List<EmiIngredient> fluidItems;
    private final int powerTick;
    private final int ticks;
    private final IMycelialGeneratorType type;

    public MycelialGeneratorEmiRecipe(IMycelialGeneratorType type, MycelialGeneratorRecipe recipe) {
        this.inputItems = recipe.getInputItems()
                .stream()
                .map(l -> l.stream()
                        .map(EmiIngredient::of)
                        .toList())
                .map(EmiIngredient::of)
                .toList();
        this.fluidItems = recipe.getFluidItems()
                .stream()
                .map(l -> l.stream()
                        .map(ForgeEmiStack::of)
                        .toList())
                .map(EmiIngredient::of)
                .toList();
        this.powerTick = recipe.getPowerTick();
        this.ticks = recipe.getTicks();
        this.type = type;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EMICustomPlugin.MYCELIAL_GENERATOR_CATEGORIES.get(type);
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return null;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        List<EmiIngredient> result = new ArrayList<>();
        result.addAll(inputItems);
        result.addAll(fluidItems);
        return result;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of();
    }

    @Override
    public int getDisplayWidth() {
        return 20 * type.getInputs().length + 110;
    }

    @Override
    public int getDisplayHeight() {
        return Minecraft.getInstance().font.lineHeight * 3;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addDrawable(0, 0, getDisplayWidth(), getDisplayHeight(), (guiGraphics, mouseX, mouseY, delta) -> {
            for (int i = 0; i < type.getInputs().length; i++) {
                if (type.getInputs()[i] == IMycelialGeneratorType.Input.SLOT) {
                    int finalI = i;
                    SlotsScreenAddon.drawAsset(guiGraphics, Minecraft.getInstance().screen, DefaultAssetProvider.DEFAULT_PROVIDER, 20 * i, Minecraft.getInstance().font.lineHeight / 2, 0, 0, 1, integer -> Pair.of(1, 1), integer -> ItemStack.EMPTY, true, integer -> new Color(type.getInputColors()[finalI].getFireworkColor()), integer -> true, 1);
                } else if (type.getInputs()[i] == IMycelialGeneratorType.Input.TANK) {
                    AssetUtil.drawAsset(guiGraphics, Minecraft.getInstance().screen, DefaultAssetProvider.DEFAULT_PROVIDER.getAsset(AssetTypes.TANK_SMALL), 20 * i, Minecraft.getInstance().font.lineHeight / 2);
                }
            }
            int x = 20 * type.getInputs().length + 3;
            guiGraphics.drawString(Minecraft.getInstance().font, ChatFormatting.DARK_GRAY + "Time: " + ChatFormatting.DARK_AQUA + new DecimalFormat().format(ticks / 20D) + ChatFormatting.DARK_GRAY + " s", x, Minecraft.getInstance().font.lineHeight * 0, 0xFFFFFFFF, false);
            guiGraphics.drawString(Minecraft.getInstance().font, ChatFormatting.DARK_GRAY + "Production: " + ChatFormatting.DARK_AQUA + powerTick + ChatFormatting.DARK_GRAY + " FE/t", x, Minecraft.getInstance().font.lineHeight * 1, 0xFFFFFFFF, false);
            guiGraphics.drawString(Minecraft.getInstance().font, ChatFormatting.DARK_GRAY + "Total: " + ChatFormatting.DARK_AQUA + new DecimalFormat().format(ticks * powerTick) + ChatFormatting.DARK_GRAY + " FE", x, Minecraft.getInstance().font.lineHeight * 2, 0xFFFFFFFF, false);
        });

        for (int i = 0; i < type.getInputs().length; i++) {
            IMycelialGeneratorType.Input input = type.getInputs()[i];
            if (input == IMycelialGeneratorType.Input.SLOT) {
                widgets.addSlot(inputItems.get(i), 20 * i, Minecraft.getInstance().font.lineHeight / 2)
                        .drawBack(false);
            } else if (input == IMycelialGeneratorType.Input.TANK) {
                widgets.add(new TankWidget(fluidItems.get(i), 20 * i + 2, 2 + Minecraft.getInstance().font.lineHeight / 2, 14, 15, 1000))
                        .drawBack(false);
            }
        }
    }
}
