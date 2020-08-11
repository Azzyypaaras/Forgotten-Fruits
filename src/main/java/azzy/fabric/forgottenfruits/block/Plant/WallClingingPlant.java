package azzy.fabric.forgottenfruits.block.Plant;

import azzy.fabric.forgottenfruits.block.PlantBase;
import azzy.fabric.forgottenfruits.util.context.ContextConsumer;
import azzy.fabric.forgottenfruits.util.context.PlantPackage;
import azzy.fabric.forgottenfruits.util.interaction.PlantType;
import net.minecraft.block.*;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class WallClingingPlant extends PlantBase {

    public static final DirectionProperty FACING;
    private final PlantType.PLANTTYPE floor;
    private final boolean allSided;
    private final VoxelShape SHAPE_UP;
    private final VoxelShape SHAPE_DOWN;
    private final VoxelShape SHAPE_NORTH;
    private final VoxelShape SHAPE_EAST;
    private final VoxelShape SHAPE_SOUTH;
    private final VoxelShape SHAPE_WEST;

    public WallClingingPlant(PlantType.PLANTTYPE type, int stages, Material material, BlockSoundGroup sound, ItemConvertible seeds, int minLight, int maxLight, boolean allSided, int width, boolean fertilizable, ContextConsumer<?, ?>... consumers) {
        super(PlantType.PLANTTYPE.NULL, stages, material, sound, seeds, minLight, maxLight, fertilizable, consumers);
        this.floor = type;
        this.allSided = allSided;
        SHAPE_UP = Block.createCuboidShape(0, 16 - width, 0, 16, 16, 16);
        SHAPE_DOWN = Block.createCuboidShape(0, 0, 0, 16, width, 16);
        SHAPE_WEST = Block.createCuboidShape(16 - width, 0, 0, 16, 16, 16);
        SHAPE_SOUTH = Block.createCuboidShape(0, 0, 0, 16, 16, width);
        SHAPE_EAST = Block.createCuboidShape(0, 0, 0, width, 16, 16);
        SHAPE_NORTH = Block.createCuboidShape(0, 0, 16 - width, 16, 16, 16);
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        Direction direction = allSided ? ctx.getSide() : ctx.getPlayerFacing();
        return floor.contains(ctx.getWorld().getBlockState(pos.offset(direction)).getBlock()) ? super.getPlacementState(ctx).with(FACING, direction.getOpposite()) : Blocks.AIR.getDefaultState();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ctx) {
        return getDirectionalShape(state.get(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return solid ? getDirectionalShape(state.get(FACING)) : VoxelShapes.empty();
    }

    protected VoxelShape getDirectionalShape(Direction direction){
        switch (direction){
            case UP: return SHAPE_UP;
            case DOWN: return SHAPE_DOWN;
            case EAST: return SHAPE_EAST;
            case SOUTH: return SHAPE_SOUTH;
            case WEST: return SHAPE_WEST;
            default: return SHAPE_NORTH;
        }
    }

    private boolean canPlantOn(BlockPos pos, World world, Direction direction){
        return floor.contains(world.getBlockState(pos.offset(direction.getOpposite())).getBlock());
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(canPlantOn(pos, world, state.get(FACING)))
            super.randomTick(state, world, pos, random);
        else
            world.breakBlock(pos, true);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(!canPlantOn(pos, world, state.get(FACING))){
            world.breakBlock(pos, true);
            return;
        }
        super.scheduledTick(state, world, pos, random);
    }

    @Override
    protected void fallbackGrowth(PlantPackage plantPackage) {
        BlockState state = plantPackage.state;
        World world = plantPackage.world;
        BlockPos pos = plantPackage.pos;
        int i = this.getAge(state);
        if (!this.isMature(state))
            world.setBlockState(pos, this.withAge(i + 1).with(FACING, state.get(FACING)), 3);
    }

    @Override
    protected void fallbackTick(PlantPackage plantPackage) {
        BlockState state = plantPackage.state;
        World world = plantPackage.world;
        BlockPos pos = plantPackage.pos;
        int i = this.getAge(state);
        if (i < this.getMaxAge()) {
            float f = getAvailableMoisture(this, world, pos);
            if (world.random.nextInt((int) (25.0F / f) + 1) == 0) {
                world.setBlockState(pos, this.withAge(i + 1).with(FACING, state.get(FACING)), 3);
            }
        }
    }

    static {
        FACING = Properties.FACING;
    }
}
