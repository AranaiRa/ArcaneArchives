package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.client.gui.controls.InvisibleButton;
import com.aranaira.arcanearchives.client.gui.controls.RightClickTextField;
import com.aranaira.arcanearchives.client.render.RenderItemExtended;
import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.inventory.ContainerRadiantChest;
import com.aranaira.arcanearchives.inventory.ContainerRadiantFurnace;
import com.aranaira.arcanearchives.inventory.slots.SlotExtended;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.PacketRadiantChest.SetName;
import com.aranaira.arcanearchives.network.PacketRadiantChest.ToggleBrazier;
import com.aranaira.arcanearchives.network.PacketRadiantChest.UnsetName;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantFurnaceTileEntity;
import com.aranaira.arcanearchives.tileentities.interfaces.IBrazierRouting;
import com.aranaira.arcanearchives.util.ColorUtils;
import com.aranaira.arcanearchives.util.ColorUtils.Color;
import com.aranaira.arcanearchives.util.ManifestTrackingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Optional;
import org.lwjgl.input.Keyboard;
import vazkii.quark.api.IChestButtonCallback;
import vazkii.quark.api.IItemSearchBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUIRadiantFurnace extends GuiContainer {
	private static final ResourceLocation GUITextures = new ResourceLocation("arcanearchives:textures/gui/radiant_furnace.png");
	private static final ResourceLocation GUITexturesSimple = new ResourceLocation("arcanearchives:textures/gui/simple/radiant_furnace.png");

	private static final int
		BG_X = 0,
		BG_Y = 0,
		BG_W = 102,
		BG_H = 70;
	private static final int ImageScale = 256;

	private ContainerRadiantFurnace container;
	private InventoryPlayer playerinventory;
	private RadiantFurnaceTileEntity tile;

	public GUIRadiantFurnace(ContainerRadiantFurnace container, InventoryPlayer playerinventory) {
		super(container);

		this.container = container;

		this.playerinventory = playerinventory;
		this.tile = container.getTile();
		xSize = BG_W;
		ySize = BG_H;
	}

	@Override
	public void initGui () {
		super.initGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer (float partialTicks, int mouseX, int mouseY) {
		if (ConfigHandler.UsePrettyGUIs) {
			mc.getTextureManager().bindTexture(GUITextures);
		} else {
			mc.getTextureManager().bindTexture(GUITexturesSimple);
		}
		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, BG_X, BG_Y, BG_W, BG_H, ImageScale, ImageScale);
	}

	@Override
	public void drawScreen (int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		int i = this.guiLeft;
		int j = this.guiTop;
		this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void renderHoveredToolTip (int mouseX, int mouseY) {
		super.renderHoveredToolTip(mouseX, mouseY);
	}
}
