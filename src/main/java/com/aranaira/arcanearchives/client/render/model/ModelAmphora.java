package com.aranaira.arcanearchives.client.render.model;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.items.RadiantAmphoraItem.AmphoraUtil;
import com.aranaira.arcanearchives.items.RadiantAmphoraItem.TankMode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class ModelAmphora implements IModel {
	public static final ModelResourceLocation RESOURCE = new ModelResourceLocation(new ResourceLocation(ArcaneArchives.MODID, "radiant_amphora"), "inventory");

	// minimal Z offset to prevent depth-fighting
	private static final float NORTH_Z_FLUID = 7.498f / 16f;
	private static final float SOUTH_Z_FLUID = 8.502f / 16f;

	public static final IModel MODEL = new ModelAmphora();

	@Nullable
	private final ResourceLocation baseLocation;
	@Nullable
	private final ResourceLocation liquidLocation;
	@Nullable
	private final ResourceLocation baseLocationRotated;
	@Nullable
	private final ResourceLocation liquidLocationRotated;
	@Nullable
	private final ResourceLocation unlinkedLocation;
	@Nullable
	private final Fluid fluid;

	private boolean rotated;
	private boolean unlinked;

	public ModelAmphora () {
		this(null, null, null, null, null, null, false, false);
	}

	public ModelAmphora (@Nullable ResourceLocation baseLocation, @Nullable ResourceLocation liquidLocation, @Nullable ResourceLocation baseLocationRotated, @Nullable ResourceLocation liquidLocationRotated, @Nullable ResourceLocation unlinkedLocation, @Nullable Fluid fluid, boolean rotated, boolean unlinked) {
		this.baseLocation = baseLocation;
		this.liquidLocation = liquidLocation;
		this.baseLocationRotated = baseLocationRotated;
		this.liquidLocationRotated = liquidLocationRotated;
		this.unlinkedLocation = unlinkedLocation;
		this.fluid = fluid;
		this.rotated = rotated;
		this.unlinked = unlinked;
	}

	@Override
	public Collection<ResourceLocation> getTextures () {
		ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();

		if (baseLocation != null) {
			builder.add(baseLocation);
		}
		if (liquidLocation != null) {
			builder.add(liquidLocation);
		}
		if (baseLocationRotated != null) {
			builder.add(baseLocationRotated);
		}
		if (liquidLocationRotated != null) {
			builder.add(liquidLocationRotated);
		}
		if (unlinkedLocation != null) {
			builder.add(unlinkedLocation);
		}
		if (fluid != null) {
			builder.add(fluid.getStill());
		}

		return builder.build();
	}

	@Override
	public IBakedModel bake (IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {

		ImmutableMap<TransformType, TRSRTransformation> transformMap = PerspectiveMapWrapper.getTransforms(state);

		TRSRTransformation transform = state.apply(Optional.empty()).orElse(TRSRTransformation.identity());
		TextureAtlasSprite fluidSprite = null;
		TextureAtlasSprite particleSprite = null;
		ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

		if (fluid != null) {
			fluidSprite = bakedTextureGetter.apply(fluid.getStill());
		}

		if (baseLocation != null && baseLocationRotated != null && unlinkedLocation != null) {
			IBakedModel model;
			if (!unlinked) {
				if (rotated) {
					model = new ItemLayerModel(ImmutableList.of(baseLocationRotated)).bake(state, format, bakedTextureGetter);
				} else {
					model = new ItemLayerModel(ImmutableList.of(baseLocation)).bake(state, format, bakedTextureGetter);
				}
			} else {
				model = new ItemLayerModel(ImmutableList.of(unlinkedLocation)).bake(state, format, bakedTextureGetter);
			}
			builder.addAll(model.getQuads(null, null, 0));
			particleSprite = model.getParticleTexture();
		}
		if (liquidLocation != null && liquidLocationRotated != null && fluidSprite != null && !unlinked) {
			TextureAtlasSprite liquid = bakedTextureGetter.apply(rotated ? liquidLocationRotated : liquidLocation);
			builder.addAll(ItemTextureQuadConverter.convertTexture(format, transform, liquid, fluidSprite, NORTH_Z_FLUID, EnumFacing.NORTH, 0xFFFFFFFF, 1));
			builder.addAll(ItemTextureQuadConverter.convertTexture(format, transform, liquid, fluidSprite, SOUTH_Z_FLUID, EnumFacing.SOUTH, 0xFFFFFFFF, 1));
			particleSprite = fluidSprite;
		}

		return new BakedAmphora(this, builder.build(), particleSprite, format, Maps.immutableEnumMap(transformMap), Maps.newHashMap(), transform.isIdentity());
	}

	/**
	 * Sets the fluid in the model.
	 * "fluid" - Name of the fluid in the fluid registry.
	 * "rotated" - If true, the amphora is rotated.
	 * <p/>
	 * If the fluid can't be found, water is used.
	 */
	@Override
	public ModelAmphora process (ImmutableMap<String, String> customData) {
		String fluidName = customData.get("fluid");
		Fluid fluid = FluidRegistry.getFluid(fluidName);

		if (fluid == null) {
			fluid = this.fluid;
		}

		String rotated = customData.get("rotated");
		boolean doRotate = false;
		if (rotated != null && rotated.equalsIgnoreCase("true")) {
			doRotate = true;
		}

		String unlinked = customData.get("unlinked");
		boolean isUnlinked = true;
		if (unlinked != null && unlinked.equalsIgnoreCase("false")) {
			isUnlinked = false;
		}

		// create new model with correct liquid
		return new ModelAmphora(baseLocation, liquidLocation, baseLocationRotated, liquidLocationRotated, unlinkedLocation, fluid, doRotate, isUnlinked);
	}

	/**
	 * Allows to use different textures for the model.
	 * There are 3 layers:
	 * base - The empty bucket/container
	 * fluid - A texture representing the liquid portion. Non-transparent = liquid
	 * cover - An overlay that's put over the liquid (optional)
	 * <p/>
	 * If no liquid is given a hardcoded variant for the bucket is used.
	 */
	@Override
	public ModelAmphora retexture (ImmutableMap<String, String> textures) {

		ResourceLocation base = baseLocation;
		ResourceLocation liquid = liquidLocation;
		ResourceLocation baseRotated = baseLocationRotated;
		ResourceLocation liquidRotated = liquidLocationRotated;
		ResourceLocation unlinked = unlinkedLocation;

		if (textures.containsKey("base")) {
			base = new ResourceLocation(textures.get("base"));
		}
		if (textures.containsKey("fluid")) {
			liquid = new ResourceLocation(textures.get("fluid"));
		}
		if (textures.containsKey("baseRotated")) {
			baseRotated = new ResourceLocation(textures.get("baseRotated"));
		}
		if (textures.containsKey("liquidRotated")) {
			liquidRotated = new ResourceLocation(textures.get("liquidRotated"));
		}
		if (textures.containsKey("unlinked")) {
			unlinked = new ResourceLocation(textures.get("unlinked"));
		}

		return new ModelAmphora(base, liquid, baseRotated, liquidRotated, unlinked, fluid, this.rotated, this.unlinked);
	}

	public enum LoaderAmphora implements ICustomModelLoader {
		INSTANCE;

		@Override
		public boolean accepts (ResourceLocation modelLocation) {
			if (!modelLocation.getNamespace().equals("arcanearchives")) return false;

			if (!modelLocation.getPath().equals("radiant_amphora")) return false;

			return true;
		}

		@Override
		public IModel loadModel (ResourceLocation modelLocation) {
			return MODEL;
		}

		@Override
		public void onResourceManagerReload (IResourceManager resourceManager) {
			// no need to clear cache since we create a new model instance
		}
	}

	private static final class BakedAmphoraOverrideHandler extends ItemOverrideList {
		public static final BakedAmphoraOverrideHandler INSTANCE = new BakedAmphoraOverrideHandler();

		private BakedAmphoraOverrideHandler () {
			super(ImmutableList.of());
		}

		@Override
		public IBakedModel handleItemState (IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
			AmphoraUtil util = new AmphoraUtil(stack);
			boolean linked = util.isLinked();
			boolean rotated = util.getMode() == TankMode.DRAIN;
			Fluid fluid = util.getFluid();

			BakedAmphora model = (BakedAmphora) originalModel;

			String fluidName = (fluid == null) ? "nofluid" : fluid.getName();
			String name = (fluid == null) ? "unlinked" : fluid.getName() + "-" + (rotated ? "rotated" : "upright");

			if (!model.cache.containsKey(name)) {
				IModel parent = model.parent.process(ImmutableMap.of("fluid", fluidName, "unlinked", (linked) ? "false" : "true", "rotated", (rotated) ? "false" : "true"));
				Function<ResourceLocation, TextureAtlasSprite> textureGetter;
				textureGetter = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());

				IBakedModel bakedModel = parent.bake(new SimpleModelState(model.getTransforms()), model.format, textureGetter);
				model.cache.put(name, bakedModel);
				return bakedModel;
			}

			return model.cache.get(name);
		}
	}

	// the dynamic bucket is based on the empty bucket
	private static final class BakedAmphora extends BakedItemModel {
		private final ModelAmphora parent;
		private final Map<String, IBakedModel> cache; // contains all the baked models since they'll never change
		private final VertexFormat format;

		BakedAmphora (
				ModelAmphora parent, ImmutableList<BakedQuad> quads, TextureAtlasSprite particle, VertexFormat format, ImmutableMap<TransformType, TRSRTransformation> transforms, Map<String, IBakedModel> cache, boolean untransformed) {
			super(quads, particle, transforms, BakedAmphoraOverrideHandler.INSTANCE, untransformed);
			this.format = format;
			this.parent = parent;
			this.cache = cache;
		}

		public ImmutableMap<TransformType, TRSRTransformation> getTransforms () {
			return transforms;
		}
	}

}
