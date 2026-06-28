package ciaabcdefg.oeam.item.tag;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {
    public static final TagKey<Item> CLEAVE_WEAPON_ENCHANTABLE = create("enchantable/cleave_weapon");
    public static final TagKey<Item> FLYING_ENCHANTABLE = create("enchantable/flying");
    public static final TagKey<Item> HEART_OF_TARASQUE_ENCHANTABLE = create("enchantable/heart_of_tarasque");

    private static TagKey<Item> create(final String name) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(OPEnchantsAndMore.MOD_ID, name));
    }
}
