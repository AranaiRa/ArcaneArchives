package com.aranaira.enderio.core.client.render;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class IconUtil {

	public interface IIconProvider {

		void registerIcons (@Nonnull TextureMap register);

	}

	public static @Nonnull
	IconUtil instance = new IconUtil();

	public static void addIconProvider (@Nonnull IIconProvider registrar) {
		instance.iconProviders.add(registrar);
	}

	private final @Nonnull
	ArrayList<IIconProvider> iconProviders = new ArrayList<IIconProvider>();

	public @Nonnull
	TextureAtlasSprite whiteTexture;
	public @Nonnull
	TextureAtlasSprite blankTexture;
	public @Nonnull
	TextureAtlasSprite errorTexture;

	private boolean doneInit = false;

	@SuppressWarnings("null")
	private IconUtil () {
	}

	public void init () {
		if (doneInit) {
			return;
		}
		doneInit = true;
		MinecraftForge.EVENT_BUS.register(this);
		addIconProvider(new IIconProvider() {

			@Override
			public void registerIcons (@Nonnull TextureMap register) {
				// TODO: FIX THIS
				whiteTexture = register.registerSprite(new ResourceLocation(ArcaneArchives.MODID, "white"));
				errorTexture = register.registerSprite(new ResourceLocation(ArcaneArchives.MODID, "error"));
				blankTexture = register.registerSprite(new ResourceLocation(ArcaneArchives.MODID, "blank"));
			}

		});
	}

	@SubscribeEvent
	public void onIconLoad (TextureStitchEvent.Pre event) {
		for (IIconProvider reg : iconProviders) {
			final TextureMap map = event.getMap();
			if (map != null) {
				reg.registerIcons(map);
			}
		}
	}

	@SuppressWarnings("null") // don't trust modded models to not do stupid things...
	public static @Nonnull
	TextureAtlasSprite getIconForItem (@Nonnull Item item, int meta) {
		final TextureAtlasSprite icon = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(item, meta);
		return icon != null ? icon : Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
	}

}
