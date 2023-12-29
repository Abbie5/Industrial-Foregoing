package com.buuz135.industrial.plugin.emi;

import com.buuz135.industrial.api.recipe.ore.OreFluidEntryFermenter;
import com.buuz135.industrial.api.recipe.ore.OreFluidEntryRaw;
import com.buuz135.industrial.api.recipe.ore.OreFluidEntrySieve;
import com.buuz135.industrial.block.generator.MycelialGeneratorBlock;
import com.buuz135.industrial.block.generator.mycelial.IMycelialGeneratorType;
import com.buuz135.industrial.block.generator.tile.BioReactorTile;
import com.buuz135.industrial.block.resourceproduction.tile.MaterialStoneWorkFactoryTile;
import com.buuz135.industrial.fluid.OreTitaniumFluidType;
import com.buuz135.industrial.module.*;
import com.buuz135.industrial.plugin.emi.recipe.*;
import com.buuz135.industrial.plugin.jei.generator.MycelialGeneratorRecipe;
import com.buuz135.industrial.recipe.StoneWorkGenerateRecipe;
import com.buuz135.industrial.utils.IndustrialTags;
import com.buuz135.industrial.utils.Reference;
import com.hrznstudio.titanium.util.RecipeUtil;
import com.hrznstudio.titanium.util.TagUtil;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Function;

@EmiEntrypoint
public class EMICustomPlugin implements EmiPlugin {
    public static final EmiRecipeCategory BIO_REACTOR_CATEGORY;
    public static final EmiRecipeCategory LASER_DRILL_ORE_CATEGORY;
    public static final EmiRecipeCategory LASER_DRILL_FLUID_CATEGORY;
    public static final EmiRecipeCategory FLUID_EXTRACTOR_CATEGORY;
    public static final EmiRecipeCategory DISSOLUTION_CHAMBER_CATEGORY;
    public static final Map<IMycelialGeneratorType, EmiRecipeCategory> MYCELIAL_GENERATOR_CATEGORIES;
    public static final EmiRecipeCategory STONE_WORK_CATEGORY;
    public static final EmiRecipeCategory MACHINE_PRODUCE_CATEGORY;
    public static final EmiRecipeCategory STONE_WORK_GENERATOR_CATEGORY;
    public static final EmiRecipeCategory ORE_WASHER_CATEGORY;
    public static final EmiRecipeCategory FERMENTATION_STATION_CATEGORY;
    public static final EmiRecipeCategory FLUID_SIEVE_CATEGORY;

    static {
        BIO_REACTOR_CATEGORY = new NamedEmiRecipeCategory(
                new ResourceLocation(Reference.MOD_ID, "bioreactor"),
                EmiStack.of(ModuleGenerator.BIOREACTOR.getLeft().get()),
                Component.literal("Bioreactor accepted items")
        );
        LASER_DRILL_ORE_CATEGORY = new NamedEmiRecipeCategory(
                new ResourceLocation(Reference.MOD_ID, "laser_ore"),
                EmiStack.of(ModuleResourceProduction.ORE_LASER_BASE.getLeft().get()),
                Component.literal("Laser Drill Items")
        );
        LASER_DRILL_FLUID_CATEGORY = new NamedEmiRecipeCategory(
                new ResourceLocation(Reference.MOD_ID, "laser_fluid"),
                EmiStack.of(ModuleResourceProduction.FLUID_LASER_BASE.getLeft().get()),
                Component.literal("Laser Drill Fluids")
        );
        DISSOLUTION_CHAMBER_CATEGORY = new NamedEmiRecipeCategory(
                new ResourceLocation(Reference.MOD_ID, "dissolution"),
                EmiStack.of(ModuleCore.DISSOLUTION_CHAMBER.getLeft().get()),
                ModuleCore.DISSOLUTION_CHAMBER.getLeft().get().getName()
        );
        FERMENTATION_STATION_CATEGORY = new NamedEmiRecipeCategory(
                new ResourceLocation(Reference.MOD_ID, "fermenter"),
                EmiStack.of(ModuleResourceProduction.FERMENTATION_STATION.getLeft().get()),
                ModuleResourceProduction.FERMENTATION_STATION.getLeft().get().getName()
        );
        FLUID_EXTRACTOR_CATEGORY = new NamedEmiRecipeCategory(
                new ResourceLocation(Reference.MOD_ID, "fluid_extractor"),
                EmiStack.of(ModuleCore.FLUID_EXTRACTOR.getLeft().get()),
                ModuleCore.FLUID_EXTRACTOR.getLeft().get().getName()
        );
        FLUID_SIEVE_CATEGORY = new NamedEmiRecipeCategory(
                new ResourceLocation(Reference.MOD_ID, "ore_sieve"),
                EmiStack.of(ModuleResourceProduction.FLUID_SIEVING_MACHINE.getLeft().get()),
                ModuleResourceProduction.FLUID_SIEVING_MACHINE.getLeft().get().getName()
        );
        ORE_WASHER_CATEGORY = new NamedEmiRecipeCategory(
                new ResourceLocation(Reference.MOD_ID, "ore_washer"),
                EmiStack.of(ModuleResourceProduction.WASHING_FACTORY.getLeft().get()),
                ModuleResourceProduction.WASHING_FACTORY.getLeft().get().getName()
        );
        STONE_WORK_CATEGORY = new NamedEmiRecipeCategory(
                new ResourceLocation(Reference.MOD_ID, "stone_work"),
                EmiStack.of(ModuleResourceProduction.MATERIAL_STONEWORK_FACTORY.getLeft().get()),
                ModuleResourceProduction.MATERIAL_STONEWORK_FACTORY.getLeft().get().getName()
        );
        STONE_WORK_GENERATOR_CATEGORY = new NamedEmiRecipeCategory(
                new ResourceLocation(Reference.MOD_ID, "stone_work_generator"),
                EmiStack.of(ModuleResourceProduction.MATERIAL_STONEWORK_FACTORY.getLeft().get()),
                Component.literal("StoneWork Generation")
        );
        MACHINE_PRODUCE_CATEGORY = new NamedEmiRecipeCategory(
                new ResourceLocation(Reference.MOD_ID, "machine_produce"),
                (guiGraphics, i, i1, v) -> {},
                Component.literal("Machine Outputs")
        );

        MYCELIAL_GENERATOR_CATEGORIES = new HashMap<>();
        for (Pair<RegistryObject<Block>, RegistryObject<BlockEntityType<?>>> mycelialGenerator : ModuleGenerator.MYCELIAL_GENERATORS) {
            IMycelialGeneratorType type = ((MycelialGeneratorBlock) mycelialGenerator.getLeft().get()).getType();
            MYCELIAL_GENERATOR_CATEGORIES.put(
                    type,
                    new NamedEmiRecipeCategory(
                            new ResourceLocation(Reference.MOD_ID, "mycelial_" + type.getName()),
                            EmiStack.of(mycelialGenerator.getLeft().get()),
                            Component.translatable("industrialforegoing.jei.category." + type.getName())
                    )
            );
        }
    }

    @Override
    public void register(EmiRegistry registry) {
        // register categories
        registry.addCategory(BIO_REACTOR_CATEGORY);
        registry.addCategory(LASER_DRILL_ORE_CATEGORY);
        registry.addCategory(LASER_DRILL_FLUID_CATEGORY);
        registry.addCategory(FLUID_EXTRACTOR_CATEGORY);
        registry.addCategory(DISSOLUTION_CHAMBER_CATEGORY);
        MYCELIAL_GENERATOR_CATEGORIES.values().forEach(registry::addCategory);
        registry.addCategory(STONE_WORK_CATEGORY);
        registry.addCategory(MACHINE_PRODUCE_CATEGORY);
        registry.addCategory(STONE_WORK_GENERATOR_CATEGORY);
        registry.addCategory(ORE_WASHER_CATEGORY);
        registry.addCategory(FERMENTATION_STATION_CATEGORY);
        registry.addCategory(FLUID_SIEVE_CATEGORY);
        
        // register workstations
        registry.addWorkstation(FLUID_EXTRACTOR_CATEGORY, EmiStack.of(ModuleCore.FLUID_EXTRACTOR.getLeft().get()));
        registry.addWorkstation(DISSOLUTION_CHAMBER_CATEGORY, EmiStack.of(ModuleCore.DISSOLUTION_CHAMBER.getLeft().get()));
        registry.addWorkstation(BIO_REACTOR_CATEGORY, EmiStack.of(ModuleGenerator.BIOREACTOR.getLeft().get()));
        registry.addWorkstation(LASER_DRILL_ORE_CATEGORY, EmiStack.of(ModuleResourceProduction.ORE_LASER_BASE.getLeft().get()));
        registry.addWorkstation(LASER_DRILL_ORE_CATEGORY, EmiStack.of(ModuleResourceProduction.LASER_DRILL.getLeft().get()));
        registry.addWorkstation(LASER_DRILL_FLUID_CATEGORY, EmiStack.of(ModuleResourceProduction.FLUID_LASER_BASE.getLeft().get()));
        registry.addWorkstation(LASER_DRILL_FLUID_CATEGORY, EmiStack.of(ModuleResourceProduction.LASER_DRILL.getLeft().get()));
        for (Pair<RegistryObject<Block>, RegistryObject<BlockEntityType<?>>> mycelialGenerator : ModuleGenerator.MYCELIAL_GENERATORS) {
            for (Map.Entry<IMycelialGeneratorType, EmiRecipeCategory> entry : MYCELIAL_GENERATOR_CATEGORIES.entrySet()) {
                if (((MycelialGeneratorBlock) mycelialGenerator.getLeft().get()).getType().equals(entry.getKey())) {
                    registry.addWorkstation(entry.getValue(), EmiStack.of(mycelialGenerator.getLeft().get()));
                }
            }
        }
        registry.addWorkstation(STONE_WORK_CATEGORY, EmiStack.of(ModuleResourceProduction.MATERIAL_STONEWORK_FACTORY.getLeft().get()));
        registry.addWorkstation(STONE_WORK_GENERATOR_CATEGORY, EmiStack.of(ModuleResourceProduction.MATERIAL_STONEWORK_FACTORY.getLeft().get()));
        registry.addWorkstation(VanillaEmiRecipeCategories.BREWING, EmiStack.of(ModuleResourceProduction.POTION_BREWER.getLeft().get()));
        registry.addWorkstation(VanillaEmiRecipeCategories.ANVIL_REPAIRING, EmiStack.of(ModuleMisc.ENCHANTMENT_APPLICATOR.getLeft().get()));
        registry.addWorkstation(VanillaEmiRecipeCategories.SMELTING, EmiStack.of(ModuleResourceProduction.RESOURCEFUL_FURNACE.getLeft().get()));
        registry.addWorkstation(ORE_WASHER_CATEGORY, EmiStack.of(ModuleResourceProduction.WASHING_FACTORY.getLeft().get()));
        registry.addWorkstation(FERMENTATION_STATION_CATEGORY, EmiStack.of(ModuleResourceProduction.FERMENTATION_STATION.getLeft().get()));
        registry.addWorkstation(FLUID_SIEVE_CATEGORY, EmiStack.of(ModuleResourceProduction.FLUID_SIEVING_MACHINE.getLeft().get()));

        // register recipes
        RecipeManager manager = registry.getRecipeManager();
        addRecipes(registry, manager, ModuleCore.FLUID_EXTRACTOR_TYPE.get(), FluidExtractorEmiRecipe::new);
        addRecipes(registry, manager, ModuleCore.DISSOLUTION_TYPE.get(), DissolutionChamberEmiRecipe::new);


        Arrays.stream(BioReactorTile.VALID)
                .map(tag -> new BioReactorEmiRecipe(tag, new FluidStack(ModuleCore.BIOFUEL.getSourceFluid().get(), 80)))
                .forEach(registry::addRecipe);

        addRecipes(registry, manager, ModuleCore.LASER_DRILL_TYPE.get(), LaserDrillOreEmiRecipe::new);
        addRecipes(registry, manager, ModuleCore.LASER_DRILL_FLUID_TYPE.get(), LaserDrillFluidEmiRecipe::new);

        for (IMycelialGeneratorType type : IMycelialGeneratorType.TYPES) {
                for (MycelialGeneratorRecipe recipe : type.getRecipes()) {
                    registry.addRecipe(new MycelialGeneratorEmiRecipe(type, recipe));
                }
        }

        List<StoneWorkEmiRecipe> perfectStoneWorkWrappers = new ArrayList<>();
        for (StoneWorkGenerateRecipe generatorRecipe : RecipeUtil.getRecipes(Minecraft.getInstance().level, (RecipeType<StoneWorkGenerateRecipe>) ModuleCore.STONEWORK_GENERATE_TYPE.get())) {
            List<StoneWorkEmiRecipe> wrappers = findAllStoneWorkOutputs(generatorRecipe.output, new ArrayList<>());
            for (StoneWorkEmiRecipe workWrapper : new ArrayList<>(wrappers)) {
                if (perfectStoneWorkWrappers.stream().noneMatch(stoneWorkWrapper -> ItemStack.isSameItem(workWrapper.getOutputs().get(0).getItemStack(), stoneWorkWrapper.getOutputs().get(0).getItemStack()))) {
                    boolean isSomoneShorter = false;
                    for (StoneWorkEmiRecipe workWrapperCompare : new ArrayList<>(wrappers)) {
                        if (ItemStack.isSameItem(workWrapper.getOutputs().get(0).getItemStack(), workWrapperCompare.getOutputs().get(0).getItemStack())) {
                            List<MaterialStoneWorkFactoryTile.StoneWorkAction> workWrapperCompareModes = new ArrayList<>(workWrapperCompare.getModes());
                            workWrapperCompareModes.removeIf(mode -> mode.getAction().equalsIgnoreCase("none"));
                            List<MaterialStoneWorkFactoryTile.StoneWorkAction> workWrapperModes = new ArrayList<>(workWrapper.getModes());
                            workWrapperModes.removeIf(mode -> mode.getAction().equalsIgnoreCase("none"));
                            if (workWrapperModes.size() > workWrapperCompareModes.size()) {
                                isSomoneShorter = true;
                                break;
                            }
                        }
                    }
                    if (!isSomoneShorter) perfectStoneWorkWrappers.add(workWrapper);
                }
            }
        }
        perfectStoneWorkWrappers.forEach(registry::addRecipe);

        Arrays.asList(
                new MachineProduceEmiRecipe(ModuleCore.LATEX_PROCESSING.getLeft().get(), new ItemStack(ModuleCore.DRY_RUBBER.get())),
                new MachineProduceEmiRecipe(ModuleResourceProduction.SLUDGE_REFINER.getLeft().get(), IndustrialTags.Items.SLUDGE_OUTPUT),
                new MachineProduceEmiRecipe(ModuleAgricultureHusbandry.SEWAGE_COMPOSTER.getLeft().get(), new ItemStack(ModuleCore.FERTILIZER.get())),
                new MachineProduceEmiRecipe(ModuleResourceProduction.DYE_MIXER.getLeft().get(), Tags.Items.DYES),
                new MachineProduceEmiRecipe(ModuleResourceProduction.SPORES_RECREATOR.getLeft().get(), Tags.Items.MUSHROOMS),
                new MachineProduceEmiRecipe(ModuleResourceProduction.SPORES_RECREATOR.getLeft().get(), new ItemStack(Items.CRIMSON_FUNGUS), new ItemStack(Items.WARPED_FUNGUS)),
                new MachineProduceEmiRecipe(ModuleAgricultureHusbandry.MOB_CRUSHER.getLeft().get(), new FluidStack(ModuleCore.ESSENCE.getSourceFluid().get(), 1000)),
                new MachineProduceEmiRecipe(ModuleAgricultureHusbandry.SLAUGHTER_FACTORY.getLeft().get(), new FluidStack(ModuleCore.MEAT.getSourceFluid().get(), 1000)),
                new MachineProduceEmiRecipe(ModuleAgricultureHusbandry.SLAUGHTER_FACTORY.getLeft().get(), new FluidStack(ModuleCore.PINK_SLIME.getSourceFluid().get(), 1000)),
                new MachineProduceEmiRecipe(ModuleAgricultureHusbandry.ANIMAL_RANCHER.getLeft().get(), new FluidStack(ForgeMod.MILK.get(), 1000)),
                new MachineProduceEmiRecipe(ModuleAgricultureHusbandry.SEWER.getLeft().get(), new FluidStack(ModuleCore.SEWAGE.getSourceFluid().get(), 1000)),
                new MachineProduceEmiRecipe(ModuleAgricultureHusbandry.PLANT_GATHERER.getLeft().get(), new FluidStack(ModuleCore.SLUDGE.getSourceFluid().get(), 1000)),
                new MachineProduceEmiRecipe(ModuleResourceProduction.WATER_CONDENSATOR.getLeft().get(), new FluidStack(Fluids.WATER, 1000))
        ).forEach(registry::addRecipe);

        addRecipes(registry, manager, ModuleCore.STONEWORK_GENERATE_TYPE.get(), StoneWorkGeneratorEmiRecipe::new);

        List<OreFluidEntryRaw> washer = new ArrayList<>();
        List<OreFluidEntryFermenter> fluidEntryFermenters = new ArrayList<>();
        List<OreFluidEntrySieve> fluidSieve = new ArrayList<>();

        ForgeRegistries.ITEMS.tags().getTagNames().map(itemTagKey -> itemTagKey.location())
                .filter(resourceLocation -> resourceLocation.toString().startsWith("forge:raw_materials/") && OreTitaniumFluidType.isValid(resourceLocation))
                .forEach(resourceLocation -> {
                    TagKey<Item> tag = ForgeRegistries.ITEMS.tags().createTagKey(resourceLocation);
                    TagKey<Item> dust = ForgeRegistries.ITEMS.tags().createTagKey(new ResourceLocation(resourceLocation.toString().replace("forge:raw_materials/", "forge:dusts/")));
                    washer.add(new OreFluidEntryRaw(tag, new FluidStack(ModuleCore.MEAT.getSourceFluid().get(), 100), OreTitaniumFluidType.getFluidWithTag(ModuleCore.RAW_ORE_MEAT, 100, resourceLocation)));
                    fluidEntryFermenters.add(new OreFluidEntryFermenter(OreTitaniumFluidType.getFluidWithTag(ModuleCore.RAW_ORE_MEAT, 100, resourceLocation), OreTitaniumFluidType.getFluidWithTag(ModuleCore.FERMENTED_ORE_MEAT, 200, resourceLocation)));
                    fluidSieve.add(new OreFluidEntrySieve(OreTitaniumFluidType.getFluidWithTag(ModuleCore.FERMENTED_ORE_MEAT, 100, resourceLocation), TagUtil.getItemWithPreference(dust), ItemTags.SAND));
                });
        washer.stream()
                .map(OreWasherEmiRecipe::new)
                .forEach(registry::addRecipe);
        fluidEntryFermenters.stream()
                .map(FermenterEmiRecipe::new)
                .forEach(registry::addRecipe);
        fluidSieve.stream()
                .map(OreSieveEmiRecipe::new)
                .forEach(registry::addRecipe);
    }

    private static <C extends Container, T extends Recipe<C>> void addRecipes(EmiRegistry registry, RecipeManager manager, RecipeType<?> type, Function<T, EmiRecipe> emiRecipeConstructor) {
        manager.getAllRecipesFor((RecipeType<T>) type)
                .stream()
                .map(emiRecipeConstructor)
                .forEach(registry::addRecipe);
    }

    private List<StoneWorkEmiRecipe> findAllStoneWorkOutputs(ItemStack parent, List<MaterialStoneWorkFactoryTile.StoneWorkAction> usedModes) {
        List<StoneWorkEmiRecipe> wrappers = new ArrayList<>();
        if (usedModes.size() >= 4) return wrappers;
        for (MaterialStoneWorkFactoryTile.StoneWorkAction mode : MaterialStoneWorkFactoryTile.ACTION_RECIPES) {
            if (mode.getAction().equals("none")) continue;
            List<MaterialStoneWorkFactoryTile.StoneWorkAction> usedModesInternal = new ArrayList<>(usedModes);
            usedModesInternal.add(mode);
            ItemStack output = getStoneWorkOutputFrom(parent, new ArrayList<>(usedModesInternal));
            if (!output.isEmpty()) {
                wrappers.add(new StoneWorkEmiRecipe(parent, new ArrayList<>(usedModesInternal), output.copy()));
                wrappers.addAll(findAllStoneWorkOutputs(parent, new ArrayList<>(usedModesInternal)));
            }
        }
        return wrappers;
    }

    private ItemStack getStoneWorkOutputFrom(ItemStack stack, MaterialStoneWorkFactoryTile.StoneWorkAction mode) {
        return mode.getWork().apply(Minecraft.getInstance().level, ItemHandlerHelper.copyStackWithSize(stack, 9));
    }

    private ItemStack getStoneWorkOutputFrom(ItemStack stack, List<MaterialStoneWorkFactoryTile.StoneWorkAction> modes) {
        for (MaterialStoneWorkFactoryTile.StoneWorkAction mode : modes) {
            stack = getStoneWorkOutputFrom(stack.copy(), mode);
            if (stack.isEmpty()) return ItemStack.EMPTY;
        }
        return stack;
    }

    private static class NamedEmiRecipeCategory extends EmiRecipeCategory {
        private final Component name;

        public NamedEmiRecipeCategory(ResourceLocation id, EmiRenderable icon, Component name) {
            super(id, icon);
            this.name = name;
        }

        @Override
        public Component getName() {
            return name;
        }
    }
}
