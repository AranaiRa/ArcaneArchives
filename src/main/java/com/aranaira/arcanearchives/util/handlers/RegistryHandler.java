package com.aranaira.arcanearchives.util.handlers;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.init.BlockLibrary;
import com.aranaira.arcanearchives.init.ItemLibrary;
import com.aranaira.arcanearchives.items.templates.ItemMultistateTemplate;
import com.aranaira.arcanearchives.tileentities.AATileEntity;
import com.aranaira.arcanearchives.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class RegistryHandler
{
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(ItemLibrary.ITEMS.toArray(new Item[0]));
	}

	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(BlockLibrary.BLOCKS.toArray(new Block[0]));
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		OBJLoader.INSTANCE.addDomain(ArcaneArchives.MODID.toLowerCase());

		for(Block block : BlockLibrary.BLOCKS)
		{
			if(block instanceof BlockTemplate && ((BlockTemplate) block).hasOBJModel()) {
				// ArcaneArchives.logger.info("&&&&&&&& Setting up " + block.getRegistryName() + " with OBJ model");
			} else {
				// ArcaneArchives.logger.info("&&&&&&&& Setting up " + block.getRegistryName());
				// todo: null everything
				if (Item.getItemFromBlock(block) != Items.AIR) {
					ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
				}
			}
		}
	}

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event)
	{
		for(Item item : ItemLibrary.ITEMS)
		{
			if(item instanceof ItemMultistateTemplate)
			{
				((ItemMultistateTemplate) item).preInit();
			}
			if(item instanceof IHasModel)
			{
				((IHasModel) item).registerModels();
			}
		}

		for(Block block : BlockLibrary.BLOCKS)
		{
			if(block instanceof IHasModel)
			{
				((IHasModel) block).registerModels();
			}
		}
	}

	public static void registerTileEntities()
	{
		for(AATileEntity tile : BlockLibrary.TILES)
		{
			GameRegistry.registerTileEntity(tile.getClass(), new ResourceLocation(ArcaneArchives.MODID, tile.getName()));
			ArcaneArchives.logger.info(String.format("Registered tile entity: %s", tile.getName()));
		}
	}

	//@SubscribeEvent
	//public static void onGUIRegister(GUIRegistryEvent event)
	//{
	//	
	//}
}
