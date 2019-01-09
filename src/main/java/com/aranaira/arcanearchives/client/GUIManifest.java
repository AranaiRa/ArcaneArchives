package com.aranaira.arcanearchives.client;

import java.io.IOException;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.common.ContainerManifest;
import com.aranaira.arcanearchives.util.handlers.AATickHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

public class GUIManifest extends GuiContainer
{

	private static final ResourceLocation GUITextures = new ResourceLocation("arcanearchives:textures/gui/manifest.png");

	private String mSearchText = "";
	
	private boolean mIsEnteringText = false;
	
	private ContainerManifest mContainer;
	
	private int mTextTopOffset = 14;
	private int mTextLeftOffset = 13;

	private int mEndTrackingLeftOffset = 67;
	private int mEndTrackingTopOffset = 202;
	
	private int mEndTrackingButtonLeftOffset = 65;
	private int mEndTrackingButtonTopOffset = 200;
	private int mEndTrackingButtonWidth = 54;
	private int mEndTrackingButtonHeight = 14;
	
	public GUIManifest(EntityPlayer player, ContainerManifest container)
	{
		super(container);
		
		mContainer = container;
		
		this.xSize 	= 184;
		this.ySize = 224;
		
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		// TODO Auto-generated method stub
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		this.renderHoveredToolTip(mouseX, mouseY);

		String temp = fontRenderer.trimStringToWidth(mSearchText, 6 * 15, true);

		if (mSearchText.equals(""))
			fontRenderer.drawString("Search", 	guiLeft + mTextLeftOffset, mTextTopOffset + guiTop, 0x000000);
		else
			fontRenderer.drawString(temp, guiLeft + mTextLeftOffset, mTextTopOffset + guiTop, 0x000000);
		
		fontRenderer.drawString("End Track", guiLeft + mEndTrackingLeftOffset, mEndTrackingTopOffset + guiTop, 0x000000);
	}
	
	@Override
	protected void renderHoveredToolTip(int p_191948_1_, int p_191948_2_) {
		super.renderHoveredToolTip(p_191948_1_, p_191948_2_);
	}
	
	//TODO Figure out how to display what chest the item is in.
	@Override
	public List<String> getItemToolTip(ItemStack p_191927_1_) {
		List<String> ls = super.getItemToolTip(p_191927_1_);
		//ls.add("TEST");
		return ls;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) 
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableColorMaterial();
		this.mc.getTextureManager().bindTexture(GUITextures);

		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, 256,256,256,256);
		
	}
	
	@Override
	public boolean doesGuiPauseGame() 
	{
		return false;
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		// TODO Auto-generated method stub
		super.actionPerformed(button);
	}
	

	@Override
	protected void keyTyped(char typedChar, int keyCode){
		//If the user is currently entering text into the search bar.
		if (mIsEnteringText)
		{
			//Backspace
			if (keyCode == 14)
			{
				if (mSearchText.length() > 0)
					mSearchText = mSearchText.substring(0, mSearchText.length() - 1);
			}
			//Escape and Enter
			else if (keyCode == 1 || keyCode == 28)
			{
				mIsEnteringText = false;
			}
			//Anything else.
			else
			{
				if (Character.isLetterOrDigit(typedChar))
					mSearchText += typedChar;
				else if (typedChar == ' ')
					mSearchText += typedChar;
			}
			mContainer.SetSearchString(mSearchText);
		}
		else
			if (keyCode == 1 || keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode())
				Minecraft.getMinecraft().player.closeScreen();
	}
	

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		
		if (mouseButton == 0)
		{
			//Checks Text Box Bounds
			if (mouseX > guiLeft + mTextLeftOffset && mouseX < guiLeft + mTextLeftOffset + 88 && mouseY > guiTop + mTextTopOffset && mouseY < guiTop + mTextTopOffset + 10)
			{
				mIsEnteringText = true;
			}
			else
			{
				mIsEnteringText = false;
			}
			
			if (mouseX > guiLeft + mEndTrackingButtonLeftOffset && mouseX < guiLeft + mEndTrackingButtonLeftOffset + mEndTrackingButtonWidth && mouseY > guiTop + mEndTrackingButtonTopOffset && mouseY < guiTop + mEndTrackingButtonTopOffset + mEndTrackingButtonHeight)
			{
				AATickHandler.GetInstance().mIsDrawingLine = false;
			}
		}
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}
