package azzy.fabric.forgottenfruits.registry;

import azzy.fabric.forgottenfruits.block.fluid.JuiceCinder;
import azzy.fabric.forgottenfruits.block.fluid.JuiceCloudberry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

import static azzy.fabric.forgottenfruits.ForgottenFruits.MOD_ID;

public class FluidRegistry {

    public static final List<FluidPair> JUICE_RENDER = new ArrayList<>();
    @Environment(EnvType.CLIENT)
    public static final List<Fluid> FLUID_TRANS = new ArrayList<>();
    //Cloudberry
    public static FlowableFluid CLOUDBERRY = registerStill("cloudberry", new JuiceCloudberry.Still());
    public static FlowableFluid CLOUDBERRY_FLOWING = registerFlowing("cloudberry_flowing", new JuiceCloudberry.Flowing());
    private static final FluidPair cloudberryJuice = new FluidPair(CLOUDBERRY, CLOUDBERRY_FLOWING, 0xee9b2f);
    //Cinder
    public static FlowableFluid CINDERMOTE = registerStill("cindermote", new JuiceCinder.Still());
    public static FlowableFluid CINDERMOTE_FLOWING = registerFlowing("cindermote_flowing", new JuiceCinder.Flowing());
    private static final FluidPair cindermoteJuice = new FluidPair(CINDERMOTE, CINDERMOTE_FLOWING, 0xe37b00);

    @Environment(EnvType.CLIENT)
    public static void initTransparency() {
        FLUID_TRANS.add(CLOUDBERRY);
        FLUID_TRANS.add(CLOUDBERRY_FLOWING);
        FLUID_TRANS.add(CINDERMOTE);
        FLUID_TRANS.add(CINDERMOTE_FLOWING);
    }

    public static FlowableFluid registerStill(String name, FlowableFluid item) {
        Registry.register(Registry.FLUID, new Identifier(MOD_ID, name), item);
        return item;
    }

    public static FlowableFluid registerFlowing(String name, FlowableFluid item) {
        Registry.register(Registry.FLUID, new Identifier(MOD_ID, name), item);
        return item;
    }

    public static void init() {
        JUICE_RENDER.add(cloudberryJuice);
        JUICE_RENDER.add(cindermoteJuice);
    }

    public static class FluidPair {

        private final FlowableFluid stillState;
        private final FlowableFluid flowState;
        private final int color;

        private FluidPair(final FlowableFluid stillState, final FlowableFluid flowState, final int color) {
            this.stillState = stillState;
            this.flowState = flowState;
            this.color = color;
        }

        public FlowableFluid getStillState() {
            return stillState;
        }

        public FlowableFluid getFlowState() {
            return flowState;
        }

        public int getColor() {
            return color;
        }
    }
}
