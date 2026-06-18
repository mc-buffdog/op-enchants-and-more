package ciaabcdefg.oeam.enchantment;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import ciaabcdefg.oeam.enchantment.effect.ModEnchantmentEffects;
import ciaabcdefg.oeam.enchantment.tag.ModEnchantmentTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;


public class ModEnchantments {
    // New enchantments
    public static final ResourceKey<Enchantment> COUP_DE_GRACE
            = register("coup_de_grace");
    public static final ResourceKey<Enchantment> LIFESTEAL
            = register("lifesteal");
    public static final ResourceKey<Enchantment> BUTTERFLY
            = register("butterfly");
    public static final ResourceKey<Enchantment> CLEAVE
            = register("cleave");
    public static final ResourceKey<Enchantment> SOAR
            = register("soar");
    public static final ResourceKey<Enchantment> GIANT_SLAYER
            = register("giant_slayer");
    public static final ResourceKey<Enchantment> DESOLATOR
            = register("desolator");
    public static final ResourceKey<Enchantment> SMELTING
            = register("smelting");
    public static final ResourceKey<Enchantment> HEART_OF_TARASQUE
            = register("heart_of_tarasque");

    // Greater versions of vanilla enchantments
    public static final ResourceKey<Enchantment> GREATER_SHARPNESS
            = register("greater_sharpness");
    public static final ResourceKey<Enchantment> GREATER_EFFICIENCY
            = register("greater_efficiency");
    public static final ResourceKey<Enchantment> GREATER_PROTECTION
            = register("greater_protection");
    public static final ResourceKey<Enchantment> GREATER_FORTUNE
            = register("greater_fortune");
    public static final ResourceKey<Enchantment> GREATER_LOOTING
            = register("greater_looting");
    public static final ResourceKey<Enchantment> GREATER_MENDING
            = register("greater_mending");
    public static final ResourceKey<Enchantment> GREATER_UNBREAKING
            = register("greater_unbreaking");

    // Methods
    private static ResourceKey<Enchantment> register(String id) {
        return ResourceKey.create(
                Registries.ENCHANTMENT,
                Identifier.fromNamespaceAndPath(OPEnchantsAndMore.MOD_ID, id)
        );
    }

    public static void initialize() {
        ModEnchantmentEffects.initialize();
        ModEnchantmentTags.initialize();
        OPEnchantsAndMore.LOGGER.info("Initialized ModEnchantments");
    }
}
