package com.aranaira.arcanearchives.config;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.client.gui.GUIManifest;
import com.aranaira.arcanearchives.items.gems.oval.MunchstoneItem;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.PacketConfig.DefaultRoutingType;
import com.aranaira.arcanearchives.network.PacketConfig.MaxDistance;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

	@Config.LangKey("arcanearchives.config.manifest")
	public static ManifestConfig ManifestConfig = new ManifestConfig();

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

		@Config.Comment("Set to false to disable the Brazier item pick-up sound")
		@Config.Name("Brazier Pickup Sound")
		public boolean brazierPickup = true;

		@Config.Comment("Set to false to disable all sounds")
		@Config.Name("Use Sounds")
		public boolean useSounds = true;
	}
}
