package com.aranaira.arcanearchives.events.mappings;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.ItemRegistry;
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
			if (entry.key.getNamespace().equals(ArcaneArchives.MODID)) {
				entry.remap(lookupItem(entry.key));
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
			case "item_upgrade_devouringcharm":
				return ItemRegistry.DEVOURING_CHARM;
			case "item_fabrial":
				return ItemRegistry.FABRIAL;
			case "item_murdergleam":
				return ItemRegistry.LETTER_OF_INVITATION;
			case "item_munchstone":
				return ItemRegistry.LETTER_OF_RESIGNATION;
			case "item_mindspindle":
				return ItemRegistry.MANIFEST;
			case "item_mountaintear":
				return ItemRegistry.MINDSPINDLE;
			case "item_rivertear":
				return ItemRegistry.MOUNTAINTEAR;
			case "item_phoenixway":
				return ItemRegistry.MUNCHSTONE;
			case "item_letterofinvitation":
				return ItemRegistry.MURDERGLEAM;
			case "item_letterofresignation":
				return ItemRegistry.PHOENIXWAY;
			case "item_manifest":
				return ItemRegistry.RADIANT_AMPHORA;
			case "item_radiantamphora":
				return ItemRegistry.RAW_RADIANT_QUARTZ;
			case "item_rawquartz":
				return ItemRegistry.RIVERTEAR;
			case "item_sceptermanipulation":
				return ItemRegistry.SCEPTER_MANIPULATION;
			case "item_scepterrevelation":
				return ItemRegistry.SCEPTER_REVELATION;
			case "item_cutquartz":
				return ItemRegistry.CUT_RADIANT_QUARTZ;
			case "item_tomeofarcana":
				return ItemRegistry.TOME_OF_ARCANA;
			case "item_writofexpulsion":
				return ItemRegistry.WRIT_OF_EXPULSION;
			default:
				ArcaneArchives.logger.error("#############################################################");
				ArcaneArchives.logger.error("Unable to handle missing mapping for ResourceLocation " + resource.toString() + " and it has been replaced with air.");
				ArcaneArchives.logger.error("#############################################################");
				return Items.AIR;
		}
	}
}
