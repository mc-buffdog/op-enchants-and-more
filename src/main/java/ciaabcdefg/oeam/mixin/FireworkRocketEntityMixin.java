package ciaabcdefg.oeam.mixin;

import ciaabcdefg.oeam.attribute.ModAttributes;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FireworkRocketEntity.class)
public class FireworkRocketEntityMixin {
    @Unique
    private static final double POWER = 1.5;
    @Unique
    private static final double LERP_RATE = 0.5;
    @Unique
    private static final double NUDGE = 0.1;

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V")
    )
    private void modifyRocketImpulse(
            LivingEntity attachedToEntity,
            Vec3 vec3,
            @Local(name = "lookAngle") Vec3 lookAngle,
            @Local(name = "movement") Vec3 movement)
    {
        double speedBonus = attachedToEntity.getAttributeValue(ModAttributes.ELYTRA_SPEED_BONUS);
        double mod = map(speedBonus);
        double power = POWER * mod;

        attachedToEntity.setDeltaMovement(
                movement.add(
                        lookAngle.x * NUDGE + (lookAngle.x * power - movement.x) * LERP_RATE,
                        lookAngle.y * NUDGE + (lookAngle.y * power - movement.y) * LERP_RATE,
                        lookAngle.z * NUDGE + (lookAngle.z * power - movement.z) * LERP_RATE
                )
        );
    }

    @Unique
    private static double map(double value) {
        return 0.25 + (2 - 0.25) * ((value + 10) / (10 + 10));
    }
}
