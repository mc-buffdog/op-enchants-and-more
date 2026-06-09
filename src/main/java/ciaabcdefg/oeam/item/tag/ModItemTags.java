package ciaabcdefg.oeam.item.tag;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {
    public static final TagKey<Item> CLEAVE_WEAPON_ENCHANTABLE = create("enchantable/cleave_weapon");

    private static TagKey<Item> create(final String name) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(OPEnchantsAndMore.MOD_ID, name));
    }
}
