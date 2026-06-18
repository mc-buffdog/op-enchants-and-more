package ciaabcdefg.oeam.effect;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import ciaabcdefg.oeam.effect.custom.DesolatorEffect;
import ciaabcdefg.oeam.effect.custom.VitalityEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;

public class ModEffects {
    public static final Holder<MobEffect> DESOLATOR = register("desolator", new DesolatorEffect());
    public static final Holder<MobEffect> VITALITY = register("vitality", new VitalityEffect());

    public static void initialize() {
        OPEnchantsAndMore.LOGGER.info("Initialized ModEffects");
    }

    private static Holder<MobEffect> register(String name, MobEffect mobEffect) {
        return Registry.registerForHolder(
                BuiltInRegistries.MOB_EFFECT,
                Identifier.fromNamespaceAndPath(OPEnchantsAndMore.MOD_ID, name), mobEffect
        );
    }
}
