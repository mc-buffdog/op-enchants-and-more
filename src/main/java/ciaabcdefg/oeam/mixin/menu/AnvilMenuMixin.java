package ciaabcdefg.oeam.mixin.menu;

import ciaabcdefg.oeam.enchantment.ModEnchantments;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
        enchantmentsPrecedence(enchantments, ModEnchantments.GREATER_EFFICIENCY, Enchantments.EFFICIENCY);
        enchantmentsPrecedence(enchantments, ModEnchantments.GREATER_SHARPNESS, Enchantments.SHARPNESS);
        enchantmentsPrecedence(enchantments, ModEnchantments.GREATER_PROTECTION, Enchantments.PROTECTION);
        enchantmentsPrecedence(enchantments, ModEnchantments.GREATER_FORTUNE, Enchantments.FORTUNE);
        enchantmentsPrecedence(enchantments, ModEnchantments.GREATER_LOOTING, Enchantments.LOOTING);
    }

    @Redirect(
            method = "createResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/Enchantment;areCompatible(Lnet/minecraft/core/Holder;Lnet/minecraft/core/Holder;)Z"
            )
    )
    private boolean redirectAreCompatible(Holder<Enchantment> enchantment, Holder<Enchantment> other) {
        return
                allowEnchantments(enchantment, other, ModEnchantments.GREATER_EFFICIENCY, Enchantments.EFFICIENCY) ||
                allowEnchantments(enchantment, other, ModEnchantments.GREATER_SHARPNESS, Enchantments.SHARPNESS) ||
                allowEnchantments(enchantment, other, ModEnchantments.GREATER_PROTECTION, Enchantments.PROTECTION) ||
                allowEnchantments(enchantment, other, ModEnchantments.GREATER_FORTUNE, Enchantments.FORTUNE) ||
                allowEnchantments(enchantment, other, ModEnchantments.GREATER_LOOTING, Enchantments.LOOTING) ||
                Enchantment.areCompatible(enchantment, other);
    }

    @Unique
    private static boolean allowEnchantments(
            Holder<Enchantment> enchantment1,
            Holder<Enchantment> enchantment2,
            ResourceKey<Enchantment> enchantmentKey1,
            ResourceKey<Enchantment> enchantmentKey2)
    {
        return (enchantment1.is(enchantmentKey1) && enchantment2.is(enchantmentKey2))
                || (enchantment1.is(enchantmentKey2) && enchantment2.is(enchantmentKey1));
    }

    @Unique
    private static void enchantmentsPrecedence(ItemEnchantments.Mutable enchantments, ResourceKey<Enchantment> bigger, ResourceKey<Enchantment> smaller) {
        AtomicReference<Holder<Enchantment>> biggerHolder = new AtomicReference<>();
        AtomicReference<Holder<Enchantment>> smallerHolder = new AtomicReference<>();

        enchantments.keySet().forEach(holder -> {
            if (holder.is(bigger)) {
                biggerHolder.set(holder);
            } else if (holder.is(smaller)) {
                smallerHolder.set(holder);
            }
        });

        if (biggerHolder.get() != null && smallerHolder.get() != null) {
            enchantments.set(smallerHolder.get(), 0);
        }
    }
}
