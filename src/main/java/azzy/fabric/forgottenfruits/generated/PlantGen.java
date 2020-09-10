package azzy.fabric.forgottenfruits.generated;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class PlantGen extends Feature<DefaultFeatureConfig> {

    private final int spread;
    private final int steepness;
    private final Block plant, tile;

    public PlantGen(Codec<DefaultFeatureConfig> codec, int spread, int steepness, Block plant, Block tile) {
        super(codec);
        this.spread = spread;
        this.plant = plant;
        this.steepness = steepness;
        this.tile = tile;
    }
    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig featureConfig) {
        BlockPos topPos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, blockPos);
        double multiplier;
        for (int j = steepness; j > 0; j--) {
            BlockPos down = topPos.down();
            if (world.getBlockState(down).getBlock() == tile) {
                for (int i = 1; i < 6; i++) {
                    multiplier = (Math.random() - 0.5) * 2;
                    world.setBlockState(topPos.add(i + (spread * multiplier), 0D, i + (spread * multiplier)), plant.getDefaultState(), 1);
                }
                return true;
            } else
                topPos = down;
        }
        return false;
    }
}
