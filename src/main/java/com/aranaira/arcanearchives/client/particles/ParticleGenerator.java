package com.aranaira.arcanearchives.client.particles;

import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.client.render.RenderHelper;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

public class ParticleGenerator {
	public static Random rng = new Random();

	public static void makeDefaultLine (World world, Vec3d start, Vec3d end, int count, double shiftIntensity) {
		double dist = start.distanceTo(end);
		end = end.add(0.5, 0.5, 0.5);
		double xShiftMult1 = rng.nextDouble() - 0.5;
		double xShiftMult2 = rng.nextDouble() - 0.5;
		double zShiftMult1 = rng.nextDouble() - 0.5;
		double zShiftMult2 = rng.nextDouble() - 0.5;
		double yShiftMult = rng.nextDouble() * 0.3;
		double distMagnifier = 1.0 + dist * 0.1;

		ArrayList<Vec3d> verts = new ArrayList<Vec3d>();
		for (int i = 0; i <= count; i++) {
			double prog = (double) i / (double) count;
			Vec3d pos = veclerp(start, end, prog);
			if (shiftIntensity > 0) {
				//TODO: shift based on normalized vector
				double x1 = Math.sin(prog * Math.PI * 2) * shiftIntensity * xShiftMult1;
				double x2 = Math.sin(prog * Math.PI) * shiftIntensity * xShiftMult2;
				double z1 = Math.sin(prog * Math.PI * 2) * shiftIntensity * zShiftMult1;
				double z2 = Math.sin(prog * Math.PI) * shiftIntensity * zShiftMult2;
				pos = pos.add((x1 + x2) * prog * distMagnifier, Math.sin(prog * Math.PI) * shiftIntensity * yShiftMult * prog * distMagnifier, (z1 + z2) * prog * distMagnifier);

			}
			double sx = ((rng.nextDouble() * 2.0) - 1.0) * 0.1;
			double sz = ((rng.nextDouble() * 2.0) - 1.0) * 0.1;
			double sy = ((rng.nextDouble() * 0.7) + 0.3) * 0.1;
			//ParticlePendeloqueGemLine p = new ParticlePendeloqueGemLine(world, pos.x, pos.y, pos.z, sx, sy, sz, 20+i, 2.0f);
			//Minecraft.getMinecraft().effectRenderer.addEffect(p);
			verts.add(pos);
		}
		RenderHelper.drawSegmentedLine(world.getWorldTime(), ArcaneGemItem.GemColor.getColor(ArcaneGemItem.GemColor.ORANGE), 1, start, verts);
	}

	public static void makeDefaultBurst (World world, Vec3d point, int radialCount, int heightCount, double radius, double speedMin, double speedMax) {

		for (int h = 0; h < heightCount; h++) {
			double py = (double) h / (double) heightCount;
			for (int i = 0; i < radialCount; i++) {
				double segment = (Math.PI * 2.0) / radialCount;
				double theta = i * segment;
				double ax = Math.cos(theta) * radius;
				double az = Math.sin(theta) * radius;

				double px = point.x + ax + 0.5;
				double pz = point.z + az + 0.5;

				double sx = ax * ((speedMax - speedMin) + speedMin);
				double sz = az * ((speedMax - speedMin) + speedMin);
				double sy = ((rng.nextDouble() * 0.7) + 0.3) * 0.1;


				world.spawnParticle(EnumParticleTypes.CLOUD, px, point.y + 0.5, pz, sx, sy * 2, sz);
			}
		}
	}

	public static void makeDefaultBurstOnEntity (World world, Vec3d point, int radialCount, int heightCount, double radius, double speedMin, double speedMax) {

		for (int h = 0; h < heightCount; h++) {
			double py = (double) h / (double) heightCount;
			for (int i = 0; i < radialCount; i++) {
				double segment = (Math.PI * 2.0) / radialCount;
				double theta = i * segment;
				double ax = Math.cos(theta) * radius;
				double az = Math.sin(theta) * radius;

				double px = point.x + ax;
				double pz = point.z + az;

				double sx = ax * ((speedMax - speedMin) + speedMin);
				double sz = az * ((speedMax - speedMin) + speedMin);
				double sy = Math.abs(rng.nextDouble() * 14.5 * ((speedMax - speedMin) + speedMin));

				world.spawnParticle(EnumParticleTypes.CLOUD, px, point.y + 0.5, pz, sx, sy, sz);
			}
		}
	}

	private static Vec3d veclerp (Vec3d start, Vec3d end, double prog) {
		if (prog < 0) {
			prog = 0;
		}
		if (prog > 1) {
			prog = 1;
		}

		double x = (end.x - start.x) * prog;
		x += start.x;
		double y = (end.y - start.y) * prog;
		y += start.y;
		double z = (end.z - start.z) * prog;
		z += start.z;

		return new Vec3d(x, y, z);
	}
}
