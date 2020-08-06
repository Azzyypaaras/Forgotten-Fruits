package azzy.fabric.forgottenfruits.staticentities.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;

import java.util.LinkedList;
import java.util.List;

import static azzy.fabric.forgottenfruits.registry.BlockEntityRegistry.BLOODBUN_HEART_ENTITY;

public class BloodbunEntity extends BlockEntity {

    private long children;

    public BloodbunEntity() {
        super(BLOODBUN_HEART_ENTITY);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putLong("lolis", children);
        return super.toTag(tag);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        children = tag.getLong("lolis");
    }

    public long getChildren() {
        return children;
    }

    public void addChild(){
        children++;
    }

    public void deleteChild(){
        if(children > 0)
            children--;
    }
}
