package com.aranaira.arcanearchives.client.model;

import java.util.List;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.OBJBakedModel;
import net.minecraftforge.common.model.IModelState;

/**
 * Originally created by 2piradians for the mod Minewatch; modified for AA by Aranai
 * @author 2piradians
 */
public class BakedEntityModel extends OBJBakedModel
{
	public EntityLivingBase entity;
	@Nullable
	private TextureAtlasSprite particleTexture; //probably won't need this
	
	public BakedEntityModel(OBJModel model, IModelState state, VertexFormat format, ImmutableMap<String, TextureAtlasSprite> textures)
	{
		model.super(model, state, format, textures);
	}
	
	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType type)
	{
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		
		Pair<? extends IBakedModel, Matrix4f> ret = super.handlePerspective(type);
		
		return ret;
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState blockState, EnumFacing side, long rand)
	{
		//Set tint index for quads
		List<BakedQuad> ret = super.getQuads(blockState, side, rand);
		
		return ret;
	}
}
