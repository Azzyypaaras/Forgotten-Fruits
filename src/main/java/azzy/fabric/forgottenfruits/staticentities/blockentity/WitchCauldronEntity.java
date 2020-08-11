package azzy.fabric.forgottenfruits.staticentities.blockentity;

import azzy.fabric.forgottenfruits.registry.ItemRegistry;
import azzy.fabric.forgottenfruits.registry.ParticleRegistry;
import azzy.fabric.forgottenfruits.util.interaction.HeatHolder;
import azzy.fabric.forgottenfruits.util.interaction.HeatTransferHelper;
import azzy.fabric.forgottenfruits.util.tracker.BrewMetadata;
import azzy.fabric.forgottenfruits.util.tracker.BrewingTracker;
import com.google.common.util.concurrent.AtomicDouble;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import static azzy.fabric.forgottenfruits.ForgottenFruits.FFLog;
import static azzy.fabric.forgottenfruits.ForgottenFruits.config;
import static azzy.fabric.forgottenfruits.block.entity.WitchCauldronBlock.CHAOTIC;
import static azzy.fabric.forgottenfruits.block.entity.WitchCauldronBlock.EFFULGENT;
import static azzy.fabric.forgottenfruits.registry.BlockEntityRegistry.WITCH_CAULDRON;

public class WitchCauldronEntity extends MachineEntity implements HeatHolder {

    private BrewMetadata metadata;
    private Item brew;
    private AtomicDouble heat;
    private BrewingTracker tracker;

    public WitchCauldronEntity() {
        super(WITCH_CAULDRON);
        inventory = DefaultedList.ofSize(512, ItemStack.EMPTY);
    }

    @Override
    public void tick() {
        if (hasWorld()) {
            World world = Objects.requireNonNull(getWorld());
            if (heat == null) heat = new AtomicDouble(HeatTransferHelper.translateBiomeHeat(world.getBiome(this.pos)));
            if (world.isClient() && heat.get() >= 100 && world.getRandom().nextInt(9) == 0) {
                spawnParticles();
            }
            if (world.getTime() % 5 == 0 && metadata != null) {
                fetchDroppedItems();

                if (config.isDebug())
                    FFLog.error("Cauldron Temperature: " + heat);
                Block source = world.getBlockState(pos.down()).getBlock();
                BlockEntity entity = world.getBlockEntity(pos.down());
                for(int i = 0; i < 4; i++) {
                    HeatTransferHelper.simulateAmbientHeat(this, world.getBiome(pos));
                    if (entity instanceof HeatHolder) {
                        HeatTransferHelper.simulateHeat(HeatTransferHelper.HeatMaterial.IRON, this, (HeatHolder) entity);
                    } else if (HeatTransferHelper.isHeatSource(source)) {
                        HeatTransferHelper.simulateHeat(HeatTransferHelper.HeatMaterial.IRON, this, source);
                    }
                }
            }
        }

        super.tick();
    }

    public void spawnParticles() {
        if (hasWorld()) {
            Random rand = Objects.requireNonNull(world).getRandom();
            int variation = rand.nextInt(6) + 12;
            for (int i = 0; i < 1 + variation; i++) {
                double positionX = rand.nextDouble() * 0.5 + 0.25;
                double positionZ = rand.nextDouble() * 0.5 + 0.25;
                Particle particle = MinecraftClient.getInstance().particleManager.addParticle(ParticleRegistry.CAULDRON_BUBBLES, pos.getX() + positionX, pos.getY() + 0.3, pos.getZ() + positionZ, 0, 0.0, 0);
                if (particle != null) {
                    int color = tracker.getColor();
                    int r = color >> 16 & 0xFF;
                    int b = color >> 8 & 0xFF;
                    int g = color & 0xFF;
                    particle.setColor(r / 255f, g / 255f, b / 255f);
                    MinecraftClient.getInstance().particleManager.addParticle(particle);
                    particle.setMaxAge(world.getRandom().nextInt(35) + 10);
                }
            }
        }
    }

    public void fetchDroppedItems() {
        List<ItemEntity> items = Objects.requireNonNull(getWorld()).getNonSpectatingEntities(ItemEntity.class, new Box(this.pos.add(0, 0.6875, 0), this.pos.add(1, 1, 1)));
        items.forEach(e -> {
            for (int i = 1; i < inventory.size(); i++) {
                ItemStack stack = inventory.get(i);
                ItemStack drops = e.getStack();
                if (stack.isEmpty()) {
                    inventory.set(i, drops);
                    e.kill();
                    playDropEffects();
                    break;
                } else if (drops.getItem() == stack.getItem() && stack.getCount() < 64) {
                    if (drops.getCount() + stack.getCount() <= 64) {
                        inventory.get(i).increment(drops.getCount());
                        e.kill();
                        playDropEffects();
                    }
                    break;
                }
            }
        });
    }

    public BrewMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(BrewMetadata metadata, Item brew) {
        this.metadata = metadata;
        this.brew = brew;
    }

    public Item getBrew() {
        return brew;
    }

    public BrewingTracker getTracker() {
        return tracker;
    }

    public void playDropEffects() {
        if (world instanceof ServerWorld) {
            ((ServerWorld) world).spawnParticles(ParticleTypes.SPLASH, this.pos.getX() + 0.5, this.pos.getY() + 0.69, this.pos.getZ() + 0.5, world.random.nextInt(5) + 2, 0, 0.0, 0, 0);
        } else {
            world.playSound(this.pos.getX() + 0.5, this.pos.getY() + 0.7, this.pos.getZ() + 0.5, SoundEvents.ENTITY_PLAYER_SPLASH, SoundCategory.BLOCKS, 0.9f, 1.1f, false);
        }
    }

    public void updateGem() {
        Objects.requireNonNull(this.world).setBlockState(this.pos, this.getCachedState().with(EFFULGENT, inventory.get(0).getItem() == ItemRegistry.ATTUNED_EFFULGENT).with(CHAOTIC, inventory.get(0).getItem() == ItemRegistry.ATTUNED_CHAOTIC));
    }

    private void toBaseTag(CompoundTag tag) {
        if (metadata != null)
            tag.put("metadata", metadata.toTag());
        if (heat != null) tag.putDouble("heat", heat.get());
    }

    private void fromBaseTag(CompoundTag tag) {
        if (tag.contains("metadata")) metadata = BrewMetadata.fromTag(tag.getCompound("metadata"));
        heat = new AtomicDouble(tag.getDouble("heat"));
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        toBaseTag(tag);
        return super.toTag(tag);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        fromBaseTag(tag);
        super.fromTag(state, tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag) {
        toBaseTag(compoundTag);
        return super.toClientTag(compoundTag);
    }

    @Override
    public void fromClientTag(CompoundTag compoundTag) {
        fromBaseTag(compoundTag);
        super.fromClientTag(compoundTag);
    }

    @Override
    public void sync() {
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    public double getHeat() {
        return heat == null ? 0 : heat.get();
    }

    @Override
    public void moveHeat(double change) {
        if (heat == null) heat = new AtomicDouble(change);
        else heat.addAndGet(change);
    }

    @Override
    public double getArea() {
        return 0;
    }
}
