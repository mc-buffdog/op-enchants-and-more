package ciaabcdefg.oeam.mixin;

import ciaabcdefg.oeam.attribute.ModAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LivingEntity.class)
public class ElytraMixin {
    @ModifyReturnValue(
            method = "createLivingAttributes",
            at = @At("RETURN")
    )
    private static AttributeSupplier.Builder modifyAttributes(AttributeSupplier.Builder original) {
        return original.add(ModAttributes.ELYTRA_SPEED_BONUS);
    }

    @Unique
    private static double map(double value, double start1, double stop1, double start2, double stop2) {
        return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
    }

    @ModifyArgs(
            method = "updateFallFlyingMovement",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;multiply(DDD)Lnet/minecraft/world/phys/Vec3;"
            )
    )
    private void modifyDrag(Args args) {
        var entity = (LivingEntity)(Object)this;
        double speedBonus = entity.getAttributeValue(ModAttributes.ELYTRA_SPEED_BONUS);

        double x = args.get(0);
        double y = args.get(1);
        double z = args.get(2);

        double mod = map(speedBonus, -10, 10, 0.909090909091, 1.010101010101010101);

        args.set(0, x * mod);
        args.set(1, y);
        args.set(2, z * mod);
    }
}
