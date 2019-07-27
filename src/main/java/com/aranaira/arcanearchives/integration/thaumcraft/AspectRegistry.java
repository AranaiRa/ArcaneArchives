package com.aranaira.arcanearchives.integration.thaumcraft;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.init.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.AspectRegistryEvent;

@Mod.EventBusSubscriber
public class AspectRegistry {
	@Method(modid = "thaumcraft")
	@SubscribeEvent
	public static void registerAspects (AspectRegistryEvent event) {
		try {
			AspectRegisterHelper helper = new AspectRegisterHelper(event);

			//QUARTZ ETC
			helper.registerTCObjectTag(ItemRegistry.RAW_RADIANT_QUARTZ, new AspectList().add(Aspect.MAGIC, 5).add(Aspect.LIGHT, 5).add(Aspect.CRYSTAL, 5));

			helper.registerTCObjectTag(BlockRegistry.RAW_QUARTZ, new AspectList().add(Aspect.MAGIC, 5).add(Aspect.LIGHT, 5).add(Aspect.CRYSTAL, 5));

			helper.registerTCObjectTag(ItemRegistry.SHAPED_RADIANT_QUARTZ, new AspectList().add(Aspect.MAGIC, 10).add(Aspect.LIGHT, 10).add(Aspect.CRYSTAL, 10).add(Aspect.VOID, 5));

			helper.registerTCObjectTag(BlockRegistry.STORAGE_RAW_QUARTZ, new AspectList().add(Aspect.MAGIC, 45).add(Aspect.LIGHT, 45).add(Aspect.CRYSTAL, 45));

			helper.registerTCObjectTag(BlockRegistry.STORAGE_SHAPED_QUARTZ, new AspectList().add(Aspect.MAGIC, 90).add(Aspect.LIGHT, 90).add(Aspect.CRYSTAL, 90).add(Aspect.VOID, 45));

			helper.registerTCObjectTag(BlockRegistry.QUARTZ_SLIVER, new AspectList().add(Aspect.LIGHT, 1));

			//MANIFEST ETC
			helper.registerTCObjectTag(ItemRegistry.MANIFEST, new AspectList().add(Aspect.ORDER, 20).add(Aspect.MIND, 10).add(Aspect.CRAFT, 5));

			helper.registerTCObjectTag(BlockRegistry.LECTERN_MANIFEST, new AspectList().add(Aspect.ORDER, 30).add(Aspect.MIND, 15).add(Aspect.CRAFT, 8).add(Aspect.PLANT, 5));

			//DEVICES
			helper.registerTCObjectTag(BlockRegistry.RADIANT_CHEST, new AspectList().add(Aspect.VOID, 20).add(Aspect.ORDER, 10).add(Aspect.CRYSTAL, 5));

			helper.registerTCObjectTag(BlockRegistry.RADIANT_TANK, new AspectList().add(Aspect.VOID, 20).add(Aspect.ORDER, 10).add(Aspect.WATER, 10).add(Aspect.CRYSTAL, 5));

			helper.registerTCObjectTag(BlockRegistry.RADIANT_TROVE, new AspectList().add(Aspect.VOID, 30).add(Aspect.ORDER, 20).add(Aspect.DESIRE, 10).add(Aspect.CRYSTAL, 5));

			helper.registerTCObjectTag(BlockRegistry.RADIANT_RESONATOR, new AspectList().add(Aspect.CRAFT, 30).add(Aspect.CRYSTAL, 20).add(Aspect.DESIRE, 20).add(Aspect.WATER, 10).add(Aspect.ENERGY, 10).add(Aspect.PLANT, 5));

			helper.registerTCObjectTag(BlockRegistry.BRAZIER_OF_HOARDING, new AspectList().add(Aspect.FIRE, 30).add(Aspect.MOTION, 20).add(Aspect.EXCHANGE, 15).add(Aspect.DESIRE, 5).add(Aspect.PLANT, 5));

			//COMPONENTS
			helper.registerTCObjectTag(ItemRegistry.COMPONENT_RADIANTDUST, new AspectList().add(Aspect.LIGHT, 5).add(Aspect.ENTROPY, 5));

			helper.registerTCObjectTag(ItemRegistry.COMPONENT_MATERIALINTERFACE, new AspectList().add(Aspect.MIND, 20).add(Aspect.EXCHANGE, 15).add(Aspect.DESIRE, 10));

			helper.registerTCObjectTag(ItemRegistry.COMPONENT_SCINTILLATINGINLAY, new AspectList().add(Aspect.ENERGY, 30).add(Aspect.EXCHANGE, 20).add(Aspect.MECHANISM, 15).add(Aspect.DESIRE, 5));

			helper.registerTCObjectTag(ItemRegistry.COMPONENT_CONTAINMENTFIELD, new AspectList().add(Aspect.PROTECT, 30).add(Aspect.MOTION, 10).add(Aspect.DESIRE, 5));

			helper.registerTCObjectTag(ItemRegistry.COMPONENT_MATRIXBRACE, new AspectList().add(Aspect.TRAP, 30).add(Aspect.FLIGHT, 20).add(Aspect.DESIRE, 5));

			helper.registerTCObjectTag(ItemRegistry.DEVOURING_CHARM, new AspectList().add(Aspect.VOID, 40).add(Aspect.DESIRE, 5));

			helper.registerTCObjectTag(BlockRegistry.MONITORING_CRYSTAL, new AspectList().add(Aspect.SENSES, 30).add(Aspect.ORDER, 15).add(Aspect.CRYSTAL, 10).add(Aspect.LIGHT, 5).add(Aspect.PLANT, 5));

			//CRAFTING STATIONS
			helper.registerTCObjectTag(BlockRegistry.RADIANT_CRAFTING_TABLE, new AspectList().add(Aspect.CRAFT, 40).add(Aspect.ORDER, 15).add(Aspect.EXCHANGE, 10).add(Aspect.LIGHT, 5).add(Aspect.CRYSTAL, 5));

			helper.registerTCObjectTag(BlockRegistry.RADIANT_CRAFTING_TABLE, new AspectList().add(Aspect.CRAFT, 40).add(Aspect.ORDER, 15).add(Aspect.EXCHANGE, 10).add(Aspect.LIGHT, 5).add(Aspect.CRYSTAL, 5));

			helper.registerTCObjectTag(BlockRegistry.GEMCUTTERS_TABLE, new AspectList().add(Aspect.CRAFT, 40).add(Aspect.ORDER, 20).add(Aspect.SENSES, 15).add(Aspect.PLANT, 10).add(Aspect.LIGHT, 5).add(Aspect.CRYSTAL, 5));

			//ITEMS
			helper.registerTCObjectTag(ItemRegistry.LETTER_OF_INVITATION, new AspectList().add(Aspect.MIND, 20).add(Aspect.ORDER, 15).add(Aspect.TOOL, 5));

			helper.registerTCObjectTag(ItemRegistry.LETTER_OF_RESIGNATION, new AspectList().add(Aspect.MIND, 20).add(Aspect.ORDER, 15).add(Aspect.ENTROPY, 5));

			helper.registerTCObjectTag(ItemRegistry.WRIT_OF_EXPULSION, new AspectList().add(Aspect.MIND, 20).add(Aspect.ORDER, 15).add(Aspect.DEATH, 5));

			helper.registerTCObjectTag(ItemRegistry.RADIANT_AMPHORA, new AspectList().add(Aspect.WATER, 40).add(Aspect.MOTION, 20).add(Aspect.EXCHANGE, 20).add(Aspect.DESIRE, 5).add(Aspect.EARTH, 5));

			helper.registerTCObjectTag(ItemRegistry.SCEPTER_REVELATION, new AspectList().add(Aspect.MIND, 25).add(Aspect.ORDER, 15).add(Aspect.PLANT, 10));

			helper.registerTCObjectTag(ItemRegistry.SCEPTER_MANIPULATION, new AspectList().add(Aspect.EXCHANGE, 25).add(Aspect.ORDER, 15).add(Aspect.PLANT, 10));

			helper.registerTCObjectTag(ItemRegistry.BAUBLE_GEMSOCKET, new AspectList().add(Aspect.MAGIC, 10).add(Aspect.CRYSTAL, 5).add(Aspect.DESIRE, 5).add(Aspect.BEAST, 5));

			helper.registerTCObjectTag(ItemRegistry.TOME_OF_ARCANA, new AspectList().add(Aspect.MIND, 25).add(Aspect.MAGIC, 5));

			//DECORATIVE
			helper.registerTCObjectTag(BlockRegistry.RADIANT_LANTERN, new AspectList().add(Aspect.LIGHT, 10).add(Aspect.CRYSTAL, 5).add(Aspect.DESIRE, 5));

			//ARCANE GEMS
			helper.registerTCObjectTag(ItemRegistry.SLAUGHTERGLEAM, new AspectList().add(Aspect.MAGIC, 30).add(Aspect.CRYSTAL, 30).add(Aspect.DESIRE, 30));

			helper.registerTCObjectTag(ItemRegistry.MURDERGLEAM, new AspectList().add(Aspect.MAGIC, 30).add(Aspect.CRYSTAL, 30).add(Aspect.DEATH, 30));

			helper.registerTCObjectTag(ItemRegistry.AGEGLEAM, new AspectList().add(Aspect.MAGIC, 30).add(Aspect.CRYSTAL, 30).add(Aspect.BEAST, 30));

			helper.registerTCObjectTag(ItemRegistry.CLEANSEGLEAM, new AspectList().add(Aspect.MAGIC, 30).add(Aspect.CRYSTAL, 30).add(Aspect.LIFE, 30));

			helper.registerTCObjectTag(ItemRegistry.SWITCHGLEAM, new AspectList().add(Aspect.MAGIC, 30).add(Aspect.CRYSTAL, 30).add(Aspect.ELDRITCH, 30));

			helper.registerTCObjectTag(ItemRegistry.SALVEGLEAM, new AspectList().add(Aspect.MAGIC, 30).add(Aspect.CRYSTAL, 30).add(Aspect.LIFE, 30));

			helper.registerTCObjectTag(ItemRegistry.ORDERSTONE, new AspectList().add(Aspect.MAGIC, 30).add(Aspect.CRYSTAL, 30).add(Aspect.ORDER, 30));

			helper.registerTCObjectTag(ItemRegistry.MUNCHSTONE, new AspectList().add(Aspect.MAGIC, 30).add(Aspect.CRYSTAL, 30).add(Aspect.ENTROPY, 30));

			helper.registerTCObjectTag(ItemRegistry.MINDSPINDLE, new AspectList().add(Aspect.MAGIC, 30).add(Aspect.CRYSTAL, 30).add(Aspect.MIND, 30));

			helper.registerTCObjectTag(ItemRegistry.ELIXIRSPINDLE, new AspectList().add(Aspect.MAGIC, 30).add(Aspect.CRYSTAL, 30).add(Aspect.ALCHEMY, 30));

			helper.registerTCObjectTag(ItemRegistry.MOUNTAINTEAR, new AspectList().add(Aspect.MAGIC, 30).add(Aspect.CRYSTAL, 30).add(Aspect.FIRE, 30));

			helper.registerTCObjectTag(ItemRegistry.RIVERTEAR, new AspectList().add(Aspect.MAGIC, 30).add(Aspect.CRYSTAL, 30).add(Aspect.WATER, 30));

			helper.registerTCObjectTag(ItemRegistry.PARCHTEAR, new AspectList().add(Aspect.MAGIC, 30).add(Aspect.CRYSTAL, 30).add(Aspect.VOID, 30));

			helper.registerTCObjectTag(ItemRegistry.PHOENIXWAY, new AspectList().add(Aspect.MAGIC, 30).add(Aspect.CRYSTAL, 30).add(Aspect.FIRE, 30));

			helper.registerTCObjectTag(ItemRegistry.STORMWAY, new AspectList().add(Aspect.MAGIC, 30).add(Aspect.CRYSTAL, 30).add(Aspect.ENERGY, 30));

			//GEM RECHARGE POWDER
			helper.registerTCObjectTag(ItemRegistry.CHROMATIC_POWDER, new AspectList().add(Aspect.ENERGY, 20).add(Aspect.SENSES, 10).add(Aspect.AURA, 5));

			helper.registerTCObjectTag(ItemRegistry.RAINBOW_CHROMATIC_POWDER, new AspectList().add(Aspect.ENERGY, 180).add(Aspect.SENSES, 90).add(Aspect.AURA, 45));

		} catch (Exception e) {
			ArcaneArchives.logger.info("&&&&&&& Error registering Thaumcraft aspects: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	//Copied from Twilight Forest
	private static class AspectRegisterHelper {

		private final AspectRegistryEvent event;

		private AspectRegisterHelper (AspectRegistryEvent event) {
			this.event = event;
		}

		private void registerTCObjectTag (Block block, AspectList list) {
			registerTCObjectTag(new ItemStack(block), list);
		}

		// Register a block with Thaumcraft aspects
		private void registerTCObjectTag (Block block, int meta, AspectList list) {
			if (meta == -1) {
				meta = OreDictionary.WILDCARD_VALUE;
			}
			registerTCObjectTag(new ItemStack(block, 1, meta), list);
		}

		// Register blocks with Thaumcraft aspects
		private void registerTCObjectTag (Block block, int[] metas, AspectList list) {
			for (int meta : metas) {
				this.registerTCObjectTag(block, meta, list);
			}
		}

		private void registerTCObjectTag (Item item, AspectList list) {
			registerTCObjectTag(new ItemStack(item), list);
		}

		// Register an item with Thaumcraft aspects
		private void registerTCObjectTag (Item item, int meta, AspectList list) {
			if (meta == -1) {
				meta = OreDictionary.WILDCARD_VALUE;
			}
			registerTCObjectTag(new ItemStack(item, 1, meta), list);
		}

		// Register item swith Thaumcraft aspects
		private void registerTCObjectTag (Item item, int[] metas, AspectList list) {
			for (int meta : metas) {
				this.registerTCObjectTag(item, meta, list);
			}
		}

		private void registerTCObjectTag (ItemStack stack, AspectList list) {
			event.register.registerObjectTag(stack, list);
		}
	}
}
