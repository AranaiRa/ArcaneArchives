package com.aranaira.arcanearchives.client.particles.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ParticlePendeloqueGemLine extends Particle {

	float particleSize;
	//ResourceLocation TEXTURE = new ResourceLocation(ArcaneArchives.MODID, "particles/simple_soft");
	ResourceLocation TEXTURE = new ResourceLocation("particles/simple_soft");

	public ParticlePendeloqueGemLine (World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int lifespan, float sizeIn) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);

		this.motionX = 0.0F;
		this.motionY = 0.0F;
		this.motionZ = 0.0F;
		this.posX += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
		this.posY += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
		this.posZ += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
		this.particleSize = sizeIn;
		this.particleScale = particleSize;
		this.particleRed = 1.0F;
		this.particleGreen = 1.0F;
		this.particleBlue = 1.0F;
		this.particleMaxAge = lifespan;

		this.setParticleTexture(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(TEXTURE.toString()));

		this.particleGravity = 0.004f * rand.nextFloat();
	}

	@Override
	public void onUpdate () {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.particleAge++ >= this.particleMaxAge) {
			this.setExpired();
		} else if (this.particleAge > this.particleMaxAge / 2) {
			float ageFactor = ((float) particleAge / (float) particleMaxAge) - 0.5f;
			ageFactor *= 2;

			this.particleAlpha = 1.0f - ageFactor;
			this.particleScale = particleSize * ((1.0f - ageFactor) * 0.5f + 0.5f);
		}

		this.motionY -= 0.04D * (double) this.particleGravity;
		this.move(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9800000190734863D;
		this.motionY *= 0.9800000190734863D;
		this.motionZ *= 0.9800000190734863D;

		if (this.onGround) {
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}
	}

	@Override
	public int getFXLayer () {
		return 1;
	}
}
