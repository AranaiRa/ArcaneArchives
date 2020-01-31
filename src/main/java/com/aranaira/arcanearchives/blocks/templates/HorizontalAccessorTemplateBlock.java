package com.aranaira.arcanearchives.blocks.templates;

import javafx.beans.property.BooleanProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public abstract class HorizontalAccessorTemplateBlock extends HorizontalTemplateBlock {
	public static PropertyBool ACCESSOR = PropertyBool.create("accessor");

	public HorizontalAccessorTemplateBlock (Material materialIn) {
		super(materialIn);
	}
}
