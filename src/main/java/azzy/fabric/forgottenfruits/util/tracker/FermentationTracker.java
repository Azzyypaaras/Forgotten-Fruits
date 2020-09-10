package azzy.fabric.forgottenfruits.util.tracker;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import azzy.fabric.forgottenfruits.staticentities.blockentity.BarrelEntity;
import azzy.fabric.forgottenfruits.util.recipe.templates.FFFermentingOutput;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import static azzy.fabric.forgottenfruits.ForgottenFruits.FFLog;
import static azzy.fabric.forgottenfruits.ForgottenFruits.config;

public class FermentationTracker {

    private double quality, purity, content, temp, idealTemp, targetQuality, baseQuality, appliedTemp;
    private int height, light, age, idealHeight, idealLight, liquid, cachedColor;
    public int time, minTime;
    private BarrelEntity fermenter;
    private boolean active, hasSkyAccess, hasMetadata = false;
    private BrewMetadata brewMetadata;

    private FermentationTracker(BarrelEntity entity) {
        fermenter = entity;
        active = false;
        height = fermenter.getPos().getY();
    }

    public static FermentationTracker initiate(BarrelEntity entity) {
        return new FermentationTracker(entity);
    }

    public void start(FFFermentingOutput recipe) {
        active = true;
        hasMetadata = false;
        brewMetadata = null;
        this.purity = 1;
        this.content = recipe.alcoholContent;
        this.quality = recipe.quality;
        this.baseQuality = recipe.quality;

        idealHeight = recipe.idealHeight;
        idealLight = recipe.idealLight;
        idealTemp = recipe.idealTemp;
        cachedColor = recipe.baseColor;

        liquid = fermenter.fluidInv.getInvFluid(0).getAmount_F().as1620();

        fetchInWorldStatus();

        time = 0;
        minTime = 72000;
        minTime = (int) ((minTime / (fermenter.fluidInv.getMaxAmount_F(0).as1620() / liquid)) * 1.5);
        if (minTime > 800 && config.isDebug())
            minTime = 800;
    }

    public boolean canOutput() {
        return hasMetadata;
    }

    public int getCachedColor() {
        return cachedColor;
    }

    private void fetchInWorldStatus() {
        light = fermenter.getWorld().getLightLevel(fermenter.getPos());
        temp = fermenter.getWorld().getBiome(fermenter.getPos()).getTemperature();
        height = fermenter.getPos().getY();
        BlockPos pos = new BlockPos(fermenter.getPos().getX(), fermenter.getWorld().getHeight(), fermenter.getPos().getZ());

        while (pos.getY() > fermenter.getPos().getY()) {
            if (fermenter.getWorld().getBlockState(pos) != Blocks.AIR.getDefaultState()) {
                hasSkyAccess = false;
                return;
            }
        }
        hasSkyAccess = true;
    }

    public void tick() {
        if ((time >= minTime && active) || hasMetadata) {
            if (!hasMetadata) {
                hasMetadata = true;
                active = false;
            }
            if (fermenter.fluidInv.getTank(0).get().isEmpty() && hasMetadata) {
                restart();
                return;
            }
            outputMetadata();
        }
        if (!active) {
            age++;
            return;
        } else {
            time++;
            age++;
        }
        if (liquid != fermenter.fluidInv.getTank(0).get().getAmount_F().as1620()) {
            cancel();
        }
        if (time % 100 == 0) {
            fetchInWorldStatus();
            calculateTargetQuality();
            if (!fermenter.getWorld().isClient && config.isDebug())
                FFLog.info("Light " + light + ", MinTime " + minTime + ", Quality " + quality + ", Time " + time + ", Target Quality" + targetQuality + ", Current Temperature " + appliedTemp);

        }
        calculateQuality();
    }

    private void restart() {
        cancel();
        time = 0;
        hasMetadata = false;
        brewMetadata = null;
    }

    private void outputMetadata() {
        if (brewMetadata == null) {
            brewMetadata = new BrewMetadata(true, false, quality, purity, content);
        }
        ItemStack input = fermenter.inventory.get(0);
        ItemStack output = fermenter.inventory.get(2);
        FluidVolume tank = fermenter.fluidInv.getInvFluid(0);
        if (!input.isEmpty() && output.getCount() < 16 && input.getItem() == Items.GLASS_BOTTLE && tank.getAmount_F().as1620() >= 1) {
            FFFermentingOutput recipe = fermenter.lookUp(Registry.FLUID.getId(tank.getRawFluid()).toString());
            String out = recipe.out;
            Item item = Registry.ITEM.get(new Identifier(out));
            if (output.isEmpty()) {
                input.decrement(1);
                fermenter.inventory.set(2, new ItemStack(item, 1));
                fermenter.inventory.get(2).putSubTag("brewmetadata", brewMetadata.toTag());
                fermenter.fluidInv.getTank(0).extract(FluidAmount.BUCKET);
            } else if (output.getItem() == item) {
                input.decrement(1);
                fermenter.inventory.get(2).increment(1);
                fermenter.fluidInv.getTank(0).extract(FluidAmount.BUCKET);
            }
        }
    }

    private void calculateTargetQuality() {
        targetQuality = baseQuality;
        double tempDif = Math.abs(idealTemp - getAppliedTemp()), heightDif = Math.abs(idealHeight - height), lightDif = Math.abs(idealLight - light);
        if (tempDif < 0.3) {
            targetQuality += Math.abs(tempDif - 0.3);
        } else if (tempDif > 0.6) {
            targetQuality -= (tempDif - 0.6) / 2;
        }
        if (idealHeight != -1) {
            if (heightDif < 10) {
                targetQuality += Math.abs(heightDif - 10) / 100.0;
            } else if (heightDif > 40) {
                targetQuality -= Math.min((heightDif - 40) / 400.0, 0.2);
            }
        }
        if (idealLight != -1) {
            if (lightDif < 4) {
                targetQuality += Math.abs(lightDif - 4) / 40;
            } else if (lightDif > 10) {
                targetQuality -= Math.min((lightDif - 10) / 40, 0.1);
            }
        }
        targetQuality = Math.min(targetQuality, 1);
        targetQuality = Math.max(targetQuality, 0.05);
    }

    private double getAppliedTemp() {
        World world = fermenter.getWorld();
        Biome biome = world.getBiome(fermenter.getPos());
        appliedTemp = temp;
        appliedTemp += light / 100.0;

        if (height < 64) {
            appliedTemp += Math.pow(Math.abs(height - 64) / 120.0, 2);
        } else if (height >= 100) {
            appliedTemp -= Math.pow((height - 100) / 150.0, 2);
        }

        if (hasSkyAccess && world.isRaining() && biome.getCategory() != Biome.Category.DESERT) {
            if (biome.getTemperature() > 0)
                appliedTemp -= 0.05;
            else
                appliedTemp -= 0.2;
        }

        if (biome.getCategory() == Biome.Category.NETHER)
            appliedTemp += 1;
        else if (biome.getCategory() == (Biome.Category.THEEND))
            appliedTemp -= 1;

        return appliedTemp;
    }

    private void calculateQuality() {
        double qualDif = Math.abs(targetQuality - quality);
        if (qualDif < 0.001) {
            quality = targetQuality;
            return;
        }
        if (quality > targetQuality) {
            quality -= qualDif / 2000;
        } else if (quality < targetQuality) {
            quality += qualDif / 2000;
        }
    }

    public void cancel() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public void toTag(CompoundTag compoundTag) {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("quality", quality);
        tag.putDouble("content", content);
        tag.putDouble("temp", idealTemp);
        tag.putDouble("setquality", baseQuality);
        tag.putDouble("targetquality", targetQuality);
        tag.putInt("height", idealHeight);
        tag.putInt("light", idealLight);
        tag.putInt("liquid", liquid);
        tag.putInt("age", age);
        tag.putInt("time", time);
        tag.putInt("mintime", minTime);
        tag.putInt("color", cachedColor);
        tag.putBoolean("active", active);
        tag.putBoolean("metadata", hasMetadata);
        compoundTag.put("tracker", tag);
    }

    public void fromTag(CompoundTag compoundTag, BarrelEntity entity) {
        CompoundTag tag = compoundTag.getCompound("tracker");
        fermenter = entity;
        quality = tag.getDouble("quality");
        baseQuality = tag.getDouble("setquality");
        targetQuality = tag.getDouble("targetquality");
        content = tag.getDouble("content");
        idealTemp = tag.getDouble("idealTemp");
        idealHeight = tag.getInt("height");
        idealLight = tag.getInt("light");
        liquid = tag.getInt("liquid");
        age = tag.getInt("age");
        time = tag.getInt("time");
        minTime = tag.getInt("mintime");
        cachedColor = tag.getInt("color");
        active = tag.getBoolean("active");
        hasMetadata = tag.getBoolean("metadata");
        purity = 1;
    }
}
