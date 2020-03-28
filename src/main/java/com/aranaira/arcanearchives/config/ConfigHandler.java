package com.aranaira.arcanearchives.config;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.client.gui.GUIManifest;
import com.aranaira.arcanearchives.items.gems.oval.MunchstoneItem;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.PacketConfig.DefaultRoutingType;
import com.aranaira.arcanearchives.network.PacketConfig.MaxDistance;
import com.aranaira.arcanearchives.network.PacketConfig.TrovesDispense;
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

			TrovesDispense packet3 = new TrovesDispense(trovesDispense);
			Networking.CHANNEL.sendToServer(packet3);

			Minecraft minecraft = Minecraft.getMinecraft();
			if (minecraft.currentScreen instanceof GUIManifest) {
				((GUIManifest) minecraft.currentScreen).doRefresh();
			}
		}
	}

	@Config.Ignore
	public static boolean UnbreakableContainers = false;

	@Config.Comment("Disable to use default Minecraft-style GUI elements. (Client Only)")
	@Config.Name("Use Pretty GUIs")
	public static boolean UsePrettyGUIs = true;

	@Config.Comment("Set to true to mark all new chests with the no-new-items routing type")
	@Config.Name("Default Radiant Chests to 'No New Items' routing")
	public static boolean defaultRoutingNoNewItems = false;

	@Config.Comment("Set to true to dispense an entire stack for left click; false to dispense a single item for left click")
	@Config.Name("Troves Dispense Entire Stack")
	public static boolean trovesDispense = true;

	@Config.Comment("Settings related to server-side configuration")
	@Config.Name("Server Settings")
	public static ServerSideConfig serverSideConfig = new ServerSideConfig();

	public static class ServerSideConfig {
		@Config.Comment("Limit of resonators per player's network")
		@Config.Name("Resonator Limit")
		public int ResonatorLimit = 3;

		@Config.Comment("Number of ticks it takes to create raw quartz")
		@Config.Name("Resonator Time")
		public int ResonatorTickTime = 6000;

		@Config.Comment("The multiplier applied to Radiant Chest slots")
		@Config.Name("Radiant Chest Slot Multiplier")
		@Config.RangeInt(min = 1, max = 8)
		public int RadiantMultiplier = 4;

		@Config.Comment("Whether or not players should be granted the book from breaking bookshelves")
		@Config.Name("Book from Breaking Bookshelf")
		public boolean BookFromBookshelf = true;

		@Config.Comment("Whether or not players should be granted the book for crafting their first Resonator")
		@Config.Name("Book from Radiant Resonator")
		public boolean BookFromResonator = true;

		@Config.Comment("The chance out of 100 that bashing a piece of Raw Radiant Quartz generates a single Sliver of Light")
		@Config.Name("Chance for Single Sliver of Light")
		public int ChanceForSliverSingle = 40;

		@Config.Comment("The chance out of 100 that bashing a piece of Raw Radiant Quartz generates a cluster of Slivers of Lights and is destroyed")
		@Config.Name("Chance for Cluster of Slivers of Light")
		public int ChanceForSliverCluster = 20;

		@Config.Comment("The minimum number of Slivers of Light generated when a piece of Raw Radiant Quartz is bashed and shattered")
		@Config.Name("Minimum Slivers in Cluster")
		public int AmountGeneratedOnSliverClusterMinimum = 8;

		@Config.Comment("The maximum number of Slivers of Light generated when a piece of Raw Radiant Quartz is bashed and shattered")
		@Config.Name("Maximum Slivers in Cluster")
		public int AmountGeneratedOnSliverClusterMaximum = 24;
	}

	@Config.Comment("Settings related to item tracking in non-mod containers")
	@Config.Name("Non-Mod Tracking (Client-Only)")
	public static NonModTrackingConfig nonModTrackingConfig = new NonModTrackingConfig();

	public static class NonModTrackingConfig {
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
						ArcaneArchives.logger.debug("Unable to resolve class " + clazz + " so skipping.");
						continue;
					}

					if (clz.isAssignableFrom(container)) {
						containerClasses.add(clz);
					} else {
						ArcaneArchives.logger.debug("Skipping " + clazz + " as it does not derive from GuiContainer.");
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
		@Config.Comment("Whether having a manifest in your inventory is required to open the manifest screen using a Hotkey.")
		@Config.Name("Manifest Presence")
		public boolean ManifestPresence = true;

		@Config.Comment("Disable overlay of grid on Manifest.")
		@Config.Name("Disable Manifest Grid")
		public boolean DisableManifestGrid = true;

		@Config.Comment("Maximum distance in blocks to track chests from for the Manifest from your current position. Useful when a member of a Hive Network.")
		@Config.Name("Max distance for items")
		public int MaxDistance = 100;

		@Config.Comment("Synchronise the currently searched item into the JEI search box. The previous JEI search term will be restored upon closing. [Requires JEI installed]")
		@Config.Name("Synchronise JEI with Search Bar")
		public boolean jeiSynchronise = false;

		@Config.Comment("Whether or not the previous search term persists between 'sessions' (i.e., each time you open the manifest, your previous search term is filled in for you)")
		@Config.Name("Search Term Persistence")
		public boolean searchTermPersistence = false;

		@Config.Comment("Set to true to require holding shift to *close* the manifest after clicking on an item. Otherwise, holding shift will keep the manifest open.")
		@Config.Name("Hold Shift To Keep Manifest Open")
		public boolean holdShift = true;
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
	}

	@Config.Comment("Settings related to sounds")
	@Config.Name("Sound Settings")
	public static SoundConfig soundConfig = new SoundConfig();

	public static class SoundConfig {
		@Config.Comment("Set to false to disable the Resonator completion sound")
		@Config.Name("Resonator Complete Sound")
		public boolean resonatorComplete = true;

		@Config.Comment("Set to false to disable the Resonator ticking sound")
		@Config.Name("Resonator Ticking Sound")
		public boolean resonatorTicking = true;

		@Config.Comment("Set to the volume you wish the resonator to tick at")
		@Config.Name("Resonator Tick Volume")
		public float resonatorVolume = 0.15f;

		@Config.Comment("Set to false to disable the Brazier item pick-up sound")
		@Config.Name("Brazier Pickup Sound")
		public boolean brazierPickup = true;

		@Config.Comment("Set to false to disable all sounds")
		@Config.Name("Use Sounds")
		public boolean useSounds = true;
	}
}
