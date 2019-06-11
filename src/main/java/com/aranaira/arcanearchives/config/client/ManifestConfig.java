package com.aranaira.arcanearchives.config.client;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraftforge.common.config.Config;

@Config(modid= ArcaneArchives.MODID, name = "arcanearchives", category="manifest_client")
public class ManifestConfig {
	@Config.Comment("Whether having a manifest in your inventory is required to open the screen")
	@Config.Name("Manifest Presence")
	public static boolean ManifestPresence = true;

	@Config.Comment("Disable overlay of grid on Manifest. (Client Only")
	@Config.Name("Disable Manifest Grid")
	public static boolean DisableManifestGrid = true;

	@Config.Comment("Radiant chest highlight colour (use HTML syntax i.e., #FFFFFF")
	@Config.Name("Radiant Chest Highlight")
	public static String ChestHighlight = "#1922C4";

	@Config.Comment("Maximum distance in blocks to track chests from for the Manifest")
	@Config.Name("[Hive Network] Max track distance")
	public static int MaxDistance = 100;
}
