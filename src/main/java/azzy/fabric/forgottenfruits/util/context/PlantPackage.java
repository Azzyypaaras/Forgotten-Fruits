package azzy.fabric.forgottenfruits.util.context;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class PlantPackage {

    public final BlockState state;
    public final World world;
    public final BlockPos pos;
    public final Entity entity;
    public final WorldView view;

    public PlantPackage(@Nullable BlockState state, @Nullable World world, @Nullable BlockPos pos, @Nullable Entity entity){
        this.state = state;
        this.world = world;
        this.pos = pos;
        this.entity = entity;
        this.view = null;
    }

    public PlantPackage(@Nullable BlockState state, @Nullable WorldView world, @Nullable BlockPos pos, @Nullable Entity entity){
        this.state = state;
        this.view = world;
        this.pos = pos;
        this.entity = entity;
        this.world = null;
    }

    public enum Context {
        COLLISION,
        PARTICLE,
        GROWTH,
        RAIN,
        DISPlAY,
        SCHEDULED,
        BROKEN,
        FERTILIZATION
    }
}
