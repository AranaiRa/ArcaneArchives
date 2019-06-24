package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.client.render.RenderItemExtended;
import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.inventory.ContainerRadiantChest;
import com.aranaira.arcanearchives.inventory.slots.SlotExtended;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.ManifestTracking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Optional;
import org.lwjgl.input.Keyboard;
import vazkii.quark.api.IChestButtonCallback;
import vazkii.quark.api.IItemSearchBar;

import java.io.IOException;
import java.util.List;

@Optional.InterfaceList({@Optional.Interface(modid = "quark", iface = "vazkii.quark.api.IChestButtonCallback", striprefs = true), @Optional.Interface(modid = "quark", iface = "vazkii.quark.api.IItemSearchBar", striprefs = true)})
public class GUIRadiantChest extends GuiContainer implements GuiPageButtonList.GuiResponder, IChestButtonCallback, IItemSearchBar {

	public static final int WIDTH = 176;
	public static final int HEIGHT = 166;

	private static final ResourceLocation GUITextures = new ResourceLocation("arcanearchives:textures/gui/radiantchest.png");
	private static final ResourceLocation GUITexturesSimple = new ResourceLocation("arcanearchives:textures/gui/simple/radiantchest.png");
	private final int ImageHeight = 253, ImageWidth = 192, ImageScale = 256;

	private Slot hoveredSlot;
	/**
	 * Used when touchscreen is enabled.
	 */
	private Slot clickedSlot;
	/**
	 * Used when touchscreen is enabled.
	 */
	private boolean isRightMouseClick;
	/**
	 * Used when touchscreen is enabled
	 */
	private ItemStack draggedStack = ItemStack.EMPTY;
	private int touchUpX;
	private int touchUpY;
	private Slot returningStackDestSlot;
	private long returningStackTime;
	/**
	 * Used when touchscreen is enabled
	 */
	private ItemStack returningStack = ItemStack.EMPTY;
	private Slot currentDragTargetSlot;
	private long dragItemDropDelay;
	private int dragSplittingLimit;
	private int dragSplittingButton;
	private boolean ignoreMouseUp;
	private int dragSplittingRemnant;
	private long lastClickTime;
	private Slot lastClickSlot;
	private int lastClickButton;
	private boolean doubleClick;
	private ItemStack shiftClickedSlot = ItemStack.EMPTY;

	private RadiantChestTileEntity tile;
	private InventoryPlayer playerinventory;
	private List<Ingredient> tracked;
	private int dimension;
	private BlockPos pos;
	private RightClickTextField nameBox;
	private ContainerRadiantChest container;

	private int mNameTextLeftOffset = 53;
	private int mNameTextTopOffset = 238;
	private int mNameTextWidth = 88;
	private int mNameTextHeight = 10;

	public GUIRadiantChest (ContainerRadiantChest container, InventoryPlayer playerinventory) {
		super(container);

		this.container = container;

		this.playerinventory = playerinventory;
		this.tile = container.getTile();
		xSize = ImageWidth;
		ySize = ImageHeight;

		this.ignoreMouseUp = true;

		this.dimension = tile.dimension;
		this.pos = tile.getPos();
		tracked = ManifestTracking.get(dimension, pos);
	}

	@Override
	public void initGui () {
		super.initGui();

		nameBox = new RightClickTextField(1, fontRenderer, guiLeft + mNameTextLeftOffset, guiTop + mNameTextTopOffset, mNameTextWidth, mNameTextHeight);
		nameBox.setText(container.getName());
		nameBox.setGuiResponder(this);
		nameBox.setEnableBackgroundDrawing(false);

		buttonList.clear();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer (float partialTicks, int mouseX, int mouseY) {
		if (ConfigHandler.UsePrettyGUIs) {
			mc.getTextureManager().bindTexture(GUITextures);
		} else {
			mc.getTextureManager().bindTexture(GUITexturesSimple);
		}
		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, ImageScale, ImageScale, ImageScale, ImageScale);
	}

	@Override
	public void drawScreen (int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		int i = this.guiLeft;
		int j = this.guiTop;
		this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();

		for (int k = 0; k < this.buttonList.size(); ++k) {
			((GuiButton) this.buttonList.get(k)).drawButton(this.mc, mouseX, mouseY, partialTicks);
		}
		for (int l = 0; l < this.labelList.size(); ++l) {
			((GuiLabel) this.labelList.get(l)).drawLabel(this.mc, mouseX, mouseY);
		}

		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) i, (float) j, 0.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableRescaleNormal();
		this.hoveredSlot = null;
		int k = 240;
		int l = 240;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		for (int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); ++i1) {
			Slot slot = this.inventorySlots.inventorySlots.get(i1);

			if (slot.isEnabled()) {
				this.drawSlot(slot);
			}

			if (this.isMouseOverSlot(slot, mouseX, mouseY) && slot.isEnabled()) {
				this.hoveredSlot = slot;
				GlStateManager.disableLighting();
				GlStateManager.disableDepth();
				int j1 = slot.xPos;
				int k1 = slot.yPos;
				GlStateManager.colorMask(true, true, true, false);
				this.drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433);
				GlStateManager.colorMask(true, true, true, true);
				GlStateManager.enableLighting();
				GlStateManager.enableDepth();
			}
		}

		RenderHelper.disableStandardItemLighting();
		this.drawGuiContainerForegroundLayer(mouseX, mouseY);
		RenderHelper.enableGUIStandardItemLighting();
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiContainerEvent.DrawForeground(this, mouseX, mouseY));
		InventoryPlayer inventoryplayer = this.mc.player.inventory;
		ItemStack itemstack = this.draggedStack.isEmpty() ? inventoryplayer.getItemStack() : this.draggedStack;

		if (!itemstack.isEmpty()) {
			int j2 = 8;
			int k2 = this.draggedStack.isEmpty() ? 8 : 16;
			String s = null;

			if (!this.draggedStack.isEmpty() && this.isRightMouseClick) {
				itemstack = itemstack.copy();
				itemstack.setCount(MathHelper.ceil((float) itemstack.getCount() / 2.0F));
			} else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
				itemstack = itemstack.copy();
				itemstack.setCount(this.dragSplittingRemnant);

				if (itemstack.isEmpty()) {
					s = "" + TextFormatting.YELLOW + "0";
				}
			}

			this.drawItemStack(itemstack, mouseX - i - 8, mouseY - j - k2, s);
		}

		if (!this.returningStack.isEmpty()) {
			float f = (float) (Minecraft.getSystemTime() - this.returningStackTime) / 100.0F;

			if (f >= 1.0F) {
				f = 1.0F;
				this.returningStack = ItemStack.EMPTY;
			}

			int l2 = this.returningStackDestSlot.xPos - this.touchUpX;
			int i3 = this.returningStackDestSlot.yPos - this.touchUpY;
			int l1 = this.touchUpX + (int) ((float) l2 * f);
			int i2 = this.touchUpY + (int) ((float) i3 * f);
			this.drawItemStack(this.returningStack, l1, i2, (String) null);
		}

		GlStateManager.popMatrix();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		RenderHelper.enableStandardItemLighting();

		nameBox.drawTextBox();

		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void renderHoveredToolTip (int p_191948_1_, int p_191948_2_) {
		if (this.mc.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.getHasStack()) {
			this.renderToolTip(this.hoveredSlot.getStack(), p_191948_1_, p_191948_2_);
		}
	}

	private void drawItemStack (ItemStack stack, int x, int y, String altText) {
		GlStateManager.translate(0.0F, 0.0F, 32.0F);
		this.zLevel = 200.0F;
		this.itemRender.zLevel = 200.0F;
		RenderItemExtended.INSTANCE.setZLevel(this.itemRender.zLevel);
		net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
		if (font == null) {
			font = fontRenderer;
		}
		this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
		this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y - (this.draggedStack.isEmpty() ? 0 : 8), altText);
		this.zLevel = 0.0F;
		this.itemRender.zLevel = 0.0F;
		RenderItemExtended.INSTANCE.setZLevel(this.itemRender.zLevel);
	}

	@Override
	public void drawSlot (Slot slotIn) {
		ItemStack stack = slotIn.getStack();
		if (!stack.isEmpty()) {
			if (tracked != null && !tracked.isEmpty() && ManifestTracking.matches(stack, tracked)) {
				GlStateManager.disableDepth();
				float partialTicks = this.mc.getTickLength();
				drawRect(slotIn.xPos, slotIn.yPos, slotIn.xPos + 16, slotIn.yPos + 16, ConfigHandler.MANIFEST_HIGHLIGHT);
				GlStateManager.enableDepth();
			}
		}

		int i = slotIn.xPos;
		int j = slotIn.yPos;
		ItemStack itemstack = slotIn.getStack();
		boolean flag = false;
		boolean flag1 = slotIn == this.clickedSlot && !this.draggedStack.isEmpty() && !this.isRightMouseClick;
		ItemStack itemstack1 = this.mc.player.inventory.getItemStack();
		String s = null;

		if (slotIn == this.clickedSlot && !this.draggedStack.isEmpty() && this.isRightMouseClick && !itemstack.isEmpty()) {
			itemstack = itemstack.copy();
			itemstack.setCount(itemstack.getCount() / 2);
		} else if (this.dragSplitting && this.dragSplittingSlots.contains(slotIn) && !itemstack1.isEmpty()) {
			if (this.dragSplittingSlots.size() == 1) {
				return;
			}

			if (ContainerRadiantChest.canAddItemToSlot(slotIn, itemstack1, true) && this.inventorySlots.canDragIntoSlot(slotIn)) {
				itemstack = itemstack1.copy();
				flag = true;
				Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack, slotIn.getStack().isEmpty() ? 0 : slotIn.getStack().getCount());
				int k = slotIn.getItemStackLimit(itemstack);

				if (itemstack.getCount() > k) {
					s = TextFormatting.YELLOW.toString() + k;
					itemstack.setCount(k);
				}
			} else {
				this.dragSplittingSlots.remove(slotIn);
				this.updateDragSplitting();
			}
		}

		this.zLevel = 100.0F;
		this.itemRender.zLevel = 100.0F;

		if (itemstack.isEmpty() && slotIn.isEnabled()) {
			TextureAtlasSprite textureatlassprite = slotIn.getBackgroundSprite();

			if (textureatlassprite != null) {
				GlStateManager.disableLighting();
				this.mc.getTextureManager().bindTexture(slotIn.getBackgroundLocation());
				this.drawTexturedModalRect(i, j, textureatlassprite, 16, 16);
				GlStateManager.enableLighting();
				flag1 = true;
			}
		}

		if (!flag1) {
			if (flag) {
				drawRect(i, j, i + 16, j + 16, -2130706433);
			}

			GlStateManager.enableDepth();
			this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, itemstack, i, j);
			RenderItemExtended.INSTANCE.setZLevel(this.itemRender.zLevel);
			if (slotIn instanceof SlotExtended) {
				RenderItemExtended.INSTANCE.renderItemOverlayIntoGUI(this.fontRenderer, itemstack, i, j, s);
			} else {
				this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, itemstack, i, j, s);
			}
		}

		this.itemRender.zLevel = 0.0F;
		this.zLevel = 0.0F;
		RenderItemExtended.INSTANCE.setZLevel(this.itemRender.zLevel);


	}

	private void updateDragSplitting () {
		ItemStack itemstack = this.mc.player.inventory.getItemStack();

		if (!itemstack.isEmpty() && this.dragSplitting) {
			if (this.dragSplittingLimit == 2) {
				this.dragSplittingRemnant = itemstack.getMaxStackSize();
			} else {
				this.dragSplittingRemnant = itemstack.getCount();

				for (Slot slot : this.dragSplittingSlots) {
					ItemStack itemstack1 = itemstack.copy();
					ItemStack itemstack2 = slot.getStack();
					int i = itemstack2.isEmpty() ? 0 : itemstack2.getCount();
					Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack1, i);
					//int j = Math.min(itemstack1.getMaxStackSize(), slot.getItemStackLimit(itemstack1));
					int j = slot.getItemStackLimit(itemstack1);

					if (itemstack1.getCount() > j) {
						itemstack1.setCount(j);
					}

					this.dragSplittingRemnant -= itemstack1.getCount() - i;
				}
			}
		}
	}

	private Slot getSlotAtPosition (int x, int y) {
		for (int i = 0; i < this.inventorySlots.inventorySlots.size(); ++i) {
			Slot slot = this.inventorySlots.inventorySlots.get(i);

			if (this.isMouseOverSlot(slot, x, y) && slot.isEnabled()) {
				return slot;
			}
		}

		return null;
	}

	@Override
	protected void mouseClicked (int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseButton == 0) {
			for (int i = 0; i < this.buttonList.size(); ++i) {
				GuiButton guibutton = this.buttonList.get(i);

				if (guibutton.mousePressed(this.mc, mouseX, mouseY)) {
					net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre event = new net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre(this, guibutton, this.buttonList);
					if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) {
						break;
					}
					guibutton = event.getButton();
					this.selectedButton = guibutton;
					guibutton.playPressSound(this.mc.getSoundHandler());
					this.actionPerformed(guibutton);
					if (this.equals(this.mc.currentScreen)) {
						net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Post(this, event.getButton(), this.buttonList));
					}
				}
			}
		}

		boolean flag = this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(mouseButton - 100);
		Slot slot = this.getSlotAtPosition(mouseX, mouseY);
		long i = Minecraft.getSystemTime();
		this.doubleClick = this.lastClickSlot == slot && i - this.lastClickTime < 250L && this.lastClickButton == mouseButton;
		this.ignoreMouseUp = false;

		if (mouseButton == 0 || mouseButton == 1 || flag) {
			int j = this.guiLeft;
			int k = this.guiTop;
			boolean flag1 = this.hasClickedOutside(mouseX, mouseY, j, k);
			if (slot != null) {
				flag1 = false; // Forge, prevent dropping of items through slots outside of GUI boundaries
			}
			int l = -1;

			if (slot != null) {
				l = slot.slotNumber;
			}

			if (flag1) {
				l = -999;
			}

			if (this.mc.gameSettings.touchscreen && flag1 && this.mc.player.inventory.getItemStack().isEmpty()) {
				this.mc.displayGuiScreen((GuiScreen) null);
				return;
			}

			if (l != -1) {
				if (this.mc.gameSettings.touchscreen) {
					if (slot != null && slot.getHasStack()) {
						this.clickedSlot = slot;
						this.draggedStack = ItemStack.EMPTY;
						this.isRightMouseClick = mouseButton == 1;
					} else {
						this.clickedSlot = null;
					}
				} else if (!this.dragSplitting) {
					if (this.mc.player.inventory.getItemStack().isEmpty()) {
						if (this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(mouseButton - 100)) {
							this.handleMouseClick(slot, l, mouseButton, ClickType.CLONE);
						} else {
							boolean flag2 = l != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
							ClickType clicktype = ClickType.PICKUP;

							if (flag2) {
								this.shiftClickedSlot = slot != null && slot.getHasStack() ? slot.getStack().copy() : ItemStack.EMPTY;
								clicktype = ClickType.QUICK_MOVE;
							} else if (l == -999) {
								clicktype = ClickType.THROW;
							}

							this.handleMouseClick(slot, l, mouseButton, clicktype);
						}

						this.ignoreMouseUp = true;
					} else {
						this.dragSplitting = true;
						this.dragSplittingButton = mouseButton;
						this.dragSplittingSlots.clear();

						if (mouseButton == 0) {
							this.dragSplittingLimit = 0;
						} else if (mouseButton == 1) {
							this.dragSplittingLimit = 1;
						} else if (this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(mouseButton - 100)) {
							this.dragSplittingLimit = 2;
						}
					}
				}
			}
		}

		this.lastClickSlot = slot;
		this.lastClickTime = i;
		this.lastClickButton = mouseButton;

		nameBox.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void mouseClickMove (int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		Slot slot = this.getSlotAtPosition(mouseX, mouseY);
		ItemStack itemstack = this.mc.player.inventory.getItemStack();

		if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
			if (clickedMouseButton == 0 || clickedMouseButton == 1) {
				if (this.draggedStack.isEmpty()) {
					if (slot != this.clickedSlot && !this.clickedSlot.getStack().isEmpty()) {
						this.draggedStack = this.clickedSlot.getStack().copy();
					}
				} else if (this.draggedStack.getCount() > 1 && slot != null && ContainerRadiantChest.canAddItemToSlot(slot, this.draggedStack, false)) {
					long i = Minecraft.getSystemTime();

					if (this.currentDragTargetSlot == slot) {
						if (i - this.dragItemDropDelay > 500L) {
							this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, ClickType.PICKUP);
							this.handleMouseClick(slot, slot.slotNumber, 1, ClickType.PICKUP);
							this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, ClickType.PICKUP);
							this.dragItemDropDelay = i + 750L;
							this.draggedStack.shrink(1);
						}
					} else {
						this.currentDragTargetSlot = slot;
						this.dragItemDropDelay = i;
					}
				}
			}
		} else if (this.dragSplitting && slot != null && !itemstack.isEmpty() && (itemstack.getCount() > this.dragSplittingSlots.size() || this.dragSplittingLimit == 2) && ContainerRadiantChest.canAddItemToSlot(slot, itemstack, true) && slot.isItemValid(itemstack) && this.inventorySlots.canDragIntoSlot(slot)) {
			this.dragSplittingSlots.add(slot);
			this.updateDragSplitting();
		}
	}

	@Override
	protected void mouseReleased (int mouseX, int mouseY, int state) {
		if (this.selectedButton != null && state == 0) {
			this.selectedButton.mouseReleased(mouseX, mouseY);
			this.selectedButton = null;
		}

		Slot slot = this.getSlotAtPosition(mouseX, mouseY);
		int i = this.guiLeft;
		int j = this.guiTop;
		boolean flag = this.hasClickedOutside(mouseX, mouseY, i, j);
		if (slot != null) {
			flag = false; // Forge, prevent dropping of items through slots outside of GUI boundaries
		}
		int k = -1;

		if (slot != null) {
			k = slot.slotNumber;
		}

		if (flag) {
			k = -999;
		}

		if (this.doubleClick && slot != null && state == 0 && this.inventorySlots.canMergeSlot(ItemStack.EMPTY, slot)) {
			if (isShiftKeyDown()) {
				if (!this.shiftClickedSlot.isEmpty()) {
					for (Slot slot2 : this.inventorySlots.inventorySlots) {
						if (slot2 != null && slot2.canTakeStack(this.mc.player) && slot2.getHasStack() && slot2.isSameInventory(slot) && ContainerRadiantChest.canAddItemToSlot(slot2, this.shiftClickedSlot, true)) {
							this.handleMouseClick(slot2, slot2.slotNumber, state, ClickType.QUICK_MOVE);
						}
					}
				}
			} else {
				this.handleMouseClick(slot, k, state, ClickType.PICKUP_ALL);
			}

			this.doubleClick = false;
			this.lastClickTime = 0L;
		} else {
			if (this.dragSplitting && this.dragSplittingButton != state) {
				this.dragSplitting = false;
				this.dragSplittingSlots.clear();
				this.ignoreMouseUp = true;
				return;
			}

			if (this.ignoreMouseUp) {
				this.ignoreMouseUp = false;
				return;
			}

			if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
				if (state == 0 || state == 1) {
					if (this.draggedStack.isEmpty() && slot != this.clickedSlot) {
						this.draggedStack = this.clickedSlot.getStack();
					}

					boolean flag2 = ContainerRadiantChest.canAddItemToSlot(slot, this.draggedStack, false);

					if (k != -1 && !this.draggedStack.isEmpty() && flag2) {
						this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state, ClickType.PICKUP);
						this.handleMouseClick(slot, k, 0, ClickType.PICKUP);

						if (this.mc.player.inventory.getItemStack().isEmpty()) {
							this.returningStack = ItemStack.EMPTY;
						} else {
							this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state, ClickType.PICKUP);
							this.touchUpX = mouseX - i;
							this.touchUpY = mouseY - j;
							this.returningStackDestSlot = this.clickedSlot;
							this.returningStack = this.draggedStack;
							this.returningStackTime = Minecraft.getSystemTime();
						}
					} else if (!this.draggedStack.isEmpty()) {
						this.touchUpX = mouseX - i;
						this.touchUpY = mouseY - j;
						this.returningStackDestSlot = this.clickedSlot;
						this.returningStack = this.draggedStack;
						this.returningStackTime = Minecraft.getSystemTime();
					}

					this.draggedStack = ItemStack.EMPTY;
					this.clickedSlot = null;
				}
			} else if (this.dragSplitting && !this.dragSplittingSlots.isEmpty()) {
				this.handleMouseClick((Slot) null, -999, Container.getQuickcraftMask(0, this.dragSplittingLimit), ClickType.QUICK_CRAFT);

				for (Slot slot1 : this.dragSplittingSlots) {
					this.handleMouseClick(slot1, slot1.slotNumber, Container.getQuickcraftMask(1, this.dragSplittingLimit), ClickType.QUICK_CRAFT);
				}

				this.handleMouseClick((Slot) null, -999, Container.getQuickcraftMask(2, this.dragSplittingLimit), ClickType.QUICK_CRAFT);
			} else if (!this.mc.player.inventory.getItemStack().isEmpty()) {
				if (this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(state - 100)) {
					this.handleMouseClick(slot, k, state, ClickType.CLONE);
				} else {
					boolean flag1 = k != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));

					if (flag1) {
						this.shiftClickedSlot = slot != null && slot.getHasStack() ? slot.getStack().copy() : ItemStack.EMPTY;
					}

					this.handleMouseClick(slot, k, state, flag1 ? ClickType.QUICK_MOVE : ClickType.PICKUP);
				}
			}
		}

		if (this.mc.player.inventory.getItemStack().isEmpty()) {
			this.lastClickTime = 0L;
		}

		this.dragSplitting = false;
	}

	private boolean isMouseOverSlot (Slot slotIn, int mouseX, int mouseY) {
		return this.isPointInRegion(slotIn.xPos, slotIn.yPos, 16, 16, mouseX, mouseY);
	}

	@Override
	protected void keyTyped (char typedChar, int keyCode) throws IOException {
		if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)) {
			this.mc.player.closeScreen();
		}

		if (nameBox.textboxKeyTyped(typedChar, keyCode)) {
			return;
		}

		this.checkHotbarKeys(keyCode);

		if (this.hoveredSlot != null && this.hoveredSlot.getHasStack()) {
			if (this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(keyCode)) {
				this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, 0, ClickType.CLONE);
			} else if (this.mc.gameSettings.keyBindDrop.isActiveAndMatches(keyCode)) {
				this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, ClickType.THROW);
			}
		}


	}

	@Override
	protected boolean checkHotbarKeys (int keyCode) {
		if (this.mc.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null) {
			for (int i = 0; i < 9; ++i) {
				if (this.mc.gameSettings.keyBindsHotbar[i].isActiveAndMatches(keyCode)) {
					this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, i, ClickType.SWAP);
					return true;
				}
			}
		}

		return false;
	}

	@Override
	@javax.annotation.Nullable
	public Slot getSlotUnderMouse () {
		return this.hoveredSlot;
	}

	@Override
	protected void handleMouseClick (Slot slotIn, int slotId, int mouseButton, ClickType type) {
		super.handleMouseClick(slotIn, slotId, mouseButton, type);
        /*if (slotIn != null) {
            slotId = slotIn.slotNumber;
        }
        
    	short short1 = this.mc.player.openContainer.getNextTransactionID(this.mc.player.inventory);
        ItemStack itemstack = this.mc.player.openContainer.slotClick(slotId, mouseButton, type, this.mc.player);
        INetHandler h = FMLClientHandler.instance().getClientPlayHandler();
        if (h != null && h instanceof NetHandlerPlayClient) {
        	//PacketHandler.INSTANCE.sendToServer(new MessageClickWindowExtended(this.inventorySlots.windowId, slotId, mouseButton, type, itemstack, short1));
        } else {
        	this.mc.playerController.windowClick(this.inventorySlots.windowId, slotId, mouseButton, type, this.mc.player);
        }
        */
	}

	@Override
	public void onGuiClosed () {
		super.onGuiClosed();

		if (tracked != null) {
			ManifestTracking.remove(dimension, pos);
		}
	}

	@Optional.Method(modid = "quark")
	@Override
	public boolean onAddChestButton (GuiButton button, int buttonType) {
		return true;
	}

	@Optional.Method(modid = "quark")
	@Override
	public void onSearchBarAdded (GuiTextField bar) {
		bar.y = (height / 2) + 2;
		bar.x = (width / 2) - bar.width / 2;
	}

	@Override
	public void setEntryValue (int id, boolean value) {
	}

	@Override
	public void setEntryValue (int id, float value) {
	}

	@Override
	public void setEntryValue (int id, String value) {
		container.setName(value);
	}
}
