package com.aranaira.arcanearchives.config.client;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;

@Config(modid= ArcaneArchives.MODID, name = "Item Tracking Settings", category="item_tracking")
@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ItemTrackingConfig {
	@Config.Comment("Radiant chest highlight colour (use HTML syntax i.e., #FFFFFF")
	@Config.Name("Radiant Chest Highlight")
	public static String ChestHighlight = "#1922C4";

	@Config.Comment("Container classes to exclude from Mixin-based slot highlighting")
	@Config.Name("Container class to ignore")
	public static String[] ContainerClasses = new String[]{};

	@Config.Comment("Set to true to disable non-Arcane Archive container slots from being highlighted when tracking items")
	@Config.Name("Disable non-mod slot highlighting")
	public static boolean DisableMixinHighlight = false;
}
