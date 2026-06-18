package ciaabcdefg.oeam.enchantment.tag;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantmentTags {
    public static TagKey<Enchantment> EFFICIENCY_EXCLUSIVE = create("exclusive_set/efficiency");
    public static TagKey<Enchantment> LOOTING_EXCLUSIVE = create("exclusive_set/looting");
    public static TagKey<Enchantment> UNBREAKING_EXCLUSIVE = create("exclusive_set/unbreaking");
    public static TagKey<Enchantment> MENDING_EXCLUSIVE = create("exclusive_set/mending");

    public static void initialize() {
        OPEnchantsAndMore.LOGGER.info("Initialized ModEnchantmentTags");
    }

    private static TagKey<Enchantment> create(final String name) {
        return TagKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(OPEnchantsAndMore.MOD_ID, name));
    }
}
