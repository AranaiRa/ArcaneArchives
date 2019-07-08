package com.aranaira.arcanearchives.proxy;

import com.aranaira.arcanearchives.AAGuiHandler;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.advancements.Advancements;
import com.aranaira.arcanearchives.commands.CommandBrazier;
import com.aranaira.arcanearchives.commands.CommandHive;
import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.events.ClientTickHandler;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.init.RecipeLibrary;
import com.aranaira.arcanearchives.items.gems.oval.MunchstoneItem;
import com.aranaira.arcanearchives.network.NetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

public class CommonProxy {
	public void preInit (FMLPreInitializationEvent event) {
		ArcaneArchives.logger = event.getModLog();
		NetworkRegistry.INSTANCE.registerGuiHandler(ArcaneArchives.instance, new AAGuiHandler());

		NetworkHandler.registerPackets();
	}

	public void init (FMLInitializationEvent event) {
		RecipeLibrary.buildRecipes();
		BlockRegistry.registerTileEntities();

		FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "com.aranaira.arcanearchives.integration.top.TOPPlugin");
	}

	public void postInit (FMLPostInitializationEvent event) { }

	public void registerItemRenderer (Item item, int meta, String id) {
	}

	public void registerBlockRenderer (Block block, int meta, String id) {
	}

	public void serverStarting (FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandHive());
		event.registerServerCommand(new CommandBrazier());
	}

	public void serverStarted (FMLServerStartedEvent event) {
		NetworkHelper.clearClientCache(); // has no effect on the server
	}

	public void loadComplete (FMLLoadCompleteEvent event) {
		// Ensure Bookshelf has an ore dictionary entry
		OreDictionary.registerOre("bookshelf", Blocks.BOOKSHELF);

		// Set up advancements
		Advancements.init();
	}

	public void scheduleTask (Runnable runnable, Side side) {
		scheduleTask(runnable, 0, side);
	}

	public void scheduleTask (Runnable runnable, int delay, Side side) {
		switch (side) {
			case CLIENT:
				ClientTickHandler.addRunnable(runnable, delay);
				break;
			case SERVER:
				FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(runnable);
				break;
		}
	}
}
