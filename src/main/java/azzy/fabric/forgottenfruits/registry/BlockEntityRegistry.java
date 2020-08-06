package azzy.fabric.forgottenfruits.registry;

import azzy.fabric.forgottenfruits.staticentities.blockentity.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static azzy.fabric.forgottenfruits.ForgottenFruits.MOD_ID;

public class BlockEntityRegistry {

    public final static BlockEntityType<PressEntity> PRESS = BlockEntityType.Builder.create(PressEntity::new, BlockRegistry.PRESS).build(null);
    public final static BlockEntityType<BasketEntity> BASKET = BlockEntityType.Builder.create(BasketEntity::new, BlockRegistry.BASKET).build(null);
    final public static BlockEntityType<WoodPipeEntity> WOOD_PIPE = BlockEntityType.Builder.create(WoodPipeEntity::new, BlockRegistry.WOOD_PIPE).build(null);
    public final static BlockEntityType<BarrelEntity> BARREL = BlockEntityType.Builder.create(BarrelEntity::new, BlockRegistry.BARREL).build(null);
    public final static BlockEntityType<WitchCauldronEntity> WITCH_CAULDRON = BlockEntityType.Builder.create(WitchCauldronEntity::new, BlockRegistry.WITCH_CAULDRON).build(null);
    static public final BlockEntityType<BloodbunEntity> BLOODBUN_HEART_ENTITY = BlockEntityType.Builder.create(BloodbunEntity::new, CropRegistry.BLOODBUN_CROP).build(null);
    public final static BlockEntityType<BloodbunCreeperEntity> BLOODBUN_CREEPER_ENTITY = BlockEntityType.Builder.create(BloodbunCreeperEntity::new, CropRegistry.BLOODBUN_CREEPER).build(null);

    public static void register(BlockEntityType<? extends BlockEntity> blockEntityType, String name) {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MOD_ID, name), blockEntityType);
    }

    public static void init() {
        register(PRESS, "press");
        register(BASKET, "basket");
        register(WOOD_PIPE, "woodpipe");
        register(BARREL, "barrel");
        register(WITCH_CAULDRON, "witch_cauldron");
        register(BLOODBUN_HEART_ENTITY, "bloodbun_heart");
        register(BLOODBUN_CREEPER_ENTITY, "bloodbun_creeper");
    }
}
