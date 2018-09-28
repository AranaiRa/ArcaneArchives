package com.aranaira.arcanearchives.util.handlers;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.BlockTemplate;
import com.aranaira.arcanearchives.init.BlockLibrary;
import com.aranaira.arcanearchives.init.ItemLibrary;
import com.aranaira.arcanearchives.util.IHasModel;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = ArcaneArchives.MODID)
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
			if(((BlockTemplate)block).hasOBJModel())
			{
		    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ResourceLocation(ArcaneArchives.MODID, block.getRegistryName()+".obj"), "inventory"));
		    	ArcaneArchives.logger.info("&&&&&&&& Setting up " + block.getRegistryName() + " with OBJ model &&&&&&&");
			}
			else
			{
				ArcaneArchives.logger.info("&&&&&&&& Setting up " + block.getRegistryName() + " &&&&&&&");
			}
				
		}
    }
	
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event)
	{
		for(Item item : ItemLibrary.ITEMS)
		{
			if(item instanceof IHasModel)
			{
				((IHasModel)item).registerModels();
			}
		}
		
		for(Block block : BlockLibrary.BLOCKS)
		{
			if(block instanceof IHasModel)
			{
				((IHasModel)block).registerModels();
			}
		}
	}
}
