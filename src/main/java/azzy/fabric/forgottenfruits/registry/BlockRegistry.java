package azzy.fabric.forgottenfruits.registry;

import azzy.fabric.forgottenfruits.block.NetherFarmland;
import azzy.fabric.forgottenfruits.block.entity.BarrelBlock;
import azzy.fabric.forgottenfruits.block.entity.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;

import java.util.ArrayList;
import java.util.List;

import static azzy.fabric.forgottenfruits.ForgottenFruits.*;
import static azzy.fabric.forgottenfruits.registry.CropRegistry.*;

public class BlockRegistry {

    public static final VoxelShape DEFAULT_SHAPE = Block.createCuboidShape(1, 0, 1, 15, 15, 15);

    //Other
    public static final Block NETHER_FARMLAND = register("nether_farmland", new NetherFarmland(FabricBlockSettings.of(Material.STONE).materialColor(MaterialColor.PURPLE).nonOpaque().sounds(BlockSoundGroup.SAND).lightLevel(2).dropsLike(Blocks.OBSIDIAN).breakByTool(FabricToolTags.PICKAXES, 3).hardness(20f).ticksRandomly().lightLevel(1)), defaultSettings());

    //misc blocks
    public static final Block CLOUDBERRY_AMALGAM_BLOCK = register("cloudberry_block", new Block(FabricBlockSettings.of(Material.UNDERWATER_PLANT).materialColor(MaterialColor.ORANGE).nonOpaque().breakInstantly().sounds(BlockSoundGroup.SLIME).slipperiness(0.8f)), defaultSettings());
    public static final Block IGNOBLE = register("ignoble_silk", new Block(FabricBlockSettings.of(Material.WOOL).materialColor(MaterialColor.BROWN).sounds(BlockSoundGroup.SNOW)), materialSettings());

    //Block Entities
    public static final Block PRESS = register("press", new PressBlock(null, Material.WOOD, BlockSoundGroup.WOOD, 0, DEFAULT_SHAPE), new Item.Settings().group(BLOCK_ENTITIES));
    public static final Block BASKET = registerNoItem("basket", new BasketBlock(null, Material.WOOL, BlockSoundGroup.WOOL, 0, Block.createCuboidShape(1, 0, 1, 15, 8, 15)));
    public static final Block WOOD_PIPE = register("wooden_pipe", new WoodPipe(null, Material.WOOD, BlockSoundGroup.WOOD, 0, DEFAULT_SHAPE), machineSettings());
    public static final Block BARREL = register("barrel", new BarrelBlock(null, Material.WOOD, BlockSoundGroup.WOOD, 0, DEFAULT_SHAPE), machineSettings());
    public static final Block WITCH_CAULDRON = register("witch_cauldron", new WitchCauldronBlock(null, Material.METAL, BlockSoundGroup.METAL, 0, Block.createCuboidShape(0, 0, 0, 16, 11, 16)), machineSettings());

    //Fluids
    public static Block CLOUD_BERRY_JUICE = registerFluidBlock("cloudberry", FluidRegistry.CLOUD_BERRY, FabricBlockSettings.of(Material.WATER).noCollision().strength(100f).dropsNothing());
    public static Block CINDERMOTE_JUICE = registerFluidBlock("cindermote", FluidRegistry.CINDERMOTE, FabricBlockSettings.of(Material.WATER).strength(100f).dropsNothing().lightLevel(6));

    private static Item.Settings defaultSettings() {
        return new Item.Settings().group(PLANT_STUFF);
    }

    private static Item.Settings machineSettings() {
        return new Item.Settings().group(BLOCK_ENTITIES);
    }

    private static Item.Settings materialSettings() {
        return new Item.Settings().group(PLANT_MATERIALS);
    }

    @Environment(EnvType.CLIENT)
    public static final List<Block> REGISTRY_TRANS = new ArrayList<>();
    @Environment(EnvType.CLIENT)
    public static final List<Block> REGISTRY_PARTIAL = new ArrayList<>();

    private static Block register(String name, Block block, Item.Settings settings) {
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, name), new BlockItem(block, settings));
        return Registry.register(Registry.BLOCK, new Identifier(MOD_ID, name), block);
    }

    private static Block registerNoItem(String name, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(MOD_ID, name), block);
    }

    private static Block registerFluidBlock(String name, FlowableFluid item, AbstractBlock.Settings base) {
        return Registry.register(Registry.BLOCK, new Identifier(MOD_ID, name), new FluidBlock(item, base) {
        });
    }

    @Environment(EnvType.CLIENT)
    public static void initTransparency() {
        REGISTRY_TRANS.add(CLOUDBERRY_AMALGAM_BLOCK);
    }

    @Environment(EnvType.CLIENT)
    public static void initPartialblocks() {
        REGISTRY_PARTIAL.add(CLOUD_BERRY_CROP);
        REGISTRY_PARTIAL.add(CLOUD_BERRY_WILD);
        REGISTRY_PARTIAL.add(CINDERMOTE_CROP);
        REGISTRY_PARTIAL.add(CINDERMOTE_WILD);
        REGISTRY_PARTIAL.add(VOMPOLLOLOWM_CROP_BASE);
        REGISTRY_PARTIAL.add(VOMPOLLOLOWM_CROP_STALK);
        REGISTRY_PARTIAL.add(VOMPOLLOLOWM_WILD_BASE);
        REGISTRY_PARTIAL.add(VOMPOLLOLOWM_WILD_STALK);
        REGISTRY_PARTIAL.add(JELLY_PEAR_WILD);
        REGISTRY_PARTIAL.add(JELLY_PEAR_CROP);
    }
}
