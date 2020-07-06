package azzy.fabric.forgottenfruits.registry;

import azzy.fabric.forgottenfruits.block.CindermoteBlock;
import azzy.fabric.forgottenfruits.block.PlantBase;
import azzy.fabric.forgottenfruits.block.WildPlantBase;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static azzy.fabric.forgottenfruits.ForgottenFruits.MOD_ID;
import static azzy.fabric.forgottenfruits.registry.ItemRegistry.CINDERMOTE_SEEDS;
import static azzy.fabric.forgottenfruits.registry.ItemRegistry.CLOUDBERRY_SEEDS;

public class CropRegistry {

    public static final Block CLOUDBERRY_CROP = register("cloudberry_crop", new PlantBase(6, Material.PLANT, BlockSoundGroup.CROP, CLOUDBERRY_SEEDS, 8, 16, null, 0, 0, 0, 0));
    public static final Block CLOUDBERRY_WILD = register("cloudberry_wild", new WildPlantBase("cloudberry", Material.PLANT, BlockSoundGroup.CROP, null, 0, 0, 0, 0));

    public static final Block CINDERMOTE_CROP = register("cindermote_crop", new CindermoteBlock(7, Material.SOLID_ORGANIC, BlockSoundGroup.NETHER_WART, CINDERMOTE_SEEDS, 0, 16, ParticleTypes.FLAME, 20, 30, 0.05f, 8));
    public static final Block CINDERMOTE_WILD = register("cindermote_wild", new WildPlantBase("cindermote", Material.SOLID_ORGANIC, BlockSoundGroup.NETHER_WART, ParticleTypes.FLAME, 10, 20, 0.05f, 30));

    private static Block register(String name, Block item) {
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, name), item);
        return item;
    }

    public static void init() {
    }
}
