package ciaabcdefg.oeam.mixin;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import ciaabcdefg.oeam.enchantment.ModEnchantments;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("DiscouragedShift")
@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {
    @Inject(
            method = "createResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;setEnchantments(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/enchantment/ItemEnchantments;)V",
                    shift = At.Shift.BEFORE
            )
    )
    private void test(CallbackInfo ci, @Local(name = "enchantments") ItemEnchantments.Mutable enchantments) {
        AtomicReference<Holder<Enchantment>> greaterEfficiency = new AtomicReference<>();
        AtomicReference<Holder<Enchantment>> efficiency = new AtomicReference<>();

        enchantments.keySet().forEach(holder -> {
            if (holder.is(ModEnchantments.GREATER_EFFICIENCY)) {
                greaterEfficiency.set(holder);
            } else if (holder.is(Enchantments.EFFICIENCY)) {
                efficiency.set(holder);
            }
        });

        if (greaterEfficiency.get() != null && efficiency.get() != null) {
            OPEnchantsAndMore.LOGGER.info("Has both GrEff and Eff - removing Eff");
            enchantments.set(efficiency.get(), 0);
        }
    }

    @Redirect(
            method = "createResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/Enchantment;areCompatible(Lnet/minecraft/core/Holder;Lnet/minecraft/core/Holder;)Z"
            )
    )
    private boolean redirectAreCompatible(Holder<Enchantment> enchantment, Holder<Enchantment> other) {
        if ((enchantment.is(ModEnchantments.GREATER_EFFICIENCY) && other.is(Enchantments.EFFICIENCY))
                        || (enchantment.is(Enchantments.EFFICIENCY) && other.is(ModEnchantments.GREATER_EFFICIENCY)))
            return true;
        return Enchantment.areCompatible(enchantment, other);
    }
}
