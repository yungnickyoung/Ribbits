package com.yungnickyoung.minecraft.ribbits.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class RibbitSpellParticle extends SingleQuadParticle {
    private final SpriteSet sprites;

    public RibbitSpellParticle(ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ, SpriteSet sprites, TextureAtlasSprite textureAtlasSprite) {
        super(level, x, y, z, textureAtlasSprite);
        this.xd = motionX;
        this.yd = motionY;
        this.zd = motionZ;
        this.quadSize *= level.getRandom().nextFloat() * 0.4F + 1.0F;
        this.lifetime = level.getRandom().nextInt(15) + 20;
        this.sprites = sprites;
        this.setSpriteFromAge(this.sprites);
    }

    @Override
    public void tick() {
        super.tick();
        this.yd += level.getRandom().nextDouble() * 0.01D;
        this.setSpriteFromAge(this.sprites);
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel levelIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource randomSource) {
            return new RibbitSpellParticle(levelIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, this.spriteSet.get(randomSource));
        }
    }
}
