package ciaabcdefg.oeam.particle;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class ModParticles {
    public static final SimpleParticleType COUP_DE_GRACE_PARTICLE =
            register("coup_de_grace_particle", FabricParticleTypes.simple());

    private static SimpleParticleType register(String name, SimpleParticleType particleType) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, Identifier.fromNamespaceAndPath(OPEnchantsAndMore.MOD_ID, name), particleType);
    }

    public static void initialize() {
        OPEnchantsAndMore.LOGGER.info("Registering particles");
    }
}
