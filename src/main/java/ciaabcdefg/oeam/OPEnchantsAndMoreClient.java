package ciaabcdefg.oeam;

import ciaabcdefg.oeam.particle.ModParticles;
import ciaabcdefg.oeam.particle.custom.CoupDeGraceParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;

@Environment(EnvType.CLIENT)
public class OPEnchantsAndMoreClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ParticleProviderRegistry.getInstance().register(ModParticles.COUP_DE_GRACE_PARTICLE, CoupDeGraceParticle.Provider::new);
    }
}
