package com.aranaira.arcanearchives.events.mappings;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.init.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.MissingMappings;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Mapping;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class MappingHandler {
	@SubscribeEvent
	public static void onItemMappingsEvent (MissingMappings<Item> event) {
		for (Mapping<Item> entry : event.getAllMappings()) {
			Item item = null;
			if (entry.key.getNamespace().equals(ArcaneArchives.MODID)) {
				item = lookupItem(entry.key);
			} else if (entry.key.getNamespace().equals("gbook") && entry.key.getPath().equals("guidebook")) {
				item = Item.REGISTRY.getObject(new ResourceLocation("gbook_snapshot:guidebook"));
			}
			if (item != null) {
				entry.remap(item);
			}
		}
	}

	@SubscribeEvent
	public static void onBlockMappingsEvent (MissingMappings<Block> event) {
		for (Mapping<Block> entry : event.getAllMappings()) {
			if (entry.key.getNamespace().equals(ArcaneArchives.MODID) && entry.key.getPath().equals("raw_quartz")) {
				entry.remap(BlockRegistry.RAW_QUARTZ);
			}
			if (entry.key.getNamespace().equals(ArcaneArchives.MODID) && entry.key.getPath().equals("storage_cut_quartz")) {
				entry.remap(BlockRegistry.STORAGE_SHAPED_QUARTZ);
			}
		}
	}

	public static Item lookupItem (ResourceLocation resource) {
		switch (resource.getPath()) {
			case "bauble_gemsocket":
				return ItemRegistry.BAUBLE_GEMSOCKET;
			case "item_component_containmentfield":
				return ItemRegistry.COMPONENT_CONTAINMENTFIELD;
			case "item_component_materialinterface":
				return ItemRegistry.COMPONENT_MATERIALINTERFACE;
			case "item_component_matrixbrace":
				return ItemRegistry.COMPONENT_MATRIXBRACE;
			case "item_component_radiantdust":
				return ItemRegistry.COMPONENT_RADIANTDUST;
			case "item_component_scintillatinginlay":
				return ItemRegistry.COMPONENT_SCINTILLATINGINLAY;
			case "item_murdergleam":
				return ItemRegistry.MURDERGLEAM;
			case "item_munchstone":
				return ItemRegistry.MUNCHSTONE;
			case "item_mindspindle":
				return ItemRegistry.MINDSPINDLE;
			case "item_mountaintear":
				return ItemRegistry.MOUNTAINTEAR;
			case "item_rivertear":
				return ItemRegistry.RIVERTEAR;
			case "item_phoenixway":
				return ItemRegistry.PHOENIXWAY;
			case "item_letterofinvitation":
				return ItemRegistry.LETTER_OF_INVITATION;
			case "item_letterofresignation":
				return ItemRegistry.LETTER_OF_RESIGNATION;
			case "item_manifest":
				return ItemRegistry.MANIFEST;
			case "item_rawquartz":
				return ItemRegistry.RAW_RADIANT_QUARTZ;
			case "item_sceptermanipulation":
				return ItemRegistry.SCEPTER_MANIPULATION;
			case "item_scepterrevelation":
				return ItemRegistry.SCEPTER_REVELATION;
			case "item_cutquartz":
				return ItemRegistry.SHAPED_RADIANT_QUARTZ;
			case "item_tomeofarcana":
				return ItemRegistry.TOME_OF_ARCANA;
			case "item_writofexpulsion":
				return ItemRegistry.WRIT_OF_EXPULSION;
			default:
				ArcaneArchives.logger.error("#############################################################");
				ArcaneArchives.logger.error("Unable to handle missing mapping for ResourceLocation " + resource.toString() + " and it has not been replaced.");
				ArcaneArchives.logger.error("#############################################################");
				return null;
		}
	}
}
