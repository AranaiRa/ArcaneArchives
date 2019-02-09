package com.aranaira.arcanearchives.proxy;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.common.AAGuiHandler;
import com.aranaira.arcanearchives.data.AAWorldSavedData;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.init.RecipeLibrary;
import com.aranaira.arcanearchives.packets.AAPacketHandler;
import com.aranaira.arcanearchives.util.handlers.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy
{
	public void preInit(FMLPreInitializationEvent event)
	{
		ArcaneArchives.logger = event.getModLog();
		NetworkRegistry.INSTANCE.registerGuiHandler(ArcaneArchives.instance, new AAGuiHandler());

		AAPacketHandler.registerPackets();
	}

	public void init(FMLInitializationEvent event)
	{
		RecipeLibrary.RegisterGCTRecipes();
		RegistryHandler.registerTileEntities();
	}

	public void postInit(FMLPostInitializationEvent event)
	{
	}

	public void registerItemRenderer(Item item, int meta, String id)
	{
	}

	public void registerBlockRenderer(Block block, int meta, String id)
	{
	}

	public void serverStarted (FMLServerStartedEvent event) {
		NetworkHelper.clearClientCache(); // has no effect on the server
		NetworkHelper.clearServerCache(); // has no effect on the client
	}
}
