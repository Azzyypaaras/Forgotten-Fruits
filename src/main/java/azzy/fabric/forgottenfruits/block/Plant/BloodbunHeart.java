package azzy.fabric.forgottenfruits.block.Plant;

import azzy.fabric.forgottenfruits.block.PlantBase;
import azzy.fabric.forgottenfruits.staticentities.blockentity.BloodbunCreeperEntity;
import azzy.fabric.forgottenfruits.staticentities.blockentity.BloodbunEntity;
import azzy.fabric.forgottenfruits.util.context.ContextConsumer;
import azzy.fabric.forgottenfruits.util.interaction.PlantType;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

import static azzy.fabric.forgottenfruits.ForgottenFruits.FFLog;
import static azzy.fabric.forgottenfruits.registry.CropRegistry.BLOODBUN_CREEPER;

public class BloodbunHeart extends PlantBase implements BlockEntityProvider{

    private static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 5, 16);

    public BloodbunHeart(PlantType.PLANTTYPE type, int stages, Material material, BlockSoundGroup sound, ItemConvertible seeds, int minLight, int maxLight, ContextConsumer... consumers) {
        super(type, stages, material, sound, seeds, minLight, maxLight, true, consumers);
    }

    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity.getType() != EntityType.BEE) {
            entity.slowMovement(state, new Vec3d(0.65D, 0.75D, 0.65D));
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ctx) {
        return SHAPE;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(!this.isMature(state))
            super.randomTick(state, world, pos, random);
        else {
            for(Direction direction : Direction.values()){
                if(direction == Direction.UP)
                    continue;
                BlockPos childPos = pos.offset(direction);
                if(BloodbunCreeper.canPlantOn(world.getBlockState(childPos.down())) && world.getBlockState(childPos).isAir() && world.getRandom().nextInt(3) == 0) {
                    world.setBlockState(childPos, BLOODBUN_CREEPER.getDefaultState());
                    ((BloodbunCreeperEntity) world.getBlockEntity(childPos)).setParent(pos);
                    ((BloodbunEntity) world.getBlockEntity(pos)).addChild();
                }
            }

        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new BloodbunEntity();
    }
}
