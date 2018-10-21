package com.aranaira.arcanearchives.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.aranaira.arcanearchives.util.NetworkHelper;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GUIBookContainer extends GuiContainer 
{
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
	private List<Slot> mSlots = new ArrayList<Slot>();
	
	
	//If the player is currently entering text into the search bar.
	private boolean isEnteringText = false;
	
	//The search bar's text used for filtering the list of items on the network.
	private String SearchText = "";
	
	private NetworkContainer container;
	
	public GUIBookContainer(NetworkContainer container) {
		
		super(container);
		
		this.container = container;
		this.xSize = ImageWidth;
		this.ySize = ImageHeight;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		buttonList.clear();
		int offLeft = (width - ImageWidth) / 2 - 3;
		int offTop = 108;

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
	public void updateScreen() {
		//Sets the GUI Buttons to be usable.
		for (GuiButton button : buttonList)
		{
			button.visible = true;
		}
		
		
		
		super.updateScreen();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		// TODO Auto-generated method stub
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableColorMaterial();
		this.mc.getTextureManager().bindTexture(GUITextures);

		//Adjust these values to move locations of elements without individual adjustment
		int offLeft = (int) ((width - ImageWidth) / 2.0F) - 1;
		int offTop = 108;
		int topOffset = 103;

		drawModalRectWithCustomSizedTexture(offLeft, offTop, 0, 0, ImageScale,ImageScale,ImageScale,ImageScale);
		
		offLeft += 1;

		GlStateManager.disableLighting();
		

		//Decides what to display if the player has typed something in the search bar.
		if (SearchText.compareTo("") == 0)
			fontRenderer.drawString("Search", offLeft + 186 - 140, (154 - 130) + topOffset + 4, 0x000000);
		else
			fontRenderer.drawString(SearchText, offLeft + 186 - 140, (154 - 130) + topOffset + 4, 0x000000);

		
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		GlStateManager.enableLighting();
		
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode){
		//If the user is currently entering text into the search bar.
		if (isEnteringText)
		{
			//Backspace
			if (keyCode == 14)
			{
				if (SearchText.length() > 0)
					SearchText = SearchText.substring(0, SearchText.length() - 1);
			}
			//Escape and Enter
			else if (keyCode == 1 || keyCode == 28)
			{
				isEnteringText = false;
			}
			//Anything else.
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
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

		int offLeft = (int) ((width - ImageWidth) / 2.0F) - 1;
		int offTop = 108;
		int topOffset = 103;

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
	
	@Override
	public void onGuiClosed() {
		container.onContainerClosed(mc.player);
		super.onGuiClosed();
	}
	

	@SideOnly(Side.CLIENT)
   	static class GenericButton extends GuiButton
   	{
		public GenericButton(int x, int y, int width, int height, String text) {
			super(1, x, y, width, height, text);
		}
	}
}
