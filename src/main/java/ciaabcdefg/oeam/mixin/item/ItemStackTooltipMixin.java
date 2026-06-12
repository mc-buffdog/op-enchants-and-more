package ciaabcdefg.oeam.mixin.item;

import ciaabcdefg.oeam.enchantment.ModEnchantments;
import ciaabcdefg.oeam.enchantment.custom.CleaveEnchantment;
import ciaabcdefg.oeam.enchantment.custom.CoupDeGraceEnchantment;
import ciaabcdefg.oeam.enchantment.custom.GiantSlayerEnchantment;
import ciaabcdefg.oeam.enchantment.custom.LifestealEnchantment;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemStackTooltipMixin {
    @Inject(
            method = "addDetailsToTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;addAttributeTooltips(Ljava/util/function/Consumer;Lnet/minecraft/world/item/component/TooltipDisplay;Lnet/minecraft/world/entity/player/Player;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void addEnchantmentEffectsTooltips(
            Item.TooltipContext context,
            TooltipDisplay display,
            @Nullable Player player,
            TooltipFlag tooltipFlag,
            Consumer<Component> builder,
            CallbackInfo ci
    ) {
        var self = (ItemStack)(Object)this;
        var enchantments = self.getEnchantments();

        for (var e : enchantments.entrySet()) {
            var key = e.getKey().unwrapKey();
            if (key.isEmpty()) continue;

            var rk = key.get();
            var level = e.getIntValue();

            if (rk.equals(ModEnchantments.LIFESTEAL)) {
                LifestealEnchantment.displayTooltip(builder, level);
            }
            else if (rk.equals(ModEnchantments.COUP_DE_GRACE)) {
                CoupDeGraceEnchantment.displayTooltip(builder, level);
            }
            else if (rk.equals(ModEnchantments.CLEAVE)) {
                CleaveEnchantment.displayTooltip(builder, level);
            }
            else if (rk.equals(ModEnchantments.GIANT_SLAYER)) {
                GiantSlayerEnchantment.displayTooltip(builder, level);
            }
        }
    }
}
