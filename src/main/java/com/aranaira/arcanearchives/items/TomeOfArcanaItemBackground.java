package com.aranaira.arcanearchives.items;

import gigaherz.lirelent.guidebook.guidebook.client.GuiGuidebook;
import gigaherz.lirelent.guidebook.guidebook.client.background.IBookBackgroundFactory;
import gigaherz.lirelent.guidebook.guidebook.client.background.StaticImage1PageBackground;
import gigaherz.lirelent.guidebook.guidebook.elements.ElementImage;

public class TomeOfArcanaItemBackground extends StaticImage1PageBackground {
	public TomeOfArcanaItemBackground (GuiGuidebook gui, ElementImage image) {
		super(gui, image);
	}

	@Override
	public int getInnerMargin () {
		return 35;
	}

	@Override
	public int getOuterMargin () {
		return 5;
	}

	@Override
	public int getTopMargin () {
		return 5;
	}

	@Override
	public int getBottomMargin () {
		return 13;
	}

	public static IBookBackgroundFactory TomeOfArcanaItemBackgroundFactory = (gui, backgroundLocation) -> {
		ElementImage elementImage = new ElementImage(false, false);

		elementImage.textureLocation = backgroundLocation;
		elementImage.tx = 0;
		elementImage.ty = 0;
		elementImage.tw = 243;
		elementImage.th = 253;
		elementImage.w = 256;
		elementImage.h = 256;
		elementImage.scale = 1f;

		return new TomeOfArcanaItemBackground(gui, elementImage);
	};
}
