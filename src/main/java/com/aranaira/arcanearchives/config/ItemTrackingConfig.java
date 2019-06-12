package com.aranaira.arcanearchives.config;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;
import scala.util.control.TailCalls.Cont;

import java.util.ArrayList;
import java.util.List;

@Config.LangKey("arcanearchives.config.item_tracking")
@Config(modid= ArcaneArchives.MODID, category="item_tracking")
@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ItemTrackingConfig {
	@Config.Comment("Radiant chest highlight colour (use HTML syntax i.e., #FFFFFF")
	@Config.Name("Radiant Chest Highlight")
	public static String ChestHighlight = "#1922C4";

	@Config.Comment("Container classes to exclude from Mixin-based slot highlighting")
	@Config.Name("Container class to ignore")
	public static String[] ContainerClasses = new String[]{};

	@Config.Ignore
	private static List<Class> containerClasses = null;

	public static List<Class> getContainerClasses () {
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
	public static boolean DisableMixinHighlight = false;
}
