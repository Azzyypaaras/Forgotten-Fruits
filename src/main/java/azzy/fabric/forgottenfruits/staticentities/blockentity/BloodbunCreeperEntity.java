package azzy.fabric.forgottenfruits.staticentities.blockentity;

import azzy.fabric.forgottenfruits.registry.CropRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

import static azzy.fabric.forgottenfruits.ForgottenFruits.FFLog;
import static azzy.fabric.forgottenfruits.registry.BlockEntityRegistry.BLOODBUN_CREEPER_ENTITY;

public class BloodbunCreeperEntity extends BlockEntity {

    private BlockPos parent;
    private boolean locked;
    private int maxStage = 2;

    public BloodbunCreeperEntity() {
        super(BLOODBUN_CREEPER_ENTITY);
    }

    public BlockPos getParent() {
        return parent;
    }

    public boolean checkParent() {
        return world.getBlockState(parent).getBlock() == CropRegistry.BLOODBUN_CROP;
    }

    public void setParent(BlockPos parent) {
        this.parent = parent;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        if(parent != null)
            tag.putLong("parent", parent.asLong());
        tag.putBoolean("locked", locked);
        tag.putInt("max", maxStage);
        return super.toTag(tag);
    }

    public boolean isLocked() {
        return locked;
    }

    public void setRandomMaxAge(Random random){
        maxStage = random.nextFloat() < 0.375f ? random.nextInt(2) + 1 : 0;
        locked = true;
    }

    public int getMaxStage() {
        return maxStage;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        parent = BlockPos.fromLong(tag.getLong("parent"));
        locked = tag.getBoolean("locked");
        maxStage = tag.getInt("max");
    }
}
