package com.aranaira.arcanearchives.client.screen;

import com.aranaira.arcanearchives.api.ArcaneArchivesAPI;
import com.aranaira.arcanearchives.api.crafting.ingredients.CollatedInfoPair;
import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientInfo;
import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientStack;
import com.aranaira.arcanearchives.api.inventory.slot.IRecipeSlot;
import com.aranaira.arcanearchives.api.reference.Constants;
import com.aranaira.arcanearchives.client.impl.InvisibleButton;
import com.aranaira.arcanearchives.core.blocks.entities.CrystalWorkbenchBlockEntity;
import com.aranaira.arcanearchives.core.inventory.container.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.core.network.Networking;
import com.aranaira.arcanearchives.core.recipes.CrystalWorkbenchRecipe;
import com.aranaira.arcanearchives.core.util.ArcaneRecipeUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.command.impl.LocateBiomeCommand;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import noobanidus.libs.noobutil.client.CycleTimer;

import java.util.Arrays;
import java.util.List;

public class CrystalWorkbenchScreen extends ContainerScreen<CrystalWorkbenchContainer> {
  private final CycleTimer timer;
  private final static ResourceLocation background = new ResourceLocation(ArcaneArchivesAPI.MODID, "textures/gui/crystal_workbench.png");

  public CrystalWorkbenchScreen(CrystalWorkbenchContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    imageWidth = 206;
    imageHeight = 254;
    timer = new CycleTimer(Constants.Interface.getCycleTimer());
  }

  @SuppressWarnings("ConstantConditions")
  protected void sendButtonClick(int index) {
    this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, index);
  }

  @Override
  protected void init() {
    super.init();
    this.addButton(new InvisibleButton(width / 2 - 80, height / 2 - 59, 15, 21, (val) -> sendButtonClick(1)));
    this.addButton(new InvisibleButton(width / 2 + 65, height / 2 - 59, 15, 21, (val) -> sendButtonClick(2)));
  }

  @SuppressWarnings("deprecation")
  @Override
  protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.enableBlend();
    this.minecraft.getTextureManager().bind(background);
    int i = this.leftPos;
    int j = this.topPos;
    blit(matrixStack, i, j, 0, 0, imageWidth, imageHeight, 256, 256);
  }

  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(matrixStack);
    super.render(matrixStack, mouseX, mouseY, partialTicks);
    RenderSystem.disableBlend();
    this.renderTooltip(matrixStack, mouseX, mouseY);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void renderTooltip(MatrixStack pPoseStack, int pX, int pY) {
    if (this.minecraft.player.inventory.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
      if (this.hoveredSlot instanceof IRecipeSlot && ((IRecipeSlot<CrystalWorkbenchRecipe>)this.hoveredSlot).getRecipe() != null) {
        this.renderRecipeTooltip(pPoseStack, (IRecipeSlot<CrystalWorkbenchRecipe>) this.hoveredSlot, pX, pY);
      } else {
        this.renderTooltip(pPoseStack, this.hoveredSlot.getItem(), pX, pY);
      }
    }
  }

  protected void renderRecipeTooltip(MatrixStack pMatrixStack, IRecipeSlot<CrystalWorkbenchRecipe> slot, int pMouseX, int pMouseY) {
    ItemStack pItemStack = slot.getSlot().getItem();
    FontRenderer font = pItemStack.getItem().getFontRenderer(pItemStack);
    net.minecraftforge.fml.client.gui.GuiUtils.preItemToolTip(pItemStack);
    List<ITextComponent> tooltip = this.getTooltipFromItem(pItemStack);
    CrystalWorkbenchRecipe recipe = slot.getRecipe();
    if (recipe == null) {
      throw new IllegalStateException("recipe for slot " + slot.getIndex() + " cannot be null at this stage");
    }
    tooltip.add(new StringTextComponent(""));
    // TODO: CACHING
    List<CollatedInfoPair> infoPairs = ArcaneRecipeUtil.getCollatedIngredientInfoPairs(recipe, menu.getWorkbench());
    boolean simple = !Screen.hasShiftDown();
    for (CollatedInfoPair pair : infoPairs) {
      IngredientInfo info = pair.getInfo();
      IngredientStack stack = pair.getIngredient();
      boolean found = info.getFound() >= info.getRequired();
      if (simple) {
        tooltip.add(new TranslationTextComponent("arcanearchives.tooltip.crystal_workbench.info", stack.getFirstStack().getHoverName(), new StringTextComponent(String.valueOf(info.getFound())).setStyle(Style.EMPTY.withColor(found ? TextFormatting.BLUE : TextFormatting.RED)), new StringTextComponent(String.valueOf(info.getRequired()))));
      } else {
        List<ItemStack> stacks = Arrays.asList(stack.getMatchingStacks());
        ItemStack thisStack = timer.getCycledItem(stacks);
        if (thisStack == null) {
          thisStack = ItemStack.EMPTY;
        }
        tooltip.add(new TranslationTextComponent("arcanearchives.tooltip.crystal_workbench.info", thisStack.getHoverName(), new StringTextComponent(String.valueOf(info.getFound())).setStyle(Style.EMPTY.withColor(found ? TextFormatting.BLUE : TextFormatting.RED)), new StringTextComponent(String.valueOf(info.getRequired()))));
      }
    }
    if (simple) {
      tooltip.add(new TranslationTextComponent("arcanearchives.tooltip.crystal_workbench.more_info").setStyle(Style.EMPTY.withBold(true).withColor(TextFormatting.DARK_GRAY)));
    }

    this.renderWrappedToolTip(pMatrixStack, tooltip, pMouseX, pMouseY, (font == null ? this.font : font));
    net.minecraftforge.fml.client.gui.GuiUtils.postItemToolTip();
  }

  @Override
  public void renderSlot(MatrixStack stack, Slot slot) {
    super.renderSlot(stack, slot);

    if (slot instanceof IRecipeSlot) {
      IRecipeSlot<?> recipeSlot = (IRecipeSlot<?>) slot;

      if (recipeSlot.isDimmed()) {
        this.minecraft.getTextureManager().bind(background);
        RenderSystem.disableDepthTest();
        fill(stack, slot.x, slot.y, slot.x + 16, slot.y + 16, Constants.CrystalWorkbench.UI.Overlay);
      } else if (recipeSlot.getIndex() == menu.getSelectedSlot()) {
        this.minecraft.getTextureManager().bind(background);
        RenderSystem.enableDepthTest();
        blit(stack, slot.x - 2, slot.y - 2, 206, 0, 20, 20);
      }
    }
  }



  @Override
  protected void renderLabels(MatrixStack matrixStack, int x, int y) {
    //super.renderLabels(matrixStack, x, y);

    for (Widget widget : this.buttons) {
      if (widget.isHovered()) {
        widget.renderToolTip(matrixStack, x - this.leftPos, y - this.topPos);
        break;
      }
    }

    CrystalWorkbenchBlockEntity entity = this.menu.getBlockEntity();
    if (entity == null) {
      return;
    }
/*    UUID id = entity.getEntityId();
    if (id != null) {
      this.font.draw(matrixStack, id.toString(), 1, 1, 0x000000);
    }*/
  }
}
