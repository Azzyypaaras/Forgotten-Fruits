package azzy.fabric.forgottenfruits.block.Plant;

import azzy.fabric.forgottenfruits.block.PlantBase;
import azzy.fabric.forgottenfruits.staticentities.blockentity.BloodbunCreeperEntity;
import azzy.fabric.forgottenfruits.staticentities.blockentity.BloodbunEntity;
import azzy.fabric.forgottenfruits.util.context.ContextConsumer;
import azzy.fabric.forgottenfruits.util.interaction.PlantType;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.HashMap;
import java.util.Random;

import static azzy.fabric.forgottenfruits.registry.CropRegistry.BLOODBUN_CREEPER;
import static azzy.fabric.forgottenfruits.registry.ItemRegistry.BLOODBUN_FRUIT;

public class BloodbunCreeper extends PlantBase implements BlockEntityProvider {

    private static final HashMap<Direction, BooleanProperty> PROPERTY_MAP = new HashMap<>();
    private static final BooleanProperty HANGERS = BooleanProperty.of("hangers");
    private static final BooleanProperty LONG = BooleanProperty.of("long");
    private static final BooleanProperty LOCKED = BooleanProperty.of("locked");

    public BloodbunCreeper(PlantType.PLANTTYPE type, int stages, Material material, BlockSoundGroup sound, ItemConvertible seeds, int minLight, int maxLight, ContextConsumer... consumers) {
        super(FabricBlockSettings.of(material).sounds(sound).breakInstantly().ticksRandomly().noCollision().lightLevel(e -> e.get(AGE) >= 2 ? 6 : 0), type, stages, seeds, minLight, maxLight, true, consumers);
        setDefaultState(getDefaultState().with(PROPERTY_MAP.get(Direction.DOWN), true).with(PROPERTY_MAP.get(Direction.UP), false).with(PROPERTY_MAP.get(Direction.NORTH), false).with(PROPERTY_MAP.get(Direction.EAST), false).with(PROPERTY_MAP.get(Direction.SOUTH), false).with(PROPERTY_MAP.get(Direction.WEST), false).with(LOCKED, false).with(HANGERS, false).with(LONG, false));
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return canPlantOn(floor);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        for(Direction direction : Direction.values()){
            PROPERTY_MAP.put(direction, BooleanProperty.of(direction.getName()));
        }
        for(Direction direction : Direction.values()){
            builder.add(PROPERTY_MAP.get(direction));
        }
        builder.add(HANGERS).add(LONG).add(LOCKED);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    public static boolean canPlantOn(BlockState floor) {
        Block block = floor.getBlock();
        return !(BlockTags.FIRE.contains(block) || BlockTags.SAND.contains(block) || BlockTags.NYLIUM.contains(block) || block == Blocks.NETHERRACK || block == Blocks.END_STONE) && floor.getFluidState().isEmpty();
    }

    public static boolean canPlantOn(BlockState floor, BlockView world, BlockPos pos, Direction direction) {
        Block block = floor.getBlock();
        return !(BlockTags.FIRE.contains(block) || BlockTags.SAND.contains(block) || BlockTags.NYLIUM.contains(block) || block == Blocks.NETHERRACK || block == Blocks.END_STONE || floor.isAir()) && (floor.isSideSolidFullSquare(world, pos, direction.getOpposite()) || (BlockTags.LEAVES.contains(block) && direction == Direction.UP) && floor.getFluidState().isEmpty());
    }


    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new BloodbunCreeperEntity();
    }

    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity.getType() != EntityType.BEE) {
            entity.slowMovement(state, new Vec3d(0.65D, 0.75D, 0.65D));
        }
    }

    public static boolean calcState(World world, BlockPos pos){
        boolean valid = false;
        BlockState blockState = world.getBlockState(pos);
        if(!blockState.isOf(BLOODBUN_CREEPER))
            return true;
        for(Direction direction : Direction.values()){
            if(canPlantOn(world.getBlockState(pos.offset(direction)), world, pos.offset(direction), direction)){
                blockState = blockState.with(PROPERTY_MAP.get(direction), true);
                valid = true;
            }
            else{
                blockState = blockState.with(PROPERTY_MAP.get(direction), false);
            }
        }
        if(!blockState.get(LOCKED)){
            Random random = world.getRandom();
            boolean hangers = random.nextBoolean();
            blockState = blockState.with(HANGERS, hangers);
            if(hangers){
                blockState = blockState.with(LONG, random.nextBoolean());
            }
            blockState = blockState.with(LOCKED, true);
        }
        if(!valid){
            BloodbunEntity parent = (BloodbunEntity) world.getBlockEntity(((BloodbunCreeperEntity) world.getBlockEntity(pos)).getParent());
            if(parent != null){
                parent.deleteChild();
            }
            world.breakBlock(pos, false);
        }
        else{
            world.setBlockState(pos, blockState);
        }
        return !valid;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(this.isMature(state)){
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(BLOODBUN_FRUIT, world.getRandom().nextInt(4) + 1)));
            world.setBlockState(pos, state.with(AGE, 0));
        }
        return ActionResult.CONSUME;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ctx) {
        return VoxelShapes.fullCube();
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(calcState(world, pos))
            return;
        BloodbunCreeperEntity creeper = (BloodbunCreeperEntity) world.getBlockEntity(pos);
        if(creeper == null)
            world.breakBlock(pos, false);
        BloodbunEntity parent = (BloodbunEntity) world.getBlockEntity((creeper).getParent());
        if(parent != null) {
            if(state.get(AGE) < creeper.getMaxStage())
                super.randomTick(state, world, pos, random);
            if(random.nextInt((int) Math.min(Math.max(((Math.pow(parent.getChildren(), 1.5) / 100)), 1), 75)) == 0)
                creep(state, pos, world, creeper);
        }
        else
            world.breakBlock(pos, false);
    }

    public static void creep(BlockState state, BlockPos pos, ServerWorld world, BloodbunCreeperEntity parentCreeper) {
        Direction[] directions = Direction.values();
        for(Direction dir : directions){
            boolean success = false;
            BlockPos potentialChild = pos.offset(dir);
            if(world.getBlockState(potentialChild).getMaterial().isReplaceable() && world.getBlockState(potentialChild).getFluidState().isEmpty())
                for(Direction direction : directions){
                    BlockPos floorPos = potentialChild.offset(direction);
                    if(state.get(PROPERTY_MAP.get(direction)) && canPlantOn(world.getBlockState(floorPos), world, floorPos, direction)){
                        success = true;
                        world.setBlockState(potentialChild, BLOODBUN_CREEPER.getDefaultState());
                        BlockEntity childCreeper = world.getBlockEntity(potentialChild);
                        if(childCreeper instanceof BloodbunCreeperEntity) {
                            ((BloodbunCreeperEntity) childCreeper).setParent(parentCreeper.getParent());
                            BlockEntity parent = world.getBlockEntity(parentCreeper.getParent());
                            ((BloodbunCreeperEntity) childCreeper).setRandomMaxAge(world.getRandom());
                            if (parent instanceof BloodbunEntity) {
                                ((BloodbunEntity) parent).addChild();
                            }
                            calcState(world, potentialChild);
                        }
                    }
                }
            if(!success){
                for(Direction push : directions){
                    BlockPos cornerChild = potentialChild.offset(push);
                    if(world.getBlockState(cornerChild).getMaterial().isReplaceable() && world.getBlockState(cornerChild).getFluidState().isEmpty())
                        for(Direction direction : directions){
                            BlockPos floorPos = cornerChild.offset(direction);
                            if(canPlantOn(world.getBlockState(floorPos), world, floorPos, direction)){
                                world.setBlockState(cornerChild, BLOODBUN_CREEPER.getDefaultState());
                                BlockEntity childCreeper = world.getBlockEntity(cornerChild);
                                if(childCreeper instanceof BloodbunCreeperEntity) {
                                    ((BloodbunCreeperEntity) childCreeper).setParent(parentCreeper.getParent());
                                    BlockEntity parent = world.getBlockEntity(parentCreeper.getParent());
                                    ((BloodbunCreeperEntity) childCreeper).setRandomMaxAge(world.getRandom());
                                    if (parent instanceof BloodbunEntity) {
                                        ((BloodbunEntity) parent).addChild();
                                    }
                                    calcState(world, cornerChild);
                                }
                            }
                        }
                }
            }
        }
    }
}
