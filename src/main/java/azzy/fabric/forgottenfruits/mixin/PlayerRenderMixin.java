package azzy.fabric.forgottenfruits.mixin;

import azzy.fabric.forgottenfruits.util.mixin.TransformationHolder;
import azzy.fabric.forgottenfruits.util.mixin.TransformationType;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerRenderMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    private PlayerEntityModel<AbstractClientPlayerEntity> originalModel;

    public PlayerRenderMixin(EntityRenderDispatcher dispatcher, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(dispatcher, model, shadowRadius);
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void render(AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo callbackInfo) {
        if (originalModel == null) originalModel = getModel();
        TransformationType transformation = ((TransformationHolder) abstractClientPlayerEntity).getTransformation();
        if (transformation == TransformationType.NONE) {
            model = originalModel;
        } else {
            //Set the model here, for example:
            /*switch (transformation) {
                case WEREWOLF:
                    model = werewolfModel;
                    break;
            }*/
        }
    }
}
