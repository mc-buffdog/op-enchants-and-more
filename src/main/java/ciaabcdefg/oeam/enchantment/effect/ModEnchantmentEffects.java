package ciaabcdefg.oeam.enchantment.effect;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;

public class ModEnchantmentEffects {
    private static <T extends EnchantmentEntityEffect> MapCodec<T> register(String id, MapCodec<T> codec) {
        return Registry.register(
                BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE,
                Identifier.fromNamespaceAndPath(OPEnchantsAndMore.MOD_ID, id),
                codec
        );
    }

    public static void initialize() {
        OPEnchantsAndMore.LOGGER.info("Initialized ModEnchantmentEffects");
    }
}
