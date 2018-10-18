package com.aranaira.arcanearchives.client;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.util.ItemComparison;
import com.aranaira.arcanearchives.util.NetworkHelper;
import com.sun.jna.platform.win32.WinUser.INPUT;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.Int;

/** @author SoggyMustache's GUI Creator (http://tools.soggymustache.net)*/
public class GUIBook extends GuiScreen {

	Minecraft mc = Minecraft.getMinecraft();
	private final int ImageHeight = 256, ImageWidth = 256, ImageScale = 256;
	private static final ResourceLocation GUITextures = new ResourceLocation("arcanearchives:textures/gui/requisition_items.png");

	//Buttons to switch interface.
	private GenericButton Tab1;
	private GenericButton Tab2;
	private GenericButton Tab3;
	
	//Sorting buttons.
	private GenericButton TopButton1;
	private GenericButton TopButton2;
	private GenericButton TopButton3;
	private GenericButton TopButton4;
	
	//Clears the crafting area and puts the items back into the storage if possible.
	private GenericButton ClearCrafting;
	
	//List of the slots for the player's inventory.
	private List<AASlot> mSlots = new ArrayList<AASlot>();
	
	//The currently held item in the GUI.
	private ItemStack HeldItem = null;
	
	//The crafting inventory required for crafting.
	private InventoryCrafting mInventoryCrafting;
	
	//Required for the crafting inventory, dont ask me.
	private final ContainerWorkbench eventHandler;
	
	//If the player is currently entering text into the search bar.
	private boolean isEnteringText = false;
	
	//The search bar's text used for filtering the list of items on the network.
	private String SearchText = "";

	public GUIBook()
	{
		//Creating the handler and crafting inventory.
		eventHandler = new ContainerWorkbench(mc.player.inventory, mc.world, new BlockPos(0,0,0));
		mInventoryCrafting = new InventoryCrafting(eventHandler, 3, 3);
		
		//Creates the slots for the players inventory.
		int i = 35;
		//Inventory.
		for (int y = 2; y > -1; y--)
		{
			for (int x = 8; x > -1; x--)
			{
				mSlots.add(new AASlot(mc.player.inventory, i, 45 + (18 * x), 167 + (18 * y)));
				i--;
			}
		}
		//Hotbar.
		for (int x = 8; x > -1; x--)
		{
			mSlots.add(new AASlot(mc.player.inventory, i, 45 + (18 * x), 225));
			i--;
		}

	}

	@Override
	public void initGui() 
	{
		//Adds the buttons to the interface.
		buttonList.clear();
		int offLeft = (width - ImageWidth) / 2 - 3;
		int offTop = -16;

		buttonList.add(TopButton1 = new GenericButton(offLeft + (285 - 140) + (14 / 3), 44 + offTop, 14, 12, ""));
		buttonList.add(TopButton2 = new GenericButton(offLeft + (303 - 140) + (14 / 3), 44 + offTop, 14, 12, ""));
		buttonList.add(TopButton3 = new GenericButton(offLeft + (321 - 140) + (14 / 3), 44 + offTop, 14, 12, ""));
		buttonList.add(TopButton4 = new GenericButton(offLeft + (339 - 140) + (14 / 3), 44 + offTop, 14, 12, ""));

		buttonList.add(Tab3 = new GenericButton(offLeft + (375 - 140) + (15 / 3), 124 + offTop, 15, 40, ""));
		buttonList.add(Tab2 = new GenericButton(offLeft + (374 - 140) + (20 / 3), 40 + offTop, 20, 40, ""));
		buttonList.add(Tab1 = new GenericButton(offLeft + (375 - 140) + (15 / 3), 82 + offTop, 15, 40, ""));
		

		buttonList.add(ClearCrafting = new GenericButton(offLeft + (259 - 140) + (10 / 3), 116 + offTop, 10, 10, ""));
	}

	@Override
	public void updateScreen() 
	{
		//Sets the GUI Buttons to be usable.
		for (GuiButton button : buttonList)
		{
			button.visible = true;
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableColorMaterial();
		this.mc.getTextureManager().bindTexture(GUITextures);

		//Adjust these values to move locations of elements without individual adjustment
		int offLeft = (int) ((width - ImageWidth) / 2.0F);
		int offTop = 10;
		int topOffset = 3;

		drawModalRectWithCustomSizedTexture(offLeft, offTop, 0, 0, ImageScale,ImageScale,ImageScale,ImageScale);
		
		offLeft += 1;

		GlStateManager.disableLighting();
		
		//Pulls the list of items to be displayed.
		List<ItemStack> listOfItems;
		if (SearchText != "")
		{
			listOfItems = NetworkHelper.getArcaneArchivesNetwork(Minecraft.getMinecraft().player.getUniqueID()).GetFilteredItems(SearchText);
		}
		else
		{
			listOfItems = NetworkHelper.getArcaneArchivesNetwork(Minecraft.getMinecraft().player.getUniqueID()).GetAllItemsOnNetwork();
		}
		
		//Renders the networks inventory, that has been filtered.
		for (int y = 0; y < 2; y++)
		{
			for (int x = 0; x < 8; x++)
			{
				if (listOfItems.size() > (x + y * 9))
				{
					ItemStack s = listOfItems.get(x + y * 9);
					this.itemRender.renderItemAndEffectIntoGUI(s, offLeft + 45 + (20 * x), 40 + (18 * y) + topOffset);
					this.itemRender.renderItemOverlayIntoGUI(fontRenderer, s, offLeft + 45 + (20 * x), 40 + (18 * y) + topOffset, null);
				}
				else
					this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Block.getBlockById(0)), offLeft + 45 + (20 * x), 40 + (18 * y) + topOffset);
				
			}
		}
		
		//Crafting Area
		for (int x = 0; x < 3; x++)
		{
			for (int y = 0; y < 3; y++)
			{
				this.itemRender.renderItemAndEffectIntoGUI(mInventoryCrafting.getStackInRowAndColumn(x, y), offLeft + 63 + (18 * x), 98 + (18 * y) + topOffset);
				this.itemRender.renderItemOverlayIntoGUI(fontRenderer, mInventoryCrafting.getStackInRowAndColumn(x, y), offLeft + 63 + (18 * x), 98 + (18 * y)  + topOffset, null);
			}
		}
		
		//Crafting Area Result
		IRecipe ir = CraftingManager.findMatchingRecipe(mInventoryCrafting, mc.world);
		if (ir != null)
		{
			this.itemRender.renderItemAndEffectIntoGUI(ir.getCraftingResult(mInventoryCrafting), offLeft + 171, 116 + topOffset);
			this.itemRender.renderItemOverlayIntoGUI(fontRenderer, ir.getCraftingResult(mInventoryCrafting), offLeft + 171, 116 + topOffset, null);
		}
		
		//Render the slots for the players inventory.
		for (AASlot s : mSlots)
		{
			RenderSlot(mouseX, mouseY, s, offLeft, topOffset);
		}
		
		//Decides what to display if the player has typed something in the search bar.
		if (SearchText == "")
			fontRenderer.drawString("Search", offLeft + 186 - 140, (154 - 130) + topOffset + 4, 0x000000);
		else
			fontRenderer.drawString(SearchText, offLeft + 186 - 140, (154 - 130) + topOffset + 4, 0x000000);

		//Render Tooltips for the players Inventory.
		for (AASlot s : mSlots)
		{
			RenderSlotTooltip(mouseX, mouseY, s, offLeft, topOffset);
		}
		
		//Render Network Inventory Tooltips.
		for (int y = 0; y < 2; y++)
		{
			for (int x = 0; x < 8; x++)
			{
				if (listOfItems.size() > (x + y * 9))
				{
					ItemStack s = listOfItems.get(x + y * 9);
					if (mouseX > offLeft + 45 + (20 * x) && mouseX < offLeft + 45 + (20 * x) + 16 && mouseY > topOffset + 40 + (18 * y) && mouseY < topOffset + 40 + (18 * y) + 16)
						this.renderToolTip(s, mouseX, mouseY);
				}
				
			}
		}
		
		//Crafting Area Tooltips
		for (int x = 0; x < 3; x++)
		{
			for (int y = 0; y < 3; y++)
			{
				if (!mInventoryCrafting.getStackInRowAndColumn(x, y).isEmpty())
					if (mouseX > offLeft + 63 + (18 * x) && mouseX < offLeft + 63 + (18 * x) + 16 && mouseY > 98 + (18 * y) + topOffset && mouseY < 98 + (18 * y) + topOffset + 16)
						this.renderToolTip(mInventoryCrafting.getStackInRowAndColumn(x, y), mouseX, mouseY);
			}
		}
		
		//Crafting Result Tooltip
		if (ir != null)
		{
			if (mouseX > offLeft + 171 && mouseX < offLeft + 171 + 16 && mouseY > topOffset + 116 && mouseY < topOffset + 116 + 16)
				this.renderToolTip(ir.getCraftingResult(mInventoryCrafting), mouseX, mouseY);
		}
		
		if (HeldItem != null)
		{
			this.itemRender.renderItemAndEffectIntoGUI(HeldItem, mouseX, mouseY);
			this.itemRender.renderItemOverlayIntoGUI(fontRenderer, HeldItem, mouseX, mouseY, null);
		}
		
		GlStateManager.enableLighting();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	@Override
	protected void keyTyped(char typedChar, int keyCode){
		//ArcaneArchives.logger.info(keyCode);
		if (isEnteringText)
		{
			if (keyCode == 14)
			{
				if (SearchText.length() > 0)
					SearchText = SearchText.substring(0, SearchText.length() - 1);
			}
			else if (keyCode == 1 || keyCode == 28)
			{
				isEnteringText = false;
			}
			else
			{
				if (Character.isLetterOrDigit(typedChar))
					SearchText += typedChar;
			}
				
		}
		else
			if (keyCode == 1 || keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode())
				Minecraft.getMinecraft().player.closeScreen();
	}
	@Override
	protected void mouseClickMove(int parMouseX, int parMouseY, int parLastButtonClicked, long parTimeSinceMouseClick) 
	{ 
		
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

		int offLeft = (int) ((width - ImageWidth) / 2.0F) + 1;
		int offTop = 10;
		int topOffset = 3;

		HandleNetworkInventorySpace(mouseX, mouseY, offLeft, topOffset, mouseButton);
		HandlePlayerInventorySpace(mouseX, mouseY, offLeft, topOffset, mouseButton);
		HandleCraftingInventorySpace(mouseX, mouseY, offLeft, topOffset, mouseButton);
		
		if (mouseButton == 0)
		{
			//Checks Text Box Bounds
			if (mouseX > offLeft + 46 && mouseX < offLeft + 46 + 88 && mouseY > topOffset + 28 && mouseY < topOffset + 28 + 10)
			{
				isEnteringText = true;
			}
		}
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	
	//Helper Functons
	private void SlotSetItem(Slot s, ItemStack i)
	{
		s.inventory.setInventorySlotContents(s.getSlotIndex(), i);
	}
	
	private boolean InsideStorage(int mouseX, int mouseY)
	{
		int offLeft = (int) ((width - ImageWidth) / 2.0F) + 1;
		int offTop = 10;
		int topOffset = 3;
		if (mouseX > offLeft + 45 && mouseY > 43 && mouseX < offLeft + 45 + (20 * 8) + 16 && mouseY < 43 + (18 * 2) + 16)
		{
			return true;
		}
		return false;
	}

	private void RenderSlot(int mouseX, int mouseY, Slot s, int offLeft, int topOffset)
	{
		this.itemRender.renderItemAndEffectIntoGUI(s.inventory.getStackInSlot(s.getSlotIndex()), offLeft + s.xPos, s.yPos + topOffset);
		this.itemRender.renderItemOverlayIntoGUI(fontRenderer, s.inventory.getStackInSlot(s.getSlotIndex()), offLeft + s.xPos, s.yPos + topOffset, null);
	}
	
	private void RenderSlotTooltip(int mouseX, int mouseY, Slot s, int offLeft, int topOffset)
	{
		if (s.inventory.getStackInSlot(s.getSlotIndex()).isEmpty())
			return;
		if (mouseX > s.xPos + offLeft && mouseX < s.xPos + offLeft + 16 && mouseY > s.yPos + topOffset && mouseY < s.yPos + topOffset + 16)
		{
			this.renderToolTip(s.inventory.getStackInSlot(s.getSlotIndex()), mouseX, mouseY);
		}
	}
	
	private void HandleNetworkInventorySpace(int mouseX, int mouseY, int offLeft, int topOffset, int mouseButton)
	{
		switch (mouseButton) {
		case 0:
			if (mouseX > offLeft + 45 && mouseY > 43 && mouseX < offLeft + 45 + (20 * 8) + 16 && mouseY < 43 + (18 * 2) + 16)
			{
				if (HeldItem != null)
				{
					//Place item in the network.
					if (NetworkHelper.getArcaneArchivesNetwork(mc.player.getUniqueID()).AddItemToNetwork(HeldItem))
						HeldItem = null;
				}
				else
				{
					//Pick up item.
					Slot slot = null;
					for (AASlot s : mSlots) 
					{
						if (s.inventory.getStackInSlot(s.getSlotIndex()).isEmpty())
						{
							slot = s;
							break;
						}
					}
					for (int y = 0; y < 2; y++)
					{
						if (HeldItem != null && !isShiftKeyDown())
							break;
						for (int x = 0; x < 8; x++)
						{
							if (HeldItem != null && !isShiftKeyDown())
								break;
							if (NetworkHelper.getArcaneArchivesNetwork(Minecraft.getMinecraft().player.getUniqueID()).GetAllItemsOnNetwork().size() > (x + y * 9))
							{
								ItemStack s = NetworkHelper.getArcaneArchivesNetwork(Minecraft.getMinecraft().player.getUniqueID()).GetAllItemsOnNetwork().get(x + y * 9);
								if (isShiftKeyDown())
								{
									if (slot != null)
									{
										if (mouseX > offLeft + 45 + (20 * x) && mouseY > 43 + (18 * y) && mouseX < offLeft + 45 + (20 * x) + 16 && mouseY < 43 + (18 * y) + 16)
										{
											slot.inventory.setInventorySlotContents(slot.getSlotIndex(), NetworkHelper.getArcaneArchivesNetwork(mc.player.getUniqueID()).RemoveItemFromNetwork(s));
											return;
										}
									}
									else
										return;
								}
								else
									if (mouseX > offLeft + 45 + (20 * x) && mouseY > 43 + (18 * x) && mouseX < offLeft + 45 + (20 * 8) + 16 && mouseY < 43 + (18 * y) + 16)
									{
										HeldItem = NetworkHelper.getArcaneArchivesNetwork(mc.player.getUniqueID()).RemoveItemFromNetwork(s);
										return;
									}
							}
							
						}
					}
				}
			}
			else
			{
				//if (HeldItem != null)
				//	mc.player.entityDropItem(HeldItem, 5);
			}
		case 1:
			if (HeldItem != null)
			{
				if (InsideStorage(mouseX, mouseY))
				{
					ItemStack temp = HeldItem.copy();
					temp.setCount(1);
					if (NetworkHelper.getArcaneArchivesNetwork(mc.player.getUniqueID()).AddItemToNetwork(temp))
					{
						if (HeldItem.getCount() > 1)
							HeldItem.setCount(HeldItem.getCount() - 1);
						else
							HeldItem = null;
					}
				}
			}
			else
			{
				if (InsideStorage(mouseX, mouseY))
				{
					for (int y = 0; y < 2; y++)
					{
						for (int x = 0; x < 8; x++)
						{
							if (HeldItem != null)
								break;
							if (NetworkHelper.getArcaneArchivesNetwork(Minecraft.getMinecraft().player.getUniqueID()).GetAllItemsOnNetwork().size() > (x + y * 9))
							{
								ItemStack s = NetworkHelper.getArcaneArchivesNetwork(Minecraft.getMinecraft().player.getUniqueID()).GetAllItemsOnNetwork().get(x + y * 9);
								
								if (mouseX > offLeft + 45 + (20 * x) && mouseY > 43 + (18 * x) && mouseX < offLeft + 45 + (20 * 8) + 16 && mouseY < 43 + (18 * y) + 16)
								{
									HeldItem = NetworkHelper.getArcaneArchivesNetwork(mc.player.getUniqueID()).RemoveHalfStackFromNetwork(s);
									return;
								}
							}
							
						}
					}
				}
			}
		default:
			return;
		}
		
	}
	
	private void HandlePlayerInventorySpace(int mouseX, int mouseY, int offLeft, int topOffset, int mouseButton)
	{
		switch (mouseButton) {
		case 0:
			for (AASlot s : mSlots)
			{
				if (s.Contains(mouseX, mouseY, offLeft, topOffset))
				{
					ItemStack slotStack = s.inventory.getStackInSlot(s.getSlotIndex());
					if (isShiftKeyDown())
					{
						if (!slotStack.isEmpty())
						{
							if (NetworkHelper.getArcaneArchivesNetwork(mc.player.getUniqueID()).AddItemToNetwork(slotStack.copy()))
								SlotSetItem(s, new ItemStack(Block.getBlockById(0)));
						}
						return;
					}
					else
						if (HeldItem == null)
						{
							if (!slotStack.isEmpty())
							{
								HeldItem = slotStack;
								SlotSetItem(s, new ItemStack(Block.getBlockById(0)));
							}
						}
						else
						{
							if (slotStack.isEmpty())
							{
								SlotSetItem(s, HeldItem);
								HeldItem = null;
							}
							else
							{
								if (ItemComparison.AreItemsEqual(HeldItem, slotStack))
								{
									if (slotStack.getCount() + HeldItem.getCount() > HeldItem.getMaxStackSize())
									{
										int temp = HeldItem.getMaxStackSize() - slotStack.getCount();
										slotStack.setCount(HeldItem.getMaxStackSize());
										HeldItem.setCount(HeldItem.getCount() - temp);
									}
									else
									{
										slotStack.setCount(slotStack.getCount() + HeldItem.getCount());
										HeldItem = null;
									}
								}
								else
								{
									ItemStack temp = slotStack;
									SlotSetItem(s, HeldItem);
									HeldItem = temp;
								}
							}
						}
					return;
					
					
				}
			}
			return;
		case 1:
			if (HeldItem == null)
			{
				for (AASlot s : mSlots)
				{
					if (s.Contains(mouseX, mouseY, offLeft, topOffset))
					{
						ItemStack temp = s.getStack().copy();
						temp.setCount(s.getStack().getCount() / 2);
						s.getStack().setCount(s.getStack().getCount() - temp.getCount());
						HeldItem = temp;
					}
				}
			}
			else
			{
				for (AASlot s : mSlots)
				{
					if (s.Contains(mouseX, mouseY, offLeft, topOffset))
					{
						if (ItemComparison.AreItemsEqual(s.getStack(), HeldItem))
						{
							if (s.getStack().getMaxStackSize() > s.getStack().getCount())
							{
								s.getStack().setCount(s.getStack().getCount() + 1);
								HeldItem.setCount(HeldItem.getCount() - 1);
								if (HeldItem.isEmpty())
								{
									HeldItem = null;
								}
								return;
							}
						}
						else
						{
							if (s.getStack().isEmpty())
							{
								ItemStack temp = HeldItem.copy();
								temp.setCount(1);
								s.inventory.setInventorySlotContents(s.getSlotIndex(), temp);
								HeldItem.setCount(HeldItem.getCount() - 1);
								if (HeldItem.isEmpty())
									HeldItem = null;
								return;
							}
						}
					}
				}
			}
			return;
		default:
			return;
		}
	}
	
	private void HandleCraftingInventorySpace(int mouseX, int mouseY, int offLeft, int topOffset, int mouseButton)
	{
		switch (mouseButton) {
		case 0:
			for (int y = 0; y < 3; y++)
			{
				for (int x = 0; x < 3; x++)
				{
					ItemStack slotItem = mInventoryCrafting.getStackInRowAndColumn(x, y);
					if (mouseX > offLeft + 63 + (18 * x) && mouseX < offLeft + 63 + (18 * x) + 16 && mouseY > topOffset + 98 + (18 * y) && mouseY < topOffset + 98 + (18 * y) + 16)
					{
						if (isShiftKeyDown())
						{
							
						}
						if (HeldItem == null)
						{
							
							if (slotItem.isEmpty())
								return;
							HeldItem = slotItem;
							mInventoryCrafting.setInventorySlotContents(y * 3 + x, new ItemStack(Block.getBlockById(0)));
							return;
						}
						else
						{
							if (mInventoryCrafting.getStackInRowAndColumn(x, y).isEmpty())
							{
								mInventoryCrafting.setInventorySlotContents(y * 3 + x, HeldItem);
								HeldItem = null;
							}
							else
							{
								if (ItemStack.areItemStackTagsEqual(HeldItem, slotItem))
								{
									if (slotItem.getCount() < 64)
									{
										if (slotItem.getCount() + HeldItem.getCount() > HeldItem.getMaxStackSize())
										{
											int temp = HeldItem.getMaxStackSize() - slotItem.getCount();
											slotItem.setCount(64);
											HeldItem.setCount(HeldItem.getCount() - temp);
										}
										else
										{
											slotItem.setCount(slotItem.getCount() + HeldItem.getCount());
											HeldItem = null;
										}
									}
								}
								else
								{
									ItemStack temp = slotItem;
									mInventoryCrafting.setInventorySlotContents(y * 3 + x, HeldItem);
									HeldItem = temp;
								}
							}
							return;
						}
					}
				}
			}
			
			IRecipe ir = CraftingManager.findMatchingRecipe(mInventoryCrafting, mc.world);
			if (mouseX > offLeft + 171 && mouseX < offLeft + 171 + 16 && mouseY > topOffset + 116 && mouseY < topOffset + 116 + 16)
			{
				if (ir == null)
					return;
				else
				{
					if (HeldItem == null)
					{
						HeldItem = ir.getCraftingResult(mInventoryCrafting);
						for (int x = 0; x < 9; x++)
						{
							mInventoryCrafting.decrStackSize(x, 1);
						}
						return;
					}
					else if (ItemStack.areItemStackTagsEqual(HeldItem, ir.getCraftingResult(mInventoryCrafting)))
					{
						if (HeldItem.getMaxStackSize() >= HeldItem.getCount() + ir.getCraftingResult(mInventoryCrafting).getCount())
						{
							HeldItem.setCount(HeldItem.getCount() + ir.getCraftingResult(mInventoryCrafting).getCount());
							for (int x = 0; x < 9; x++)
							{
								mInventoryCrafting.decrStackSize(x, 1);
							}
						}
						return;
					}
				}
			}
		case 1:
			if (HeldItem != null)
			{
				for (int y = 0; y < 3; y++)
				{
					for (int x = 0; x < 3; x++)
					{
						if (mouseX > offLeft + 63 + (18 * x) && mouseX < offLeft + 63 + (18 * x) + 16 && mouseY > topOffset + 98 + (18 * y) && mouseY < topOffset + 98 + (18 * y) + 16)
						{
							if (mInventoryCrafting.getStackInRowAndColumn(x, y).isEmpty())
							{
								ItemStack temp = HeldItem.copy();
								temp.setCount(1);
								mInventoryCrafting.setInventorySlotContents(y * 3 + x, temp);
								HeldItem.setCount(HeldItem.getCount() - 1);
								if (HeldItem.getCount() == 0)
								{
									HeldItem = null;
								}
							}
							else
							{
								if (ItemStack.areItemStackTagsEqual(HeldItem, mInventoryCrafting.getStackInRowAndColumn(x, y)))
								{
									if (mInventoryCrafting.getStackInRowAndColumn(x, y).getCount() < mInventoryCrafting.getStackInRowAndColumn(x, y).getMaxStackSize())
									{
										mInventoryCrafting.getStackInRowAndColumn(x, y).setCount(mInventoryCrafting.getStackInRowAndColumn(x, y).getCount() + 1);
										HeldItem.setCount(HeldItem.getCount() - 1);
										if (HeldItem.getCount() == 0)
										{
											HeldItem = null;
										}
									}
								}
							}
							return;
						}
					}
				}
			}
			else
			{
				for (int y = 0; y < 3; y++)
				{
					for (int x = 0; x < 3; x++)
					{
						ItemStack slotItem = mInventoryCrafting.getStackInRowAndColumn(x, y);
						if (mouseX > offLeft + 63 + (18 * x) && mouseX < offLeft + 63 + (18 * x) + 16 && mouseY > topOffset + 98 + (18 * y) && mouseY < topOffset + 98 + (18 * y) + 16)
						{
							if (slotItem.isEmpty())
								return;
							if (slotItem.getCount() == 1)
							{
								HeldItem = slotItem;
								mInventoryCrafting.setInventorySlotContents(y * 3 + x, new ItemStack(Block.getBlockById(0)));
								return;
							}
							ItemStack temp = slotItem.copy();
							temp.setCount(temp.getCount() / 2);
							//mInventoryCrafting.setInventorySlotContents(y * 3 + x, new ItemStack(Block.getBlockById(0)));
							slotItem.setCount(slotItem.getCount() - temp.getCount());
							mInventoryCrafting.setInventorySlotContents(y * 3 + x, slotItem);
							HeldItem = temp;
							return;
						}
					}
				}
			}
			return;
		default:
			return;
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		//Within the if statements write your button actions (if you have buttons)
		if (button == Tab1)
		{
			
		}
		if (button == Tab2)
		{
			
		}
		if (button == Tab3)
		{
			
		}
		if (button == ClearCrafting)
		{
			for (int x = 0; x < 3; x++)
			{
				for (int y = 0; y < 3; y++)
				{
					
					if (NetworkHelper.getArcaneArchivesNetwork(mc.player.getUniqueID()).AddItemToNetwork(mInventoryCrafting.getStackInRowAndColumn(x, y)))
					{
						mInventoryCrafting.setInventorySlotContents(y * 3 + x, new ItemStack(Blocks.AIR));
					}
				}
			}
		}
		if (button == TopButton1)
		{
			
		}
		if (button == TopButton2)
		{
			
		}
		if (button == TopButton3)
		{
			
		}
		if (button == TopButton4)
		{
			
		}
	}

	@Override
	public void onGuiClosed() 
	{ 
		if (HeldItem != null)
		{
			if (!NetworkHelper.getArcaneArchivesNetwork(mc.player.getUniqueID()).AddItemToNetwork(HeldItem))
			{
				mc.player.entityDropItem(HeldItem, 5);
			}
		}
	}

	@Override
	public boolean doesGuiPauseGame() 
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
   	static class GenericButton extends GuiButton
   	{
		public GenericButton(int x, int y, int width, int height, String text) {
			super(1, x, y, width, height, text);
		}
	}
}
