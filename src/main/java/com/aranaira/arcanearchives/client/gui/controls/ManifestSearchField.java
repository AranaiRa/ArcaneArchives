package com.aranaira.arcanearchives.client.gui.controls;

import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.integration.jei.JEIPlugin;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.common.Loader;
import org.lwjgl.input.Keyboard;

public class ManifestSearchField extends RightClickTextField {
	private int id;
	private String storedJEI;

	public ManifestSearchField (int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
		super(componentId, fontrendererObj, x, y, par5Width, par6Height);
		this.id = componentId;
	}

	@Override
	public boolean mouseClicked (int mouseX, int mouseY, int mouseButton) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			syncFromJEI();
		} else if (mouseButton == 1) {
			// right click
			this.setText("");
			this.setResponderEntryValue(this.id, "");
			syncToJEI();
		}

		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void syncToJEI () {
		if (!ConfigHandler.ManifestConfig.jeiSynchronise) {
			return;
		}

		if (!Loader.isModLoaded("jei")) {
			return;
		}

		// This function manually pushes the current search term from here into JEI
		// if our current string is not empty.
		String current = this.getText();
		JEIPlugin.runtime.getIngredientFilter().setFilterText(current);
	}

	public void syncFromJEI () {
		if (!ConfigHandler.ManifestConfig.jeiSynchronise) {
			return;
		}

		if (!Loader.isModLoaded("jei")) {
			return;
		}

		// This forcefully copies the current search term from JEI into the Manifest.
		// It will override any value in the manifest current.

		String filterText = JEIPlugin.runtime.getIngredientFilter().getFilterText();
		if (filterText.trim().toLowerCase().isEmpty()) {
			return;
		}

		setText(filterText);
		setResponderEntryValue(this.id, filterText);
	}

	public void restoreFromJEI () {
		if (!Loader.isModLoaded("jei")) {
			return;
		}

		JEIPlugin.runtime.getIngredientFilter().setFilterText(storedJEI);
	}

	@Override
	public void setResponderEntryValue (int idIn, String textIn) {
		super.setResponderEntryValue(idIn, textIn);
		syncToJEI();
	}
}
