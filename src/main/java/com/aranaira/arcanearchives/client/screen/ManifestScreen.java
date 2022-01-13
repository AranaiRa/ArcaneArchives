package com.aranaira.arcanearchives.client.screen;

import com.aranaira.arcanearchives.inventory.container.ManifestContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CreativeCraftingListener;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Objects;

@SuppressWarnings("ConstantConditions")
public class ManifestScreen extends ContainerScreen<ManifestContainer> {
  public static final Inventory TMP_INVENTORY = new Inventory(45);
  private float currentScroll;
  private boolean isScrolling;
  private TextFieldWidget searchField;
  private CreativeCraftingListener listener;
  private boolean ignoreTextInput;

  public ManifestScreen(PlayerEntity player) {
    super(new ManifestContainer(player), player.inventory, StringTextComponent.EMPTY);
    player.containerMenu = this.menu;
    this.passEvents = true;
    this.imageHeight = 136;
    this.imageWidth = 195;
  }

  @Override
  public void tick() {
    this.searchField.tick();
  }

  /**
   * Called when the mouse is clicked over a slot or outside the gui.
   */
  @Override
  protected void slotClicked(@Nullable Slot slotIn, int slotId, int mouseButton, ClickType type) {
  }

  @Override
  protected void init() {
    super.init();
    this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
    this.searchField = new TextFieldWidget(this.font, this.leftPos + 82, this.topPos + 6, 80, 9, new TranslationTextComponent("itemGroup.search"));
    this.searchField.setMaxLength(50);
    this.searchField.setBordered(false);
    this.searchField.setVisible(false);
    this.searchField.setTextColor(16777215);
    this.children.add(this.searchField);
    this.minecraft.player.inventoryMenu.removeSlotListener(this.listener);
    this.listener = new CreativeCraftingListener(this.minecraft);
    this.minecraft.player.inventoryMenu.addSlotListener(this.listener);
  }

  @Override
  public void resize(Minecraft minecraft, int width, int height) {
    String s = this.searchField.getValue();
    this.init(minecraft, width, height);
    this.searchField.setValue(s);
    if (!this.searchField.getValue().isEmpty()) {
      this.updateCreativeSearch();
    }

  }

  @Override
  public void removed() {
    super.removed();
    if (this.minecraft.player != null && this.minecraft.player.inventory != null) {
      this.minecraft.player.inventoryMenu.removeSlotListener(this.listener);
    }

    this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
  }

  @Override
  public boolean charTyped(char codePoint, int modifiers) {
    if (this.ignoreTextInput) {
      return false;
    } else {
      String s = this.searchField.getValue();
      if (this.searchField.charTyped(codePoint, modifiers)) {
        if (!Objects.equals(s, this.searchField.getValue())) {
          this.updateCreativeSearch();
        }

        return true;
      } else {
        return false;
      }
    }
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    this.ignoreTextInput = false;
    boolean flag = this.hoveredSlot.hasItem();
    boolean flag1 = InputMappings.getKey(keyCode, scanCode).getNumericKeyValue().isPresent();
    if (flag && flag1 && this.checkHotbarKeyPressed(keyCode, scanCode)) {
      this.ignoreTextInput = true;
      return true;
    } else {
      String s = this.searchField.getValue();
      if (this.searchField.keyPressed(keyCode, scanCode, modifiers)) {
        if (!Objects.equals(s, this.searchField.getValue())) {
          this.updateCreativeSearch();
        }

        return true;
      } else {
        return this.searchField.isFocused() && this.searchField.isVisible() && keyCode != 256 || super.keyPressed(keyCode, scanCode, modifiers);
      }
    }
  }

  @Override
  public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
    this.ignoreTextInput = false;
    return super.keyReleased(keyCode, scanCode, modifiers);
  }

  private void updateCreativeSearch() {
/*    (this.container).itemList.clear();

    //tab.fill(container.itemList);
    if (!this.searchField.getText().isEmpty()) {
      //TODO: Make this a SearchTree not a manual search
      String search = this.searchField.getText().toLowerCase(Locale.ROOT);
      java.util.Iterator<ItemStack> itr = container.itemList.iterator();
      while (itr.hasNext()) {
        ItemStack stack = itr.next();
        boolean matches = false;
        for (ITextComponent line : stack.getTooltip(this.minecraft.player, this.minecraft.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL)) {
          if (TextFormatting.getTextWithoutFormattingCodes(line.getString()).toLowerCase(Locale.ROOT).contains(search)) {
            matches = true;
            break;
          }
        }
        if (!matches)
          itr.remove();
      }
    }
    this.currentScroll = 0.0F;
    container.scrollTo(0.0F);

    String s = this.searchField.getText();
    if (s.isEmpty()) {
      for (Item item : Registry.ITEM) {
        item.fillItemGroup(ItemGroup.SEARCH, (this.container).itemList);
      }
    } else {
      ISearchTree<ItemStack> isearchtree;
      if (s.startsWith("#")) {
        s = s.substring(1);
        isearchtree = this.minecraft.getSearchTree(SearchTreeManager.TAGS);
        this.searchTags(s);
      } else {
        isearchtree = this.minecraft.getSearchTree(SearchTreeManager.ITEMS);
      }

      (this.container).itemList.addAll(isearchtree.search(s.toLowerCase(Locale.ROOT)));
    }

    this.currentScroll = 0.0F;
    this.container.scrollTo(0.0F);*/
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    return super.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    if (button == 0) {
      isScrolling = false;
    }
    return super.mouseReleased(mouseX, mouseY, button);
  }

  /**
   * returns (if you are not on the inventoryTab) and (the flag isn't set) and (you have more than 1 page of items)
   */
  private boolean needsScrollBars() {
    return true;
  }

  /**
   * Sets the current creative tab, restructuring the GUI as needed.
   */
  private void reset() {
    this.quickCraftSlots.clear();
    (this.menu).itemList.clear();
    //tab.fill((this.container).itemList);

    if (this.searchField != null) {
      if (true) {
        this.searchField.setVisible(true);
        this.searchField.setCanLoseFocus(false);
        this.searchField.setFocus(true);
        this.searchField.setValue("");
        this.searchField.setWidth(32); // find significant default
        this.searchField.x = this.leftPos + (82 /*default left*/ + 89 /*default width*/) - this.searchField.getWidth();
        this.updateCreativeSearch();
      } else {
        this.searchField.setVisible(false);
        this.searchField.setCanLoseFocus(true);
        this.searchField.setFocus(false);
        this.searchField.setValue("");
      }
    }

    this.currentScroll = 0.0F;
    this.menu.scrollTo(0.0F);
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
    if (!this.needsScrollBars()) {
      return false;
    } else {
      int i = ((this.menu).itemList.size() + 9 - 1) / 9 - 5;
      this.currentScroll = (float) ((double) this.currentScroll - delta / (double) i);
      this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
      this.menu.scrollTo(this.currentScroll);
      return true;
    }
  }

  @Override
  protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
    return mouseX < (double) guiLeftIn || mouseY < (double) guiTopIn || mouseX >= (double) (guiLeftIn + this.imageWidth) || mouseY >= (double) (guiTopIn + this.imageHeight);
  }

  @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
    if (this.isScrolling) {
      int i = this.topPos + 18;
      int j = i + 112;
      this.currentScroll = ((float) mouseY - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
      this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
      this.menu.scrollTo(this.currentScroll);
      return true;
    } else {
      return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }
  }

  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(matrixStack);
    super.render(matrixStack, mouseX, mouseY, partialTicks);
    this.renderTooltip(matrixStack, mouseX, mouseY);
  }

  @Override
  protected void renderTooltip(MatrixStack matrixStack, ItemStack itemStack, int mouseX, int mouseY) {
    super.renderTooltip(matrixStack, itemStack, mouseX, mouseY);
  }

  @Override
  protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

    this.searchField.render(matrixStack, x, y, partialTicks);
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    int i = this.leftPos + 175;
    int j = this.topPos + 18;
    int k = j + 112;
    // TODO: FIX THIS
    // SEARCH BAR NUBBIN
    this.minecraft.getTextureManager().bind(null);
    if (true) {
      this.blit(matrixStack, i, j + (int) ((float) (k - j - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
    }
  }
}
