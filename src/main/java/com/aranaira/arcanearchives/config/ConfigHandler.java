package com.aranaira.arcanearchives.config;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.client.gui.GUIManifest;
import com.aranaira.arcanearchives.items.gems.oval.MunchstoneItem;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.PacketConfig.DefaultRoutingType;
import com.aranaira.arcanearchives.network.PacketConfig.MaxDistance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@Config.LangKey("arcanearchives.config.general")
@Config(modid = ArcaneArchives.MODID)
@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ConfigHandler {
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onConfigChanged (ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(ArcaneArchives.MODID)) {
			ConfigManager.sync(ArcaneArchives.MODID, Config.Type.INSTANCE);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	@SideOnly(Side.CLIENT)
	public static void onClientConfigChanged (ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(ArcaneArchives.MODID)) {
			MaxDistance packet = new MaxDistance(ManifestConfig.MaxDistance);
			Networking.CHANNEL.sendToServer(packet);

			DefaultRoutingType packet2 = new DefaultRoutingType(defaultRoutingNoNewItems);
			Networking.CHANNEL.sendToServer(packet2);

			Minecraft minecraft = Minecraft.getMinecraft();
			if (minecraft.currentScreen instanceof GUIManifest) {
				((GUIManifest) minecraft.currentScreen).doRefresh();
			}
		}
	}

	@Config.Comment("Limit of resonators per player's network")
	@Config.Name("Resonator Limit")
	public static int ResonatorLimit = 3;

	@Config.Comment("Number of ticks it takes to create raw quartz")
	@Config.Name("Resonator Time")
	public static int ResonatorTickTime = 6000;

	@Config.Ignore
	public static boolean UnbreakableContainers = false;

	@Config.Comment("Disable to use default Minecraft-style GUI elements. (Client Only)")
	@Config.Name("Use Pretty GUIs")
	public static boolean UsePrettyGUIs = true;

	@Config.Comment("The multiplier applied to Radiant Chest slots")
	@Config.Name("Radiant Chest Slot Multiplier")
	@Config.RangeInt(min = 1, max = 8)
	public static int RadiantMultiplier = 4;

	@Config.Comment("Set to true to mark all new chests with the no-new-items routing type")
	@Config.Name("Default to No New Items routing")
	public static boolean defaultRoutingNoNewItems = false;

	@Config.LangKey("arcanearchives.config.item_tracking")
	public static ItemTrackingConfig ItemTrackingConfig = new ItemTrackingConfig();

	public static class ItemTrackingConfig {
		//@Config.Comment("Whether or not radiant chests and troves should glow if being tracked")
		//@Config.Name("Chests & Troves glow when Tracked")
		//public boolean chestsGlow = true;

		@Config.Ignore
		@Config.Comment("Container classes to exclude from Mixin-based slot highlighting")
		@Config.Name("Container class to ignore")
		public String[] ContainerClasses = new String[]{};

		@Config.Ignore
		private List<Class> containerClasses = null;

		public List<Class> getContainerClasses () {
			if (containerClasses == null) {
				Class<?> container = GuiContainer.class;
				containerClasses = new ArrayList<>();
				for (String clazz : ContainerClasses) {
					Class<?> clz;
					try {
						clz = Class.forName(clazz);
					} catch (ClassNotFoundException e) {
						ArcaneArchives.logger.info("Unable to resolve class " + clazz + " so skipping");
						continue;
					}

					if (clz.isAssignableFrom(container)) {
						containerClasses.add(clz);
					} else {
						ArcaneArchives.logger.info("Skipping " + clazz + " as it does not derive from GuiContainer.");
					}
				}
			}

			return containerClasses;
		}

		@Config.Comment("Set to true to disable non-Arcane Archive container slots from being highlighted when tracking items")
		@Config.Name("Disable non-mod slot highlighting")
		public boolean DisableMixinHighlight = false;
	}

	@Config.LangKey("arcanearchives.config.manifest")
	public static ManifestConfig ManifestConfig = new ManifestConfig();

	public static class ManifestConfig {
		@Config.Comment("Whether having a manifest in your inventory is required to open the screen")
		@Config.Name("Manifest Presence")
		public boolean ManifestPresence = true;

		@Config.Comment("Disable overlay of grid on Manifest. (Client Only")
		@Config.Name("Disable Manifest Grid")
		public boolean DisableManifestGrid = true;

		@Config.Comment("Maximum distance in blocks to track chests from for the Manifest")
		@Config.Name("[Hive Network] Max track distance")
		public int MaxDistance = 100;

		@Config.Comment("Synchronise the currently searched item in JEI to the Manifest when opening for the first time, and then synchronise changes to the text field back into JEI. [Requires JEI installed]")
		@Config.Name("JEI Synchronise")
		public boolean jeiSynchronise = true;

		@Config.Comment("Whether or not the previous search term persists between 'sessions' (i.e., each time you open the manifest, your previous search term is filled in for you)")
		@Config.Name("Search Term Persistence")
		public boolean searchTermPersistence = false;
	}

	@Config.LangKey("arcanearchives.config.arsenal")
	public static ArsenalConfig ArsenalConfig = new ArsenalConfig();

	public static class ArsenalConfig {
		@Config.Comment("Enable the Arcane Arsenal module. When disabled, items will still be registered but not craftable or visible in JEI.")
		@Config.Name("Enable Arsenal")
		@RequiresMcRestart
		public boolean EnableArsenal = false;

		@Config.Comment("Enable colour-blind mode for Arsenal. This replaces gem icons specifically with variants more clearly defined for types of colour-blindness")
		@Config.Name("Colourblind Mode")
		public boolean ColourblindMode = false;

		@Config.Comment("What blocks a Munchstone can eat. First value is a block (such as minecraft:log), second value is the amount of hunger restored. Saturation always equals the hunger restoration.")
		@Config.Name("Valid Munchstone Blocks")
		@Config.RequiresMcRestart
		public String[] MunchstoneValidEntries = MunchstoneItem.DEFAULT_ENTRIES;

		//@Config.Comment("What blocks an Orderstone can transmute. First value is the transmutation cost for each step of the sequence, all subsequent values are blocks (such as minecraft:log). Please be aware that sequences longer than six blocks will display poorly in the Tome of Arcana.")
		//@Config.Name("Valid Orderstone Block Sequences")
		//@Config.RequiresMcRestart
		//public String[] OrderstoneValidEntries = OrderstoneItem.DEFAULT_ENTRIES;
	}
}
