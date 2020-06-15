package azzy.fabric.azzyfruits.staticentities.blockentity;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv;
import azzy.fabric.azzyfruits.util.InventoryWrapper;
import azzy.fabric.azzyfruits.util.recipe.RecipeHandler;
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

import static azzy.fabric.azzyfruits.ForgottenFruits.REGISTEREDRECIPES;

public class MachineEntity extends BlockEntity implements Tickable, InventoryWrapper, SidedInventory, PropertyDelegateHolder, BlockEntityClientSerializable {

    //DEFAULT VALUES, DO NOT FORGET TO OVERRIDE THESE

    public DefaultedList<ItemStack> inventory = DefaultedList.ofSize(0, ItemStack.EMPTY);
    protected String identity = "VOID";
    public SimpleFixedFluidInv fluidInv;
    protected boolean isActive = false;
    protected int progress = 0;
    protected String state = "default";

    public MachineEntity(BlockEntityType<? extends MachineEntity> entityType){
        super(entityType);
        fluidInv = new SimpleFixedFluidInv(0, new FluidAmount(0));
    }

    //ALSO OVERRIDE THIS

    @Override
    public void tick(){
        if(!world.isClient)
            sync();
    }

    @Override
    public int[] getInvAvailableSlots(Direction var1) {
        int[] result = new int[getInvSize()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        return result;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);

        //Inventory nbt
        Inventories.toTag(tag, inventory);

        //Fluid nbt
        tag.put("fluid", fluidInv.toTag());

        //State nbt
        if(isActive || state != "default"){
            tag.putInt("progress", progress);
            tag.putBoolean("active", isActive);
            tag.putString("state", state);
        }
        return tag;
    }

    @Override
    public void fromTag(CompoundTag tag) {

        //Inventory nbt
        Inventories.fromTag(tag, inventory);

        //Fluid nbt
        fluidInv.fromTag(tag.getCompound("fluid"));


        //State nbt
        progress = tag.getInt("progress");
        isActive = tag.getBoolean("active");
        state = tag.getString("state");
        super.fromTag(tag);

    }

    @Override
    public void markDirty() {
        super.markDirty();
    }

    @Override
    public boolean canInsertInvStack(int slot, ItemStack stack, Direction direction) {
        return direction == Direction.UP;
    }

    @Override
    public boolean canExtractInvStack(int slot, ItemStack stack, Direction direction) {
        return direction == Direction.DOWN;
    }

    public RecipeHandler getRecipeHandler(String id){
        if (REGISTEREDRECIPES.containsKey(id)){
            return REGISTEREDRECIPES.get(id).getHandler();
        }
        else{
            return null;
        }
    }

    PropertyDelegate tankHolder = new PropertyDelegate() {
        @Override
        public int get(int index) {
            switch(index){
                case 0:
                    return 0;
            }
            return 0;
        }

        @Override
        public void set(int index, int value) {
            switch(index) {
                case 0:
                    break;
            }
        }

        @Override
        public int size() {
            return 0;
        }
    };

    @Override
    public PropertyDelegate getPropertyDelegate() {
        return tankHolder;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public void fromClientTag(CompoundTag compoundTag) {
        fluidInv.fromTag(compoundTag.getCompound("fluid"));
        progress = compoundTag.getInt("progress");
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag) {
        compoundTag.put("fluid", fluidInv.toTag());
        compoundTag.putInt("progress", progress);
        return compoundTag;
    }
}