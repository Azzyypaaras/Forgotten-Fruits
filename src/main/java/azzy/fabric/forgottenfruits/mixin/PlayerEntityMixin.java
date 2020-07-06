package azzy.fabric.forgottenfruits.mixin;

import azzy.fabric.forgottenfruits.util.mixin.TransformationHolder;
import azzy.fabric.forgottenfruits.util.mixin.TransformationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Implements(@Interface(iface = TransformationHolder.class, prefix = "$"))
@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements TransformationHolder {
    private TransformationType transformation = TransformationType.NONE;

    @Inject(method = "updateSize", at = @At("HEAD"), cancellable = true)
    private void updateSize(CallbackInfo callbackInfo) {
        if (transformation != TransformationType.NONE) {
            //Set hitbox here
            callbackInfo.cancel();
        }
    }

    @Inject(method = "writeCustomDataToTag", at = @At("HEAD"))
    private void writeCustomDataToTag(CompoundTag nbt, CallbackInfo callbackInfo) {
        nbt.putInt("FFTransformation", transformation.ordinal());
    }

    @Inject(method = "readCustomDataFromTag", at = @At("HEAD"))
    private void readCustomDataFromTag(CompoundTag nbt, CallbackInfo callbackInfo) {
        transformation = TransformationType.values()[nbt.getInt("FFTransformation")];
    }

    @Override
    public TransformationType getTransformation() {
        return transformation;
    }
}
