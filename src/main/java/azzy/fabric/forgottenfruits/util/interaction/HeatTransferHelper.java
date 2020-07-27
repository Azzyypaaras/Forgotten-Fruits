package azzy.fabric.forgottenfruits.util.interaction;

import azzy.fabric.forgottenfruits.registry.BlockRegistry;
import azzy.fabric.forgottenfruits.registry.CropRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;

import java.util.HashMap;

public class HeatTransferHelper {

    //HEY, REMEMBER TO CHECK THE HEAT TRANSFER FORMULA TO PREVENT EXCESSIVE CALCULATIONS

    private static final HashMap<Block, HeatSource> heatMap = new HashMap<>();

    public static void init() {
        for (int i = 0; i < HeatSource.values().length; i++)
            heatMap.put(HeatSource.values()[i].block, HeatSource.values()[i]);
    }

    private static double calculateHeat(HeatMaterial medium, double bodyATemp, HeatSource bodyB) {
        double transfer;
        transfer = medium.transfer * Math.pow(bodyB.size, 2) * (bodyATemp - bodyB.temp);
        return transfer;
    }

    private static double calculateHeat(HeatMaterial medium, double bodyATemp, double bodyBTemp) {
        double transfer;
        transfer = medium.transfer * 0.75 * (bodyATemp - bodyBTemp);
        return transfer;
    }

    public static <T extends HeatHolder> void simulateHeat(HeatMaterial medium, T bodyA, T bodyB) {
        double flux = calculateHeat(medium, bodyA.getHeat(), bodyB.getHeat());
        bodyA.moveHeat(-flux);
        bodyB.moveHeat(flux);
    }

    public static <T extends HeatHolder> void simulateHeat(HeatMaterial medium, T bodyA, Block bodyB) {
        HeatSource source = heatMap.get(bodyB);
        double flux = calculateHeat(medium, bodyA.getHeat(), source);
        bodyA.moveHeat(-flux);
    }

    public static boolean isHeatSource(Block body) {
        return heatMap.containsKey(body);
    }

    public static double translateBiomeHeat(Biome biome) {
        if (biome.getCategory() == Biome.Category.NETHER) {
            return (biome.getTemperature() + 2) * 31.25;
        } else if (biome.getCategory() == Biome.Category.THEEND) {
            return (biome.getTemperature() - 1.5) * 31.25;
        } else {
            return biome.getTemperature() * 31.25;
        }
    }

    public static <T extends HeatHolder> void simulateAmbientHeat(T bodyA, Biome biome) {
        double flux = calculateHeat(HeatMaterial.AIR, bodyA.getHeat(), translateBiomeHeat(biome));
        bodyA.moveHeat(-flux);
    }

    public enum HeatMaterial {
        AIR(0.026),
        IRON(52.0);

        private final double transfer;

        HeatMaterial(double transfer) {
            this.transfer = transfer;
        }
    }

    public enum HeatSource {
        FIRE(800, Blocks.FIRE, 0.7),
        MAGMA(500, Blocks.MAGMA_BLOCK, 1),
        LAVA(1200, Blocks.LAVA, 1),
        CINDER(400, BlockRegistry.CINDERMOTE_JUICE, 1),
        GLOWSTONE(200, Blocks.GLOWSTONE, 1),
        TORCH(700, Blocks.TORCH, 0.125),
        WALLTORCH(700, Blocks.WALL_TORCH, 0.125),
        CINDERMOTE(1400, CropRegistry.CINDERMOTE_CROP, 0.02),
        CINDERMOTEWILD(1400, CropRegistry.CINDERMOTE_WILD, 0.02),
        SOULFIRE(900, Blocks.SOUL_FIRE, 0.7),
        CAMPFIRE(750, Blocks.CAMPFIRE, 0.4),
        SOULCAMPFIRE(800, Blocks.SOUL_CAMPFIRE, 0.4),
        SOULTORCH(750, Blocks.SOUL_TORCH, 0.125),
        WALLSOULTORCH(750, Blocks.SOUL_WALL_TORCH, 0.125),
        LONELYFIRE(2000, null, 0.7),
        HEATER(1200, null, 2.0),
        LONELYHEATER(3000, null, 2.0);

        private final int temp;
        private final Block block;
        private final double size;

        HeatSource(int temp, Block block, double size) {
            this.temp = temp;
            this.block = block;
            this.size = size;
        }

        public HeatSource getSource(Block block) {
            if (heatMap.containsKey(block))
                return heatMap.get(block);
            return null;
        }
    }
}
