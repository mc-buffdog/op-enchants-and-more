package ciaabcdefg.oeam.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantmentUtil {
    public static int getEnchantmentLevel(ItemStack stack, ResourceKey<Enchantment> enchantment) {
        for (var e : stack.getEnchantments().entrySet()) {
            if (e.getKey().is(enchantment)) {
                return e.getIntValue();
            }
        }
        return 0;
    }
}
