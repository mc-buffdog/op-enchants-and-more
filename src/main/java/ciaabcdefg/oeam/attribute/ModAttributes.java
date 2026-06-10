package ciaabcdefg.oeam.attribute;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class ModAttributes {
    public static final Holder<Attribute> LIFESTEAL = register(
            "lifesteal", 0, 0, 2, true
    );
    public static final Holder<Attribute> CRIT_CHANCE = register(
            "crit_chance", 0, 0, 1, true
    );
    public static final Holder<Attribute> CRIT_DAMAGE_MUL = register(
            "crit_damage_mul", 0, 0, 10, true
    );
    public static final Holder<Attribute> ELYTRA_SPEED_BONUS = register(
            "elytra_speed_bonus", 0, -10, 10, true
    );
    public static final Holder<Attribute> SWEEPING_AREA = register(
            "sweeping_area", 0, 0.0, 100.0, true
    );

    private static Holder<Attribute> register(
            String name, double defaultValue, double minValue, double maxValue, boolean syncedWithClient
    ) {
        Identifier identifier = Identifier.fromNamespaceAndPath(OPEnchantsAndMore.MOD_ID, name);
        Attribute entityAttribute = new RangedAttribute(
                "attribute.name." + identifier.toLanguageKey(),
                defaultValue,
                minValue,
                maxValue
        ).setSyncable(syncedWithClient);
        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, identifier, entityAttribute);
    }

    public static void initialize() {
        OPEnchantsAndMore.LOGGER.info("Initialized ModAttributes");
    }
}
