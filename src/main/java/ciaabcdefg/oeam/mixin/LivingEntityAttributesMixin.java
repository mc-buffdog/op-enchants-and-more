package ciaabcdefg.oeam.mixin;

import ciaabcdefg.oeam.attribute.ModAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityAttributesMixin {
    @ModifyReturnValue(
            method = "createLivingAttributes",
            at = @At("RETURN")
    )
    private static AttributeSupplier.Builder modifyAttributes(AttributeSupplier.Builder original) {
        return original
                .add(ModAttributes.ELYTRA_SPEED_BONUS)
                .add(ModAttributes.LIFESTEAL);
    }
}
