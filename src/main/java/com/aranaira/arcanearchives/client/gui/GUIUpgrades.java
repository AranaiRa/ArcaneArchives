package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.inventory.ContainerUpgrades;
import com.aranaira.arcanearchives.tileentities.IUpgradeableStorage;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;

public class GUIUpgrades extends GuiContainer {
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
	protected void drawGuiContainerBackgroundLayer (float partialTicks, int mouseX, int mouseY) {

	}
}
