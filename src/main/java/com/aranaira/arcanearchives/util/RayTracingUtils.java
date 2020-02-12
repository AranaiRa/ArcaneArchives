package com.aranaira.arcanearchives.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class RayTracingUtils {
  //Written by Daomephsta; Used with permission
  // TODO: Find original source file & license to link back to
  public static RayTraceResult raytraceClosestObject(World world, @Nullable Entity exclude, Vec3d startVec, Vec3d endVec) {
    RayTraceResult result = world.rayTraceBlocks(startVec, endVec);
    double blockHitDistance = 0.0D; // The distance to the block that was
    // hit
    if (result != null) {
      blockHitDistance = result.hitVec.distanceTo(startVec);
    }

    // Encloses the entire area where entities that could collide with this
    // ray exist
    AxisAlignedBB entitySearchArea = new AxisAlignedBB(startVec.x, startVec.y, startVec.z, endVec.x, endVec.y, endVec.z);
    Entity hitEntity = null; // The closest entity that was hit
    double entityHitDistance = 0.0D; // The squared distance to the closest
    // entity that was hit
    for (Entity entity : world.getEntitiesInAABBexcluding(exclude, entitySearchArea, EntitySelectors.NOT_SPECTATING)) {
      // The collision AABB of the entity expanded by the collision border
      // size
      AxisAlignedBB collisionBB = entity.getEntityBoundingBox().grow(entity.getCollisionBorderSize());
      RayTraceResult intercept = collisionBB.calculateIntercept(startVec, endVec);
      if (intercept != null) {
        double distance = startVec.distanceTo(intercept.hitVec);

        if ((distance < blockHitDistance || blockHitDistance == 0) && (distance < entityHitDistance || entityHitDistance == 0.0D)) {
          entityHitDistance = distance;
          hitEntity = entity;
        }
      }
    }

    if (hitEntity != null) {
      result = new RayTraceResult(hitEntity, hitEntity.getPositionVector());
    }

    return result;
  }

  //Written by Daomephsta; Used with permission
  public static void raytraceAll(List<RayTraceResult> results, World world, @Nullable Entity exclude, Vec3d startVec, Vec3d endVec) {
    RayTraceResult blockRaytrace = world.rayTraceBlocks(startVec, endVec);
    if (blockRaytrace != null) {
      results.add(blockRaytrace);
    }

    // Encloses the entire area where entities that could collide with this
    // ray exist
    AxisAlignedBB entitySearchArea = new AxisAlignedBB(startVec.x, startVec.y, startVec.z, endVec.x, endVec.y, endVec.z);
    for (Entity entity : world.getEntitiesInAABBexcluding(exclude, entitySearchArea, EntitySelectors.NOT_SPECTATING)) {
      // The collision AABB of the entity expanded by the collision border
      // size
      AxisAlignedBB collisionBB = entity.getEntityBoundingBox().grow(entity.getCollisionBorderSize());
      RayTraceResult intercept = collisionBB.calculateIntercept(startVec, endVec);
      if (intercept != null) {
        results.add(new RayTraceResult(entity, intercept.hitVec));
      }
    }
  }
}
