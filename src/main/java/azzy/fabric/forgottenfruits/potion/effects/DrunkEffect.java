package azzy.fabric.forgottenfruits.potion.effects;

import azzy.fabric.forgottenfruits.ClientInit;
import azzy.fabric.forgottenfruits.util.effect.FFStatusEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Random;

public class DrunkEffect extends FFStatusEffect {

    public DrunkEffect(StatusEffectType type, int color, boolean instant) {
        super(type, color, instant, false);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        amplifier++;

        if (entity.world.isClient() && entity instanceof PlayerEntity) {
            Random random = entity.getRandom();
            PlayerEntity player = (PlayerEntity) entity;
            if (player.world.getTime() % 100 / amplifier == 0 || (ClientInit.getCachedX() == 0 || ClientInit.getCachedY() == 0)) {
                int x = random.nextInt(2) == 0 ? random.nextInt(4) * amplifier : -(random.nextInt(4) * amplifier);
                int y = random.nextInt(2) == 0 ? random.nextInt(2) * amplifier : -(random.nextInt(2) * amplifier);
                ClientInit.setCachedLook(x, y);
            }
            player.changeLookDirection(ClientInit.getCachedX() / 1.5, ClientInit.getCachedY() / 1.5);
        }
    }
}
