package azzy.fabric.forgottenfruits.generated;

import azzy.fabric.forgottenfruits.registry.CropRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

import static net.minecraft.block.LeavesBlock.PERSISTENT;

public class VompollolowmFeature extends Feature<DefaultFeatureConfig> {

    public VompollolowmFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig featureConfig) {
        BlockPos topPos = pos;
        for (BlockPos blockPos : BlockPos.iterate(topPos.add(24, 64, 24), topPos.add(-24, -16, -24))) {
            topPos = blockPos;
            if ((world.getBlockState(topPos.down()) == Blocks.STONE.getDefaultState() || (world.getBlockState(topPos.down(2)) == Blocks.STONE.getDefaultState()) && world.getBlockState(topPos.down()) == Blocks.SNOW.getDefaultState()) && world.isAir(topPos) && random.nextInt(3) == 0)
                if (random.nextInt(65) == 0)
                    generateStalk(world, topPos, random);
                else if (random.nextInt(10) == 0) {
                    world.setBlockState(topPos, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 3);
                    if(random.nextInt(15) == 0)
                        world.setBlockState(topPos.up(), Blocks.MOSSY_COBBLESTONE_SLAB.getDefaultState(), 3);
                }
                else if (random.nextInt(18) == 0)
                    world.setBlockState(topPos, Blocks.MOSSY_COBBLESTONE_SLAB.getDefaultState(), 3);
        }
        return true;
    }

    private void generateStalk(ServerWorldAccess world, BlockPos pos, Random random){
        world.setBlockState(pos, CropRegistry.VOMPOLLOLOWM_WILD_BASE.getDefaultState(), 3);
        while(random.nextInt(15) != 0){
            pos = pos.up();
            world.setBlockState(pos, CropRegistry.VOMPOLLOLOWM_WILD_STALK.getDefaultState(), 3);
        }
        world.setBlockState(pos.up(), CropRegistry.VOMPOLLOLOWM_WILD_FRUIT.getDefaultState(), 3);
    }
}
