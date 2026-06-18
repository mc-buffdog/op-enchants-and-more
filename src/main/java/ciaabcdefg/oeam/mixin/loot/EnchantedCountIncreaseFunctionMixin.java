package ciaabcdefg.oeam.mixin.loot;

import ciaabcdefg.oeam.enchantment.ModEnchantments;
import ciaabcdefg.oeam.mixin.accessor.EnchantedCountIncreaseFunctionAccessor;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("DiscouragedShift")
@Mixin(EnchantedCountIncreaseFunction.class)
public class EnchantedCountIncreaseFunctionMixin {
    @Inject(
            method = "run",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getEnchantmentLevel(Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/LivingEntity;)I",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true)
    private void modifyRun(
            ItemStack itemStack,
            LootContext context,
            CallbackInfoReturnable<ItemStack> cir,
            @Local(name = "entity") LivingEntity entity
    ) {
        var greaterLooting = entity.level().registryAccess().getOrThrow(ModEnchantments.GREATER_LOOTING);
        int level = EnchantmentHelper.getEnchantmentLevel(greaterLooting, entity);
        if (level == 0) {
            return;
        }

        var accessor = (EnchantedCountIncreaseFunctionAccessor)this;
        var count = accessor.getCount();
        var limit = accessor.getLimit();
        var hasLimit = accessor.invokeHasLimit();

        float addition = 2 * level * (count.getFloat(context) + 0.1F);
        itemStack.grow(Math.round(addition));

        if (hasLimit) {
            itemStack.limitSize(limit);
        }

        cir.setReturnValue(itemStack);
        cir.cancel();
    }
}
