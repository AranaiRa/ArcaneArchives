/*package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.inventory.ContainerUpgrades;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.tileentities.interfaces.IUpgradeableStorage;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GUIUpgrades extends GuiContainer {
	private static final ResourceLocation TEXTURE_PLAYERINV = new ResourceLocation("arcanearchives:textures/gui/player_inv.png");
	private static final ResourceLocation TEXTURE_PLAYERINV_SIMPLE = new ResourceLocation("arcanearchives:textures/gui/simple/player_inv.png");
	private static final ResourceLocation TEXTURE_UPGRADES = new ResourceLocation("arcanearchives:textures/gui/radiant_upgrades.png");
	private static final ResourceLocation TEXTURE_UPGRADES_SIMPLE = new ResourceLocation("arcanearchives:textures/gui/simple/radiant_upgrades.png");

	private static final int INVENTORY_W = 181;
	private static final int INVENTORY_H = 101;
	private static final int STORAGEUPGRADES_W = 82;
	private static final int STORAGEUPGRADES_H = 32;
	private static final int STORAGEUPGRADES_U = 0;
	private static final int STORAGEUPGRADES_V3 = 32;
	private static final int STORAGEUPGRADES_V2 = 64;
	private static final int STORAGEUPGRADES_V1 = 96;

	private ContainerUpgrades container;
	private EntityPlayer player;
	private ImmanenceTileEntity tile;
	private IUpgradeableStorage storage;

	public GUIUpgrades (ContainerUpgrades container, EntityPlayer player, ImmanenceTileEntity tile) {
		super(container);

		assert tile instanceof IUpgradeableStorage;

		this.container = container;
		this.player = player;
		this.tile = tile;
		this.storage = (IUpgradeableStorage) tile;
	}

	@Override
	public void drawScreen (int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer (float partialTicks, int mouseX, int mouseY) {
		float i = (this.width - this.xSize) / 2;
		float j = (this.height - this.ySize) / 2;

		if (ConfigHandler.UsePrettyGUIs) {
			this.mc.getTextureManager().bindTexture(TEXTURE_UPGRADES);
		} else {
			this.mc.getTextureManager().bindTexture(TEXTURE_UPGRADES_SIMPLE);
		}
		this.drawTexturedModalRect(i + 49, j, 0, 0, STORAGEUPGRADES_W, STORAGEUPGRADES_H);
		this.drawTexturedModalRect(i + 49, j + 36, STORAGEUPGRADES_U, STORAGEUPGRADES_V3, STORAGEUPGRADES_W, STORAGEUPGRADES_H);

		if (ConfigHandler.UsePrettyGUIs) {
			this.mc.getTextureManager().bindTexture(TEXTURE_PLAYERINV);
		} else {
			this.mc.getTextureManager().bindTexture(TEXTURE_PLAYERINV_SIMPLE);
		}
		this.drawTexturedModalRect(i, j + 67, 0, 0, INVENTORY_W, INVENTORY_H);
	}
}*/
