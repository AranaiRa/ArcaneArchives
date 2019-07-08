package com.aranaira.arcanearchives.config;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.client.gui.GUIManifest;
import com.aranaira.arcanearchives.items.gems.oval.MunchstoneItem;
import com.aranaira.arcanearchives.items.gems.oval.OrderstoneItem;
import com.aranaira.arcanearchives.network.NetworkHandler;
import com.aranaira.arcanearchives.network.PacketConfig.MaxDistance;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@Config.LangKey("arcanearchives.config.general")
@Config(modid = ArcaneArchives.MODID)
@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ConfigHandler {
	@SubscribeEvent(priority=EventPriority.HIGH)
	public static void onConfigChanged (ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(ArcaneArchives.MODID)) {
			ConfigManager.sync(ArcaneArchives.MODID, Config.Type.INSTANCE);
		}
	}

	@SubscribeEvent(priority=EventPriority.LOWEST)
	@SideOnly(Side.CLIENT)
	public static void onClientConfigChanged (ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(ArcaneArchives.MODID)) {
			parseColours();
			MaxDistance packet = new MaxDistance(ManifestConfig.MaxDistance);
			NetworkHandler.CHANNEL.sendToServer(packet);

			Minecraft minecraft = Minecraft.getMinecraft();
			if (minecraft.currentScreen instanceof GUIManifest) {
				((GUIManifest) minecraft.currentScreen).doRefresh();
			}
		}
	}

	@Config.Ignore
	public static int MANIFEST_HIGHLIGHT = 0x991922c4;

	public static void parseColours () {
		try {
			MANIFEST_HIGHLIGHT = Long.decode(ItemTrackingConfig.ChestHighlight.toLowerCase()).intValue();
		} catch (NumberFormatException event) {
			ArcaneArchives.logger.error("Invalid manifest highlight colour: " + ItemTrackingConfig.ChestHighlight, event);
		}
	}

	@Config.Comment("Limit of resonators per player's network")
	@Config.Name("Resonator Limit")
	public static int ResonatorLimit = 3;

	@Config.Comment("Number of ticks it takes to create raw quartz")
	@Config.Name("Resonator Time")
	public static int ResonatorTickTime = 6000;

	@Config.Comment("Causes Radiant Chests, Troves, etc, to be unbreakable when not empty")
	@Config.Name("Unbreakable Radiant Chests & Troves")
	public static boolean UnbreakableContainers = false;

	@Config.Comment("Disable to use default Minecraft-style GUI elements. (Client Only)")
	@Config.Name("Use Pretty GUIs")
	public static boolean UsePrettyGUIs = true;

	@Config.Comment("The multiplier applied to Radiant Chest slots")
	@Config.Name("Radiant Chest Slot Multiplier")
	@Config.RangeInt(min=1, max=8)
	public static int RadiantMultiplier = 4;

	//public static boolean bJarvisModeEnabled = false;

	/*public static int iRadiantResonatorDrain = 20;
	public static int iRepositoryMatrixDrain = 80;
	public static int iStorageMatrixDrain = 80;*/

	/*@Config.Comment("The Amount of Bonus Ticks Blocks Get if Their Drain is Met")
	public static int iRadiantResonatorBonusTicks = 1;*/

	/*@Config.Comment("The Amount of Items Each Repository Can Hold")
	public static int iRepositoryMatrixItemCap = 256;
	public static int iStorageMatrixItemCap = 256;*/

	@Config.LangKey("arcanearchives.config.item_tracking")
	public static ItemTrackingConfig ItemTrackingConfig = new ItemTrackingConfig();

	public static class ItemTrackingConfig {
		@Config.Comment("Radiant chest highlight colour (use HTML syntax i.e., #FFFFFF")
		@Config.Name("Radiant Chest Highlight")
		public String ChestHighlight = "#1922C4";

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
	}

	@Config.LangKey("arcanearchives.config.arsenal")
	public static ArsenalConfig ArsenalConfig = new ArsenalConfig();

	public static class ArsenalConfig {
		@Config.Comment("Enable the Arcane Arsenal module. When disabled, items will still be registered but not craftable or visible in JEI.")
		@Config.Name("Enable Arsenal")
		@RequiresMcRestart
		public boolean EnableArsenal = true;

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
