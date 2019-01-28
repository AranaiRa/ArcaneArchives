package com.aranaira.arcanearchives.util.handlers;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.BlockTemplate;
import com.aranaira.arcanearchives.init.BlockLibrary;
import com.aranaira.arcanearchives.init.ItemLibrary;
import com.aranaira.arcanearchives.tileentities.*;
import com.aranaira.arcanearchives.util.IHasModel;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import scala.annotation.meta.field;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
			if(block instanceof BlockTemplate) if(((BlockTemplate) block).hasOBJModel())
			{
				ArcaneArchives.logger.info("&&&&&&&& Setting up " + block.getRegistryName() + " with OBJ model");
			} else
			{
				ArcaneArchives.logger.info("&&&&&&&& Setting up " + block.getRegistryName());
				// todo: null check
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
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
		List<TileEntity> entities = Arrays.asList(
				BlockLibrary.RADIANT_RESONATOR_TILE_ENTITY,
				BlockLibrary.MATRIX_CORE_TILE_ENTITY,
				BlockLibrary.MATRIX_REPOSITORY_TILE_ENTITY,
				BlockLibrary.ACCESSOR_TILE_ENTITY,
				BlockLibrary.RADIANT_CHEST_TILE_ENTITY,
				BlockLibrary.GEMCUTTERS_TABLE_TILE_ENTITY,
				BlockLibrary.RADIANT_CRAFTING_TABLE_TILE_ENTITY,
				BlockLibrary.MATRIX_STORAGE_TILE_ENTITY
		);

		entities.forEach((f) -> {
			AATileEntity entity = (AATileEntity) f;
			GameRegistry.registerTileEntity(f.getClass(), new ResourceLocation(ArcaneArchives.MODID, entity.getName()));
			ArcaneArchives.logger.info(String.format("Registered tile entity: %s", entity.getName()));

		});
	}

	//@SubscribeEvent
	//public static void onGUIRegister(GUIRegistryEvent event)
	//{
	//	
	//}
}
