package ciaabcdefg.oeam.particle.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.NonNull;

public class CoupDeGraceParticle extends SingleQuadParticle {
    private final SpriteSet sprites;

    public CoupDeGraceParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteSet sprites) {
        super(world, x, y, z, velocityX, velocityY, velocityZ, sprites.first());
        this.sprites = sprites;
        this.gravity = 1F;
        this.lifetime = 8;
        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
        this.quadSize = 5f;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
    }

    @Override
    protected @NonNull Layer getLayer() {
        return Layer.OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            return new CoupDeGraceParticle(level, x, y, z, xAux, yAux, zAux, this.sprites);
        }
    }
}
