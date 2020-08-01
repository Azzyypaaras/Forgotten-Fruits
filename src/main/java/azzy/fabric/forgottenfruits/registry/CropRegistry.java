package azzy.fabric.forgottenfruits.registry;

import azzy.fabric.forgottenfruits.block.*;
import azzy.fabric.forgottenfruits.block.Plant.PlantReferences;
import azzy.fabric.forgottenfruits.util.interaction.PlantType.*;
import azzy.fabric.forgottenfruits.util.context.ContextConsumer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricMaterialBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.fluid.Fluids;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import static azzy.fabric.forgottenfruits.ForgottenFruits.MOD_ID;
import static azzy.fabric.forgottenfruits.registry.ItemRegistry.*;
import static azzy.fabric.forgottenfruits.util.context.PlantPackage.Context.*;

public class CropRegistry {

    private static final Material VOMPOLLOLOWM = (new FabricMaterialBuilder(MaterialColor.FOLIAGE)).lightPassesThrough().destroyedByPiston().burnable().allowsMovement().build();

    public static final Block CLOUD_BERRY_CROP = register("cloudberry_crop", new PlantBase(PLANTTYPE.FARM, 6, Material.PLANT, BlockSoundGroup.CROP, CLOUD_BERRY_SEEDS, 8, 16));
    public static final Block CLOUD_BERRY_WILD = register("cloudberry_wild", new WildPlantBase(PLANTTYPE.GENERIC, Material.PLANT, BlockSoundGroup.CROP));

    public static final Block CINDERMOTE_CROP = register("cindermote_crop", new PlantBase(PLANTTYPE.VOLCANIC, 7, Material.SOLID_ORGANIC, BlockSoundGroup.NETHER_WART, CINDERMOTE_SEEDS, 0, 16, ContextConsumer.of(PlantReferences::cindermoteCollision, COLLISION), ContextConsumer.of(PlantReferences::cindermoteParticles, PARTICLE)));
    public static final Block CINDERMOTE_WILD = register("cindermote_wild", new WildPlantBase(PLANTTYPE.HELLISH, Material.SOLID_ORGANIC, BlockSoundGroup.NETHER_WART, ContextConsumer.of(PlantReferences::cindermoteCollision, COLLISION), ContextConsumer.of(PlantReferences::cindermoteParticles, PARTICLE)));

    public static final Block VOMPOLLOLOWM_CROP_BASE = register("vompollolowm_crop", new PlantBase(PLANTTYPE.FARM, 4, Material.PLANT, BlockSoundGroup.CROP, VOMPOLLOLOWM_SEEDS, 8, 16, VoxelShapes.cuboid(0.4375, 0, 0.4375, 0.5625, 1, 0.5625), true, ContextConsumer.of(PlantReferences::vompollolowmBaseGrowth, GROWTH), ContextConsumer.of(PlantReferences::vompollolowmScheduler, BROKEN), ContextConsumer.of(PlantReferences::vompollolowmBaseTick, SCHEDULED)));
    public static final Block VOMPOLLOLOWM_CROP_STALK = register("vompollolowm_stalk", new PlantBase(PLANTTYPE.NULL, 2, VOMPOLLOLOWM, BlockSoundGroup.CROP, null, 0, 16, VoxelShapes.cuboid(0.4375, 0, 0.4375, 0.5625, 1, 0.5625), true, ContextConsumer.of(PlantReferences::vompollolowmStalkGrowth, GROWTH), ContextConsumer.of(PlantReferences::vompollolowmScheduler, BROKEN), ContextConsumer.of(PlantReferences::vompollolowmBreakTick, SCHEDULED)));
    public static final Block VOMPOLLOLOWM_CROP_FRUIT = register("vompollolowm_fruit", new PlantBase(PLANTTYPE.NULL, 1, VOMPOLLOLOWM, BlockSoundGroup.CROP, null, 0, 16, VoxelShapes.cuboid(0.125, 0.125, 0.125, 0.875, 0.875, 0.875), true, ContextConsumer.of(PlantReferences::vompollolowmBreakTick, SCHEDULED), ContextConsumer.of(PlantReferences::vompollolowmScheduler, BROKEN)));
    public static final Block VOMPOLLOLOWM_WILD_BASE = register("vompollolowm_wild", new WildPlantBase(PLANTTYPE.ROCK, Material.PLANT, BlockSoundGroup.CROP, ContextConsumer.of(PlantReferences::vompollolowmScheduler, BROKEN), ContextConsumer.of(PlantReferences::vompollolowmBreakTick, SCHEDULED)));
    public static final Block VOMPOLLOLOWM_WILD_STALK = register("vompollolowm_wild_stalk", new WildPlantBase(PLANTTYPE.NULL, VOMPOLLOLOWM, BlockSoundGroup.CROP, VoxelShapes.cuboid(0.4375, 0, 0.4375, 0.5625, 1, 0.5625), true, ContextConsumer.of(PlantReferences::vompollolowmBreakTick, SCHEDULED), ContextConsumer.of(PlantReferences::vompollolowmScheduler, BROKEN)));
    public static final Block VOMPOLLOLOWM_WILD_FRUIT = register("vompollolowm_wild_fruit", new WildPlantBase(PLANTTYPE.NULL, VOMPOLLOLOWM, BlockSoundGroup.CROP, VoxelShapes.cuboid(0.125, 0.125, 0.125, 0.875, 0.875, 0.875), true, ContextConsumer.of(PlantReferences::vompollolowmBreakTick, SCHEDULED), ContextConsumer.of(PlantReferences::vompollolowmScheduler, BROKEN)));

    public static final Block JELLY_PEAR_CROP = register("jelly_pear_crop", new PlantBase(PLANTTYPE.AQUATIC,4, Material.UNDERWATER_PLANT, BlockSoundGroup.WET_GRASS, JELLY_PEAR_SEEDS, 4, 12, VoxelShapes.cuboid(0.125, 0, 0.125, 0.875, 0.25, 0.875), false, Fluids.WATER.getStill(false)));
    public static final Block JELLY_PEAR_WILD = register("jelly_pear_wild", new WildPlantBase(PLANTTYPE.AQUATIC, Material.UNDERWATER_PLANT, BlockSoundGroup.WET_GRASS, VoxelShapes.cuboid(0.125, 0, 0.125, 0.875, 0.25, 0.875), false, Fluids.WATER.getStill(false)));

    private static Block register(String name, Block item) {
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, name), item);
        return item;
    }

    public static void init() {
    }
}
