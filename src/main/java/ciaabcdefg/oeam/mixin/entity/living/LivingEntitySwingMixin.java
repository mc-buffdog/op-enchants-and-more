package ciaabcdefg.oeam.mixin.entity.living;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntitySwingMixin {
    @ModifyExpressionValue(
            method = "swing(Lnet/minecraft/world/InteractionHand;Z)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/LivingEntity;swinging:Z",
                    opcode = Opcodes.GETFIELD
            )
    )
    private boolean cancelSwing(boolean original) {
        var self = (LivingEntity)(Object)this;
        var weapon = self.getWeaponItem();
        var isSpear = weapon.is(ItemTags.SPEARS);

        if (isSpear) {
            return false;
        }

        return original;
    }
}
