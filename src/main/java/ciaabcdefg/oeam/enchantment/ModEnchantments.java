package ciaabcdefg.oeam.enchantment;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantments {
    public static final ResourceKey<Enchantment> COUP_DE_GRACE
            = register("coup_de_grace");

    public static final ResourceKey<Enchantment> LIFESTEAL
            = register("lifesteal");

    private static ResourceKey<Enchantment> register(String id) {
        return ResourceKey.create(
                Registries.ENCHANTMENT,
                Identifier.fromNamespaceAndPath(OPEnchantsAndMore.MOD_ID, id)
        );
    }
}
