package com.aranaira.arcanearchives.setup;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ForgeBlockStateV1.Transforms;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Map;
import java.util.Set;

public class ClientSetup {
  private static final TRSRTransformation THIRD_PERSON_BLOCK = Transforms.convert(0, 2.5f, 0, 75, 45, 0, 0.375f);
  private static final ImmutableMap<TransformType, TRSRTransformation> BLOCK_TRANSFORMS = ImmutableMap.<TransformType, TRSRTransformation>builder()
      .put(TransformType.GUI, Transforms.convert(0, 0, 0, 30, 225, 0, 0.625f))
      .put(TransformType.GROUND, Transforms.convert(0, 3, 0, 0, 0, 0, 0.25f))
      .put(TransformType.FIXED, Transforms.convert(0, 0, 0, 0, 0, 0, 0.5f))
      .put(TransformType.THIRD_PERSON_RIGHT_HAND, THIRD_PERSON_BLOCK)
      .put(TransformType.THIRD_PERSON_LEFT_HAND, Transforms.leftify(THIRD_PERSON_BLOCK))
      .put(TransformType.FIRST_PERSON_RIGHT_HAND, Transforms.convert(0, 0, 0, 0, 45, 0, 0.4f))
      .put(TransformType.FIRST_PERSON_LEFT_HAND, Transforms.convert(0, 0, 0, 0, 225, 0, 0.4f))
      .build();

  public static final Set<ResourceLocation> MODELS = Sets.newHashSet(
      new ResourceLocation(ArcaneArchives.MODID, "radiant_resonator")
  );

  public static void init(FMLClientSetupEvent event) {
    OBJLoader.INSTANCE.addDomain("arcanearchives");
  }

  public static void modelBake(ModelBakeEvent event) {
    Map<ResourceLocation, IBakedModel> registry = event.getModelRegistry();
    for (ResourceLocation ml : MODELS) {
      IBakedModel baked = registry.get(new ModelResourceLocation(ml, ""));
      if (baked != null) {
        IBakedModel inventory = new PerspectiveMapWrapper(baked, BLOCK_TRANSFORMS);
        registry.put(new ModelResourceLocation(ml, "inventory"), inventory);
      }
    }
    IBakedModel baked = registry.get(new ModelResourceLocation(new ResourceLocation(ArcaneArchives.MODID, "quartz_cluster"), "facing=up"));
    IBakedModel inventory = new PerspectiveMapWrapper(baked, BLOCK_TRANSFORMS);
    registry.put(new ModelResourceLocation(new ResourceLocation(ArcaneArchives.MODID, "quartz_cluster"), "inventory"), inventory);
  }
}
