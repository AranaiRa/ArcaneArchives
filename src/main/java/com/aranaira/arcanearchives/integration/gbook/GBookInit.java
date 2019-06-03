package com.aranaira.arcanearchives.integration.gbook;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.items.TomeOfArcanaItemBackground;
import gigaherz.lirelent.guidebook.guidebook.client.BookRendering;
import net.minecraft.util.ResourceLocation;

public class GBookInit {
	public static void init () {
		BookRendering.BACKGROUND_FACTORY_MAP.put(new ResourceLocation(ArcaneArchives.MODID, "textures/gui/arcana_documentation.png"), TomeOfArcanaItemBackground.TomeOfArcanaItemBackgroundFactory);
	}
}
