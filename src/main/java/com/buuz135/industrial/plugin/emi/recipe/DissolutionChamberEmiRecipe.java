package com.buuz135.industrial.plugin.emi.recipe;

import com.buuz135.industrial.block.core.tile.DissolutionChamberTile;
import com.buuz135.industrial.config.machine.core.DissolutionChamberConfig;
import com.buuz135.industrial.plugin.emi.EMICustomPlugin;
import com.buuz135.industrial.recipe.DissolutionChamberRecipe;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.client.screen.addon.EnergyBarScreenAddon;
import com.hrznstudio.titanium.client.screen.addon.SlotsScreenAddon;
import com.hrznstudio.titanium.client.screen.asset.DefaultAssetProvider;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.emi.api.forge.ForgeEmiStack;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TankWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DissolutionChamberEmiRecipe implements EmiRecipe {
    private static final EmiTexture SMALL_TANK = new EmiTexture(DefaultAssetProvider.DEFAULT_LOCATION, 235 + 3, 1 + 3, 12, 13);
    private static final EmiTexture BIG_TANK = new EmiTexture(DefaultAssetProvider.DEFAULT_LOCATION, 177 + 3, 1 + 3, 12, 50);
    private final ResourceLocation id;
    private final List<EmiIngredient> inputItems;
    private final EmiStack output;
    private final EmiIngredient inputFluid;
    private final EmiStack outputFluid;
    private final int processingTime;

    public DissolutionChamberEmiRecipe(DissolutionChamberRecipe recipe) {
        this.id = recipe.getId();
        inputItems = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            inputItems.add(EmiStack.EMPTY);
        }
        for (int i = 0; i < recipe.input.length; i++) {
            inputItems.set(i, EmiIngredient.of(recipe.input[i].getItems().stream().map(EmiStack::of).toList()));
        }
        this.output = EmiStack.of(recipe.output);
        this.inputFluid = ForgeEmiStack.of(recipe.inputFluid);
        this.outputFluid = recipe.outputFluid == null ? EmiStack.EMPTY : ForgeEmiStack.of(recipe.outputFluid);
        this.processingTime = recipe.processingTime;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EMICustomPlugin.DISSOLUTION_CHAMBER_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        List<EmiIngredient> inputs = new ArrayList<>(inputItems);
        inputs.add(inputFluid);
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output, outputFluid);
    }

    @Override
    public int getDisplayWidth() {
        return 160;
    }

    @Override
    public int getDisplayHeight() {
        return 82;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addDrawable(0, 0, 160, 82, (guiGraphics, i, i1, v) -> {
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            poseStack.translate(0, 0, 0);
            EnergyBarScreenAddon.drawBackground(guiGraphics, Minecraft.getInstance().screen, DefaultAssetProvider.DEFAULT_PROVIDER, 0, 12, 0, 0);


            SlotsScreenAddon.drawAsset(guiGraphics, Minecraft.getInstance().screen, DefaultAssetProvider.DEFAULT_PROVIDER, 24, 11, 0, 0, 8, DissolutionChamberTile::getSlotPos, integer -> ItemStack.EMPTY, true, integer -> new Color(DyeColor.LIGHT_BLUE.getFireworkColor()), integer -> true, 0);
            SlotsScreenAddon.drawAsset(guiGraphics, Minecraft.getInstance().screen, DefaultAssetProvider.DEFAULT_PROVIDER, 119, 16, 0, 0, 3, integer -> Pair.of(18 * (integer % 1), 18 * (integer / 1)), integer -> ItemStack.EMPTY, true, integer -> new Color(DyeColor.ORANGE.getFireworkColor()), integer -> true, 0);

            AssetUtil.drawAsset(guiGraphics, Minecraft.getInstance().screen, DefaultAssetProvider.DEFAULT_PROVIDER.getAsset(AssetTypes.TANK_SMALL), 33 + 12, 32);
            AssetUtil.drawAsset(guiGraphics, Minecraft.getInstance().screen, DefaultAssetProvider.DEFAULT_PROVIDER.getAsset(AssetTypes.TANK_NORMAL), 139, 14);

            AssetUtil.drawAsset(guiGraphics, Minecraft.getInstance().screen, IAssetProvider.getAsset(DefaultAssetProvider.DEFAULT_PROVIDER, AssetTypes.PROGRESS_BAR_BACKGROUND_ARROW_HORIZONTAL), 92, 41 - 8);

            int consumed = processingTime * DissolutionChamberConfig.powerPerTick;
            EnergyBarScreenAddon.drawForeground(guiGraphics, Minecraft.getInstance().screen, DefaultAssetProvider.DEFAULT_PROVIDER, 0, 12, 0, 0, consumed, (int) Math.max(50000, Math.ceil(consumed)));
            poseStack.popPose();
        });

        for (int i = 0; i < 8; i++) {
            widgets.addSlot(
                    inputItems.get(i),
                    23 + DissolutionChamberTile.getSlotPos(i).getLeft(),
                    10 + DissolutionChamberTile.getSlotPos(i).getRight()
            ).drawBack(false);
        }
        widgets.add(new TankWidget(inputFluid, 33 + 12 + 2, 32 + 2, 14, 15, 1000))
                .drawBack(false);
        widgets.addSlot(output, 118, 15)
                .drawBack(false)
                .recipeContext(this);
        widgets.add(new TankWidget(outputFluid, 139 + 2, 14 + 2, 14, 52, 1000))
                .drawBack(false)
                .recipeContext(this);
    }
}
