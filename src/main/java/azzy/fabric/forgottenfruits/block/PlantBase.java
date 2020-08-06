package azzy.fabric.forgottenfruits.block;

import azzy.fabric.forgottenfruits.util.interaction.PlantType.*;
import azzy.fabric.forgottenfruits.util.context.ContextConsumer;
import azzy.fabric.forgottenfruits.util.context.ContextMap;
import azzy.fabric.forgottenfruits.util.context.PlantPackage;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class PlantBase extends CropBlock implements FluidFillable {
    private final int maxAge;
    private final int minLight;
    private final int maxLight;
    private final ItemConvertible seeds;
    private static final VoxelShape DEFAULT = VoxelShapes.cuboid(0.2f, 0f, 0.2f, 0.8f, 0.8f, 0.8f);;
    private final VoxelShape shape;
    private final PLANTTYPE type;
    private final ContextMap<PlantPackage, PlantPackage.Context> contextConsumers;
    private final boolean solid;
    private final FluidState fluid;

    public PlantBase(PLANTTYPE type, int stages, Material material, BlockSoundGroup sound, ItemConvertible seeds, int minLight, int maxLight, ContextConsumer ... consumers) {
        super(FabricBlockSettings.of(material).sounds(sound).breakInstantly().ticksRandomly().noCollision());
        maxAge = stages - 1;
        this.setDefaultState((this.getStateManager().getDefaultState()).with(this.getAgeProperty(), 0));
        this.minLight = minLight;
        this.maxLight = maxLight;
        this.seeds = seeds;
        this.shape = DEFAULT;
        this.type = type;
        this.contextConsumers = ContextMap.construct(consumers);
        this.solid = false;
        this.fluid = Fluids.EMPTY.getDefaultState();
    }

    public PlantBase(FabricBlockSettings settings, PLANTTYPE type, int stages, Material material, BlockSoundGroup sound, ItemConvertible seeds, int minLight, int maxLight, ContextConsumer ... consumers) {
        super(settings);
        maxAge = stages - 1;
        this.setDefaultState((this.getStateManager().getDefaultState()).with(this.getAgeProperty(), 0));
        this.minLight = minLight;
        this.maxLight = maxLight;
        this.seeds = seeds;
        this.shape = DEFAULT;
        this.type = type;
        this.contextConsumers = ContextMap.construct(consumers);
        this.solid = false;
        this.fluid = Fluids.EMPTY.getDefaultState();
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(this.getSeedsItem());
    }

    public PlantBase(PLANTTYPE type, int stages, Material material, BlockSoundGroup sound, ItemConvertible seeds, int minLight, int maxLight, VoxelShape shape, boolean solid, ContextConsumer ... consumers) {
        super(FabricBlockSettings.of(material).sounds(sound).breakInstantly().ticksRandomly().noCollision());
        maxAge = stages - 1;
        this.setDefaultState((this.getStateManager().getDefaultState()).with(this.getAgeProperty(), 0));
        this.minLight = minLight;
        this.maxLight = maxLight;
        this.seeds = seeds;
        if(shape != null)
            this.shape = shape;
        else
            this.shape = DEFAULT;
        this.type = type;
        this.contextConsumers = ContextMap.construct(consumers);
        this.solid = solid;
        this.fluid = Fluids.EMPTY.getDefaultState();
    }

    public PlantBase(PLANTTYPE type, int stages, Material material, BlockSoundGroup sound, ItemConvertible seeds, int minLight, int maxLight, VoxelShape shape, boolean solid, FluidState fluid, ContextConsumer ... consumers) {
        super(FabricBlockSettings.of(material).sounds(sound).breakInstantly().ticksRandomly().noCollision());
        maxAge = stages - 1;
        this.setDefaultState((this.getStateManager().getDefaultState()).with(this.getAgeProperty(), 0));
        this.minLight = minLight;
        this.maxLight = maxLight;
        this.seeds = seeds;
        if(shape != null)
            this.shape = shape;
        else
            this.shape = DEFAULT;
        this.type = type;
        this.contextConsumers = ContextMap.construct(consumers);
        this.solid = solid;
        this.fluid = fluid;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        if(fluid == Fluids.EMPTY.getDefaultState())
            return super.getPlacementState(ctx);
        else{
            FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
            return fluidState.isIn(FluidTags.WATER) && fluidState.getLevel() == 8 ? super.getPlacementState(ctx) : null;
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        if(fluid != Fluids.EMPTY.getDefaultState()){
            BlockState blockState = super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
            if (!blockState.isAir()) {
                world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
            }
            return blockState;
        }
        return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }

    @Override
    public int getMaxAge() {
        return maxAge;
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ctx) {
        return shape;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return solid ? shape : VoxelShapes.empty();
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        contextConsumers.execute(new PlantPackage(state, world, pos, null), PlantPackage.Context.SCHEDULED);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        contextConsumers.execute(new PlantPackage(state, world, pos, player), PlantPackage.Context.BROKEN);
        super.onBreak(world, pos, state, player);
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return seeds;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        contextConsumers.executeWithFallback(new PlantPackage(state, world, pos, entity), PlantPackage.Context.COLLISION, this::standardCollision);
    }

    @Override
    public void rainTick(World world, BlockPos pos) {
        contextConsumers.execute(new PlantPackage(null, world, pos, null), PlantPackage.Context.RAIN);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        contextConsumers.execute(new PlantPackage(state, world, pos, null), PlantPackage.Context.DISPlAY);
    }

    private void standardCollision(PlantPackage plantPackage){
        World world = plantPackage.world;
        Entity entity = plantPackage.entity;
        BlockPos pos = plantPackage.pos;
        if(world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) && (entity instanceof RavagerEntity))
            world.breakBlock(pos, true, entity);
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return type.contains(floor.getBlock());
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int currentLight = world.getBaseLightLevel(pos, 0);
        if (currentLight >= minLight && currentLight <= maxLight) {
                contextConsumers.execute(new PlantPackage(state, world, pos, null), PlantPackage.Context.PARTICLE);
                contextConsumers.executeWithFallback(new PlantPackage(state, world, pos, null), PlantPackage.Context.GROWTH, this::fallbackTick);
        }
    }

    private void fallbackTick(PlantPackage plantPackage){
        BlockState state = plantPackage.state;
        World world = plantPackage.world;
        BlockPos pos = plantPackage.pos;
        int i = this.getAge(state);
        if (i < this.getMaxAge()) {
            float f = getAvailableMoisture(this, world, pos);
            if (world.random.nextInt((int) (25.0F / f) + 1) == 0) {
                world.setBlockState(pos, this.withAge(i + 1), 2);
            }
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return fluid;
    }

    @Override
    public boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
        return false;
    }
}
