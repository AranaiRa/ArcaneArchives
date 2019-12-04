package com.aranaira.arcanearchives.proxy;

import com.aranaira.arcanearchives.AAGuiHandler;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.commands.*;
import com.aranaira.arcanearchives.data.DataHelper;
import com.aranaira.arcanearchives.entity.EntityWeight;
import com.aranaira.arcanearchives.events.ClientTickHandler;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.init.RecipeLibrary;
import com.aranaira.arcanearchives.integration.craftingtweaks.CraftingTweaks;
import com.aranaira.arcanearchives.network.Networking;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

public class CommonProxy {
	public void preInit (FMLPreInitializationEvent event) {
		ArcaneArchives.logger = event.getModLog();
		NetworkRegistry.INSTANCE.registerGuiHandler(ArcaneArchives.instance, new AAGuiHandler());

		BlockRegistry.init();
		ItemRegistry.init();

		Networking.registerPackets();
		EntityRegistry.registerModEntity(new ResourceLocation(ArcaneArchives.MODID, "weight"), EntityWeight.class, "weight", 0, ArcaneArchives.instance, 64, 10, false);
	}

	public void init (FMLInitializationEvent event) {
		RecipeLibrary.buildRecipes();
		BlockRegistry.registerTileEntities();
		CraftingTweaks.init();

		FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "com.aranaira.arcanearchives.integration.top.TOPPlugin");
	}

	public void postInit (FMLPostInitializationEvent event) {
	}

	public void registerItemRenderer (Item item, int meta, String id) {
	}

	public void registerBlockRenderer (Block block, int meta, String id) {
	}

	public void serverStarting (FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandHive());
		event.registerServerCommand(new CommandBrazier());
		event.registerServerCommand(new CommandCopy());
		event.registerServerCommand(new CommandTiles());
		event.registerServerCommand(new CommandRebuild());
	}

	public void serverStarted (FMLServerStartedEvent event) {
		DataHelper.clearClientCache(); // has no effect on the server
	}

	public void loadComplete (FMLLoadCompleteEvent event) {
		// Ensure Bookshelf has an ore dictionary entry
		OreDictionary.registerOre("bookshelf", Blocks.BOOKSHELF);
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
