/*package com.aranaira.arcanearchives.client.screen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.settings.CreativeSettings;
import net.minecraft.client.settings.HotbarSnapshot;
import net.minecraft.client.util.ISearchTree;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.client.util.SearchTreeManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITagCollection;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

@OnlyIn(Dist.CLIENT)
public class ManifestScreen extends DisplayEffectsScreen<ManifestScreen.ManifestContainer> {
  *//**
   * The location of the creative inventory tabs texture
   *//*
  @SuppressWarnings("unused")
  private static final ResourceLocation CREATIVE_INVENTORY_TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
  private static final Inventory TMP_INVENTORY = new Inventory(45);
  private static final ITextComponent BIN_SLOT_TEXT = new TranslationTextComponent("inventory.binSlot");
  *//**
   * Currently selected creative inventory tab index.
   *//*
  private static int selectedTabIndex = ItemGroup.BUILDING_BLOCKS.getIndex();
  *//**
   * Amount scrolled in Manifest mode inventory (0 = top, 1 = bottom)
   *//*
  private float currentScroll;
  *//**
   * True if the scrollbar is being dragged
   *//*
  private boolean isScrolling;
  private TextFieldWidget searchField;
  @Nullable
  private List<Slot> originalSlots;
  @Nullable
  private Slot destroyItemSlot;
  private ManifestCraftingListener listener;
  private boolean maybeSearching;
  private static int tabPage = 0;
  private int maxPages = 0;
  private boolean clickedOutside;
  private final Map<ResourceLocation, ITag<Item>> tagSearchResults = Maps.newTreeMap();

  @SuppressWarnings("unused")
  public ManifestScreen(PlayerEntity player) {
    super(new ManifestScreen.ManifestContainer(player), player.inventory, StringTextComponent.EMPTY);
    player.openContainer = this.container;
    this.passEvents = true;
    this.ySize = 136;
    this.xSize = 195;
  }

  @Override
  public void tick() {
    //noinspection ConstantConditions
    if (!this.minecraft.playerController.isInCreativeMode()) {
      //noinspection ConstantConditions
      this.minecraft.displayGuiScreen(new InventoryScreen(this.minecraft.player));
    } else if (this.searchField != null) {
      this.searchField.tick();
    }

  }

  *//**
   * Called when the mouse is clicked over a slot or outside the gui.
   *//*
  @Override
  @SuppressWarnings("ConstantConditions")
  protected void handleMouseClick(@Nullable Slot slotIn, int slotId, int mouseButton, @Nonnull ClickType type) {
    if (this.hasTmpInventory(slotIn)) {
      this.searchField.setCursorPositionEnd();
      this.searchField.setSelectionPos(0);
    }

    boolean flag = type == ClickType.QUICK_MOVE;
    type = slotId == -999 && type == ClickType.PICKUP ? ClickType.THROW : type;
    if (slotIn == null && selectedTabIndex != ItemGroup.INVENTORY.getIndex() && type != ClickType.QUICK_CRAFT) {
      PlayerInventory playerinventory1 = this.minecraft.player.inventory;
      if (!playerinventory1.getItemStack().isEmpty() && this.clickedOutside) {
        if (mouseButton == 0) {
          this.minecraft.player.dropItem(playerinventory1.getItemStack(), true);
          this.minecraft.playerController.sendPacketDropItem(playerinventory1.getItemStack());
          playerinventory1.setItemStack(ItemStack.EMPTY);
        }

        if (mouseButton == 1) {
          ItemStack itemstack6 = playerinventory1.getItemStack().split(1);
          this.minecraft.player.dropItem(itemstack6, true);
          this.minecraft.playerController.sendPacketDropItem(itemstack6);
        }
      }
    } else {
      if (slotIn != null && !slotIn.canTakeStack(this.minecraft.player)) {
        return;
      }

      if (slotIn == this.destroyItemSlot && flag) {
        for (int j = 0; j < this.minecraft.player.container.getInventory().size(); ++j) {
          this.minecraft.playerController.sendSlotPacket(ItemStack.EMPTY, j);
        }
      } else if (selectedTabIndex == ItemGroup.INVENTORY.getIndex()) {
        if (slotIn == this.destroyItemSlot) {
          this.minecraft.player.inventory.setItemStack(ItemStack.EMPTY);
        } else if (type == ClickType.THROW && slotIn != null && slotIn.getHasStack()) {
          ItemStack itemstack = slotIn.decrStackSize(mouseButton == 0 ? 1 : slotIn.getStack().getMaxStackSize());
          ItemStack itemstack1 = slotIn.getStack();
          this.minecraft.player.dropItem(itemstack, true);
          this.minecraft.playerController.sendPacketDropItem(itemstack);
          this.minecraft.playerController.sendSlotPacket(itemstack1, ((ManifestScreen.ManifestSlot) slotIn).slot.slotNumber);
        } else if (type == ClickType.THROW && !this.minecraft.player.inventory.getItemStack().isEmpty()) {
          this.minecraft.player.dropItem(this.minecraft.player.inventory.getItemStack(), true);
          this.minecraft.playerController.sendPacketDropItem(this.minecraft.player.inventory.getItemStack());
          this.minecraft.player.inventory.setItemStack(ItemStack.EMPTY);
        } else {
          this.minecraft.player.container.slotClick(slotIn == null ? slotId : ((ManifestScreen.ManifestSlot) slotIn).slot.slotNumber, mouseButton, type, this.minecraft.player);
          this.minecraft.player.container.detectAndSendChanges();
        }
      } else if (type != ClickType.QUICK_CRAFT && slotIn.inventory == TMP_INVENTORY) {
        PlayerInventory playerinventory = this.minecraft.player.inventory;
        ItemStack itemstack5 = playerinventory.getItemStack();
        ItemStack itemstack7 = slotIn.getStack();
        if (type == ClickType.SWAP) {
          if (!itemstack7.isEmpty()) {
            ItemStack itemstack10 = itemstack7.copy();
            itemstack10.setCount(itemstack10.getMaxStackSize());
            this.minecraft.player.inventory.setInventorySlotContents(mouseButton, itemstack10);
            this.minecraft.player.container.detectAndSendChanges();
          }

          return;
        }

        if (type == ClickType.CLONE) {
          if (playerinventory.getItemStack().isEmpty() && slotIn.getHasStack()) {
            ItemStack itemstack9 = slotIn.getStack().copy();
            itemstack9.setCount(itemstack9.getMaxStackSize());
            playerinventory.setItemStack(itemstack9);
          }

          return;
        }

        if (type == ClickType.THROW) {
          if (!itemstack7.isEmpty()) {
            ItemStack itemstack8 = itemstack7.copy();
            itemstack8.setCount(mouseButton == 0 ? 1 : itemstack8.getMaxStackSize());
            this.minecraft.player.dropItem(itemstack8, true);
            this.minecraft.playerController.sendPacketDropItem(itemstack8);
          }

          return;
        }

        if (!itemstack5.isEmpty() && !itemstack7.isEmpty() && itemstack5.isItemEqual(itemstack7) && ItemStack.areItemStackTagsEqual(itemstack5, itemstack7)) {
          if (mouseButton == 0) {
            if (flag) {
              itemstack5.setCount(itemstack5.getMaxStackSize());
            } else if (itemstack5.getCount() < itemstack5.getMaxStackSize()) {
              itemstack5.grow(1);
            }
          } else {
            itemstack5.shrink(1);
          }
        } else if (!itemstack7.isEmpty() && itemstack5.isEmpty()) {
          playerinventory.setItemStack(itemstack7.copy());
          itemstack5 = playerinventory.getItemStack();
          if (flag) {
            itemstack5.setCount(itemstack5.getMaxStackSize());
          }
        } else if (mouseButton == 0) {
          playerinventory.setItemStack(ItemStack.EMPTY);
        } else {
          playerinventory.getItemStack().shrink(1);
        }
      } else if (this.container != null) {
        ItemStack itemstack3 = slotIn == null ? ItemStack.EMPTY : this.container.getSlot(slotIn.slotNumber).getStack();
        this.container.slotClick(slotIn == null ? slotId : slotIn.slotNumber, mouseButton, type, this.minecraft.player);
        if (Container.getDragEvent(mouseButton) == 2) {
          for (int k = 0; k < 9; ++k) {
            this.minecraft.playerController.sendSlotPacket(this.container.getSlot(45 + k).getStack(), 36 + k);
          }
        } else if (slotIn != null) {
          ItemStack itemstack4 = this.container.getSlot(slotIn.slotNumber).getStack();
          this.minecraft.playerController.sendSlotPacket(itemstack4, slotIn.slotNumber - (this.container).inventorySlots.size() + 9 + 36);
          int i = 45 + mouseButton;
          if (type == ClickType.SWAP) {
            this.minecraft.playerController.sendSlotPacket(itemstack3, i - (this.container).inventorySlots.size() + 9 + 36);
          } else if (type == ClickType.THROW && !itemstack3.isEmpty()) {
            ItemStack itemstack2 = itemstack3.copy();
            itemstack2.setCount(mouseButton == 0 ? 1 : itemstack2.getMaxStackSize());
            this.minecraft.player.dropItem(itemstack2, true);
            this.minecraft.playerController.sendPacketDropItem(itemstack2);
          }

          this.minecraft.player.container.detectAndSendChanges();
        }
      }
    }

  }

  private boolean hasTmpInventory(@Nullable Slot slotIn) {
    return slotIn != null && slotIn.inventory == TMP_INVENTORY;
  }

  @Override
  protected void updateActivePotionEffects() {
    int i = this.guiLeft;
    super.updateActivePotionEffects();
    if (this.searchField != null && this.guiLeft != i) {
      this.searchField.setX(this.guiLeft + 82);
    }

  }

  @Override
  @SuppressWarnings("ConstantConditions")
  protected void init() {
    if (this.minecraft.playerController.isInCreativeMode()) {
      super.init();
      int tabCount = ItemGroup.GROUPS.length;
      if (tabCount > 12) {
        addButton(new net.minecraft.client.gui.widget.button.Button(guiLeft, guiTop - 50, 20, 20, new StringTextComponent("<"), b -> tabPage = Math.max(tabPage - 1, 0)));
        addButton(new net.minecraft.client.gui.widget.button.Button(guiLeft + xSize - 20, guiTop - 50, 20, 20, new StringTextComponent(">"), b -> tabPage = Math.min(tabPage + 1, maxPages)));
        maxPages = (int) Math.ceil((tabCount - 12) / 10D);
      }
      this.minecraft.keyboardListener.enableRepeatEvents(true);
      this.searchField = new TextFieldWidget(this.font, this.guiLeft + 82, this.guiTop + 6, 80, 9, new TranslationTextComponent("itemGroup.search"));
      this.searchField.setMaxStringLength(50);
      this.searchField.setEnableBackgroundDrawing(false);
      this.searchField.setVisible(false);
      this.searchField.setTextColor(16777215);
      this.children.add(this.searchField);
      int i = selectedTabIndex;
      selectedTabIndex = -1;
      this.setCurrentManifestTab(ItemGroup.GROUPS[i]);
      this.minecraft.player.container.removeListener(this.listener);
      this.listener = new ManifestCraftingListener(this.minecraft);
      this.minecraft.player.container.addListener(this.listener);
    } else {
      this.minecraft.displayGuiScreen(new InventoryScreen(this.minecraft.player));
    }

  }

  @Override
  public void resize(@Nonnull Minecraft minecraft, int width, int height) {
    String s = this.searchField.getText();
    this.init(minecraft, width, height);
    this.searchField.setText(s);
    if (!this.searchField.getText().isEmpty()) {
      this.updateManifestSearch();
    }

  }

  @Override
  @SuppressWarnings("ConstantConditions")
  public void onClose() {
    super.onClose();
    if (this.minecraft.player != null && this.minecraft.player.inventory != null) {
      this.minecraft.player.container.removeListener(this.listener);
    }

    this.minecraft.keyboardListener.enableRepeatEvents(false);
  }

  @Override
  public boolean charTyped(char codePoint, int modifiers) {
    if (this.maybeSearching) {
      return false;
    } else if (!ItemGroup.GROUPS[selectedTabIndex].hasSearchBar()) {
      return false;
    } else {
      String s = this.searchField.getText();
      if (this.searchField.charTyped(codePoint, modifiers)) {
        if (!Objects.equals(s, this.searchField.getText())) {
          this.updateManifestSearch();
        }

        return true;
      } else {
        return false;
      }
    }
  }

  @Override
  @SuppressWarnings("ConstantConditions")
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    this.maybeSearching = false;
    if (!ItemGroup.GROUPS[selectedTabIndex].hasSearchBar()) {
      if (this.minecraft.gameSettings.keyBindChat.matchesKey(keyCode, scanCode)) {
        this.maybeSearching = true;
        this.setCurrentManifestTab(ItemGroup.SEARCH);
        return true;
      } else {
        return super.keyPressed(keyCode, scanCode, modifiers);
      }
    } else {
      boolean flag = !this.hasTmpInventory(this.hoveredSlot) || this.hoveredSlot.getHasStack();
      boolean flag1 = InputMappings.getInputByCode(keyCode, scanCode).func_241552_e_().isPresent();
      if (flag && flag1 && this.itemStackMoved(keyCode, scanCode)) {
        this.maybeSearching = true;
        return true;
      } else {
        String s = this.searchField.getText();
        if (this.searchField.keyPressed(keyCode, scanCode, modifiers)) {
          if (!Objects.equals(s, this.searchField.getText())) {
            this.updateManifestSearch();
          }

          return true;
        } else {
          return this.searchField.isFocused() && this.searchField.getVisible() && keyCode != 256 || super.keyPressed(keyCode, scanCode, modifiers);
        }
      }
    }
  }

  @Override
  public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
    this.maybeSearching = false;
    return super.keyReleased(keyCode, scanCode, modifiers);
  }

  @SuppressWarnings("ConstantConditions")
  private void updateManifestSearch() {
    (this.container).itemList.clear();
    this.tagSearchResults.clear();

    ItemGroup tab = ItemGroup.GROUPS[selectedTabIndex];
    if (tab.hasSearchBar() && tab != ItemGroup.SEARCH) {
      tab.fill(container.itemList);
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
      return;
    }

    String s = this.searchField.getText();
    if (s.isEmpty()) {
      for (Item item : ForgeRegistries.ITEMS) {
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
    this.container.scrollTo(0.0F);
  }

  private void searchTags(String search) {
    int i = search.indexOf(58);
    Predicate<ResourceLocation> predicate;
    if (i == -1) {
      predicate = (p_214084_1_) -> p_214084_1_.getPath().contains(search);
    } else {
      String s = search.substring(0, i).trim();
      String s1 = search.substring(i + 1).trim();
      predicate = (p_214081_2_) -> p_214081_2_.getNamespace().contains(s) && p_214081_2_.getPath().contains(s1);
    }

    ITagCollection<Item> itagcollection = ItemTags.getCollection();
    itagcollection.getRegisteredTags().stream().filter(predicate).forEach((p_214082_2_) -> this.tagSearchResults.put(p_214082_2_, itagcollection.get(p_214082_2_)));
  }

  @Override
  protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack matrixStack, int x, int y) {
    ItemGroup itemgroup = ItemGroup.GROUPS[selectedTabIndex];
    if (itemgroup != null && itemgroup.drawInForegroundOfTab()) {
      RenderSystem.disableBlend();
      this.font.func_243248_b(matrixStack, itemgroup.getGroupName(), 8.0F, 6.0F, itemgroup.getLabelColor());
    }

  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (button == 0) {
      double d0 = mouseX - (double) this.guiLeft;
      double d1 = mouseY - (double) this.guiTop;

      for (ItemGroup itemgroup : ItemGroup.GROUPS) {
        if (itemgroup != null && this.isMouseOverGroup(itemgroup, d0, d1)) {
          return true;
        }
      }

      if (selectedTabIndex != ItemGroup.INVENTORY.getIndex() && this.maybeOverScrollbars(mouseX, mouseY)) {
        this.isScrolling = this.needsScrollBars();
        return true;
      }
    }

    return super.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    if (button == 0) {
      double d0 = mouseX - (double) this.guiLeft;
      double d1 = mouseY - (double) this.guiTop;
      this.isScrolling = false;

      for (ItemGroup itemgroup : ItemGroup.GROUPS) {
        if (itemgroup != null && this.isMouseOverGroup(itemgroup, d0, d1)) {
          this.setCurrentManifestTab(itemgroup);
          return true;
        }
      }
    }

    return super.mouseReleased(mouseX, mouseY, button);
  }

  *//**
   * returns (if you are not on the inventoryTab) and (the flag isn't set) and (you have more than 1 page of items)
   *//*
  private boolean needsScrollBars() {
    if (ItemGroup.GROUPS[selectedTabIndex] == null) return false;
    return selectedTabIndex != ItemGroup.INVENTORY.getIndex() && ItemGroup.GROUPS[selectedTabIndex].hasScrollbar() && this.container.canScroll();
  }

  *//**
   * Sets the current creative tab, restructuring the GUI as needed.
   *//*
  @SuppressWarnings("ConstantConditions")
  private void setCurrentManifestTab(ItemGroup tab) {
    if (tab == null) return;
    int i = selectedTabIndex;
    selectedTabIndex = tab.getIndex();
    slotColor = tab.getSlotColor();
    this.dragSplittingSlots.clear();
    (this.container).itemList.clear();
    if (tab == ItemGroup.HOTBAR) {
      CreativeSettings creativesettings = this.minecraft.getCreativeSettings();

      for (int j = 0; j < 9; ++j) {
        HotbarSnapshot hotbarsnapshot = creativesettings.getHotbarSnapshot(j);
        if (hotbarsnapshot.isEmpty()) {
          for (int k = 0; k < 9; ++k) {
            if (k == j) {
              ItemStack itemstack = new ItemStack(Items.PAPER);
              itemstack.getOrCreateChildTag("CustomManifestLock");
              ITextComponent itextcomponent = this.minecraft.gameSettings.keyBindsHotbar[j].func_238171_j_();
              ITextComponent itextcomponent1 = this.minecraft.gameSettings.keyBindSaveToolbar.func_238171_j_();
              itemstack.setDisplayName(new TranslationTextComponent("inventory.hotbarInfo", itextcomponent1, itextcomponent));
              (this.container).itemList.add(itemstack);
            } else {
              (this.container).itemList.add(ItemStack.EMPTY);
            }
          }
        } else {
          (this.container).itemList.addAll(hotbarsnapshot);
        }
      }
    } else if (tab != ItemGroup.SEARCH) {
      tab.fill((this.container).itemList);
    }

    if (tab == ItemGroup.INVENTORY) {
      Container container = this.minecraft.player.container;
      if (this.originalSlots == null) {
        this.originalSlots = ImmutableList.copyOf((this.container).inventorySlots);
      }

      (this.container).inventorySlots.clear();

      for (int l = 0; l < container.inventorySlots.size(); ++l) {
        int i1;
        int j1;
        if (l >= 5 && l < 9) {
          int l1 = l - 5;
          int j2 = l1 / 2;
          int l2 = l1 % 2;
          i1 = 54 + j2 * 54;
          j1 = 6 + l2 * 27;
        } else if (l >= 0 && l < 5) {
          i1 = -2000;
          j1 = -2000;
        } else if (l == 45) {
          i1 = 35;
          j1 = 20;
        } else {
          int k1 = l - 9;
          int i2 = k1 % 9;
          int k2 = k1 / 9;
          i1 = 9 + i2 * 18;
          if (l >= 36) {
            j1 = 112;
          } else {
            j1 = 54 + k2 * 18;
          }
        }

        Slot slot = new ManifestScreen.ManifestSlot(container.inventorySlots.get(l), l, i1, j1);
        (this.container).inventorySlots.add(slot);
      }

      this.destroyItemSlot = new Slot(TMP_INVENTORY, 0, 173, 112);
      (this.container).inventorySlots.add(this.destroyItemSlot);
    } else if (i == ItemGroup.INVENTORY.getIndex()) {
      (this.container).inventorySlots.clear();
      (this.container).inventorySlots.addAll(this.originalSlots);
      this.originalSlots = null;
    }

    if (this.searchField != null) {
      if (tab.hasSearchBar()) {
        this.searchField.setVisible(true);
        this.searchField.setCanLoseFocus(false);
        this.searchField.setFocused2(true);
        if (i != tab.getIndex()) {
          this.searchField.setText("");
        }
        this.searchField.setWidth(tab.getSearchbarWidth());
        this.searchField.x = this.guiLeft + (82 *//*default left*//* + 89 *//*default width*//*) - this.searchField.getWidth();

        this.updateManifestSearch();
      } else {
        this.searchField.setVisible(false);
        this.searchField.setCanLoseFocus(true);
        this.searchField.setFocused2(false);
        this.searchField.setText("");
      }
    }

    this.currentScroll = 0.0F;
    this.container.scrollTo(0.0F);
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
    if (!this.needsScrollBars()) {
      return false;
    } else {
      int i = ((this.container).itemList.size() + 9 - 1) / 9 - 5;
      this.currentScroll = (float) ((double) this.currentScroll - delta / (double) i);
      this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
      this.container.scrollTo(this.currentScroll);
      return true;
    }
  }

  @Override
  protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
    boolean flag = mouseX < (double) guiLeftIn || mouseY < (double) guiTopIn || mouseX >= (double) (guiLeftIn + this.xSize) || mouseY >= (double) (guiTopIn + this.ySize);
    this.clickedOutside = flag && !this.isMouseOverGroup(ItemGroup.GROUPS[selectedTabIndex], mouseX, mouseY);
    return this.clickedOutside;
  }

  private boolean maybeOverScrollbars(double p_195376_1_, double p_195376_3_) {
    int i = this.guiLeft;
    int j = this.guiTop;
    int k = i + 175;
    int l = j + 18;
    int i1 = k + 14;
    int j1 = l + 112;
    return p_195376_1_ >= (double) k && p_195376_3_ >= (double) l && p_195376_1_ < (double) i1 && p_195376_3_ < (double) j1;
  }

  @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
    if (this.isScrolling) {
      int i = this.guiTop + 18;
      int j = i + 112;
      this.currentScroll = ((float) mouseY - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
      this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
      this.container.scrollTo(this.currentScroll);
      return true;
    } else {
      return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }
  }

  @Override
  public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(matrixStack);
    super.render(matrixStack, mouseX, mouseY, partialTicks);

    int start = tabPage * 10;
    int end = Math.min(ItemGroup.GROUPS.length, ((tabPage + 1) * 10) + 2);
    if (tabPage != 0) start += 2;
    boolean rendered = false;

    for (int x = start; x < end; x++) {
      ItemGroup itemgroup = ItemGroup.GROUPS[x];
      if (itemgroup != null && this.func_238809_a_(matrixStack, itemgroup, mouseX, mouseY)) {
        rendered = true;
        break;
      }
    }
    if (!rendered && !this.func_238809_a_(matrixStack, ItemGroup.SEARCH, mouseX, mouseY))
      this.func_238809_a_(matrixStack, ItemGroup.INVENTORY, mouseX, mouseY);

    if (this.destroyItemSlot != null && selectedTabIndex == ItemGroup.INVENTORY.getIndex() && this.isPointInRegion(this.destroyItemSlot.xPos, this.destroyItemSlot.yPos, 16, 16, (double) mouseX, (double) mouseY)) {
      this.renderTooltip(matrixStack, BIN_SLOT_TEXT, mouseX, mouseY);
    }

    if (maxPages != 0) {
      ITextComponent page = new StringTextComponent(String.format("%d / %d", tabPage + 1, maxPages + 1));
      RenderSystem.disableLighting();
      this.setBlitOffset(300);
      this.itemRenderer.zLevel = 300.0F;
      font.func_238407_a_(matrixStack, page.func_241878_f(), guiLeft + (xSize / 2.0f) - (font.getStringPropertyWidth(page) / 2.0f), guiTop - 44, -1);
      this.setBlitOffset(0);
      this.itemRenderer.zLevel = 0.0F;
    }

    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
  }

  @Override
  @SuppressWarnings("ConstantConditions")
  protected void renderTooltip(@Nonnull MatrixStack matrixStack, ItemStack itemStack, int mouseX, int mouseY) {
    if (selectedTabIndex == ItemGroup.SEARCH.getIndex()) {
      List<ITextComponent> list = itemStack.getTooltip(this.minecraft.player, this.minecraft.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);
      List<ITextComponent> list1 = Lists.newArrayList(list);
      Item item = itemStack.getItem();
      ItemGroup itemgroup = item.getGroup();
      if (itemgroup == null && item == Items.ENCHANTED_BOOK) {
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemStack);
        if (map.size() == 1) {
          Enchantment enchantment = map.keySet().iterator().next();

          for (ItemGroup itemgroup1 : ItemGroup.GROUPS) {
            if (itemgroup1.hasRelevantEnchantmentType(enchantment.type)) {
              itemgroup = itemgroup1;
              break;
            }
          }
        }
      }

      this.tagSearchResults.forEach((p_214083_2_, p_214083_3_) -> {
        if (p_214083_3_.contains(item)) {
          list1.add(1, (new StringTextComponent("#" + p_214083_2_)).mergeStyle(TextFormatting.DARK_PURPLE));
        }

      });
      if (itemgroup != null) {
        list1.add(1, itemgroup.getGroupName().deepCopy().mergeStyle(TextFormatting.BLUE));
      }

      net.minecraft.client.gui.FontRenderer font = itemStack.getItem().getFontRenderer(itemStack);
      net.minecraftforge.fml.client.gui.GuiUtils.preItemToolTip(itemStack);
      this.renderWrappedToolTip(matrixStack, list1, mouseX, mouseY, (font == null ? this.font : font));
      net.minecraftforge.fml.client.gui.GuiUtils.postItemToolTip();
    } else {
      super.renderTooltip(matrixStack, itemStack, mouseX, mouseY);
    }

  }

  @Override
  @SuppressWarnings("ConstantConditions")
  protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int x, int y) {
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    ItemGroup itemgroup = ItemGroup.GROUPS[selectedTabIndex];

    int start = tabPage * 10;
    int end = Math.min(ItemGroup.GROUPS.length, ((tabPage + 1) * 10 + 2));
    if (tabPage != 0) start += 2;

    for (int idx = start; idx < end; idx++) {
      ItemGroup itemgroup1 = ItemGroup.GROUPS[idx];
      if (itemgroup1 != null && itemgroup1.getIndex() != selectedTabIndex) {
        this.minecraft.getTextureManager().bindTexture(itemgroup1.getTabsImage());
        this.func_238808_a_(matrixStack, itemgroup1);
      }
    }

    if (tabPage != 0) {
      if (itemgroup != ItemGroup.SEARCH) {
        this.minecraft.getTextureManager().bindTexture(ItemGroup.SEARCH.getTabsImage());
        func_238808_a_(matrixStack, ItemGroup.SEARCH);
      }
      if (itemgroup != ItemGroup.INVENTORY) {
        this.minecraft.getTextureManager().bindTexture(ItemGroup.INVENTORY.getTabsImage());
        func_238808_a_(matrixStack, ItemGroup.INVENTORY);
      }
    }

    this.minecraft.getTextureManager().bindTexture(itemgroup.getBackgroundImage());
    this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    this.searchField.render(matrixStack, x, y, partialTicks);
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    int i = this.guiLeft + 175;
    int j = this.guiTop + 18;
    int k = j + 112;
    this.minecraft.getTextureManager().bindTexture(itemgroup.getTabsImage());
    if (itemgroup.hasScrollbar()) {
      this.blit(matrixStack, i, j + (int) ((float) (k - j - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
    }

    if ((itemgroup == null || itemgroup.getTabPage() != tabPage) && (itemgroup != ItemGroup.SEARCH && itemgroup != ItemGroup.INVENTORY))
      return;

    this.func_238808_a_(matrixStack, itemgroup);
    if (itemgroup == ItemGroup.INVENTORY) {
      InventoryScreen.drawEntityOnScreen(this.guiLeft + 88, this.guiTop + 45, 20, (float) (this.guiLeft + 88 - x), (float) (this.guiTop + 45 - 30 - y), this.minecraft.player);
    }

  }

  private boolean isMouseOverGroup(ItemGroup p_195375_1_, double p_195375_2_, double p_195375_4_) {
    if (p_195375_1_.getTabPage() != tabPage && p_195375_1_ != ItemGroup.SEARCH && p_195375_1_ != ItemGroup.INVENTORY)
      return false;
    int i = p_195375_1_.getColumn();
    int j = 28 * i;
    int k = 0;
    if (p_195375_1_.isAlignedRight()) {
      j = this.xSize - 28 * (6 - i) + 2;
    } else if (i > 0) {
      j += i;
    }

    if (p_195375_1_.isOnTopRow()) {
      k = k - 32;
    } else {
      k = k + this.ySize;
    }

    return p_195375_2_ >= (double) j && p_195375_2_ <= (double) (j + 28) && p_195375_4_ >= (double) k && p_195375_4_ <= (double) (k + 32);
  }

  private boolean func_238809_a_(MatrixStack p_238809_1_, ItemGroup p_238809_2_, int p_238809_3_, int p_238809_4_) {
    int i = p_238809_2_.getColumn();
    int j = 28 * i;
    int k = 0;
    if (p_238809_2_.isAlignedRight()) {
      j = this.xSize - 28 * (6 - i) + 2;
    } else if (i > 0) {
      j += i;
    }

    if (p_238809_2_.isOnTopRow()) {
      k = k - 32;
    } else {
      k = k + this.ySize;
    }

    if (this.isPointInRegion(j + 3, k + 3, 23, 27, (double) p_238809_3_, (double) p_238809_4_)) {
      this.renderTooltip(p_238809_1_, p_238809_2_.getGroupName(), p_238809_3_, p_238809_4_);
      return true;
    } else {
      return false;
    }
  }

  private void func_238808_a_(MatrixStack p_238808_1_, ItemGroup p_238808_2_) {
    boolean flag = p_238808_2_.getIndex() == selectedTabIndex;
    boolean flag1 = p_238808_2_.isOnTopRow();
    int i = p_238808_2_.getColumn();
    int j = i * 28;
    int k = 0;
    int l = this.guiLeft + 28 * i;
    int i1 = this.guiTop;
    int j1 = 32;
    if (flag) {
      k += 32;
    }

    if (p_238808_2_.isAlignedRight()) {
      l = this.guiLeft + this.xSize - 28 * (6 - i);
    } else if (i > 0) {
      l += i;
    }

    if (flag1) {
      i1 = i1 - 28;
    } else {
      k += 64;
      i1 = i1 + (this.ySize - 4);
    }

    RenderSystem.color3f(1F, 1F, 1F); //Forge: Reset color in case Items change it.
    RenderSystem.enableBlend(); //Forge: Make sure blend is enabled else tabs show a white border.
    this.blit(p_238808_1_, l, i1, j, k, 28, 32);
    this.itemRenderer.zLevel = 100.0F;
    l = l + 6;
    i1 = i1 + 8 + (flag1 ? 1 : -1);
    RenderSystem.enableRescaleNormal();
    ItemStack itemstack = p_238808_2_.getIcon();
    this.itemRenderer.renderItemAndEffectIntoGUI(itemstack, l, i1);
    this.itemRenderer.renderItemOverlays(this.font, itemstack, l, i1);
    this.itemRenderer.zLevel = 0.0F;
  }

  *//**
   * Returns the index of the currently selected tab.
   *//*
  public int getSelectedTabIndex() {
    return selectedTabIndex;
  }

  @SuppressWarnings("ConstantConditions")
  public static void handleHotbarSnapshots(Minecraft client, int index, boolean load, boolean save) {
    ClientPlayerEntity clientplayerentity = client.player;
    CreativeSettings creativesettings = client.getCreativeSettings();
    HotbarSnapshot hotbarsnapshot = creativesettings.getHotbarSnapshot(index);
    if (load) {
      for (int i = 0; i < PlayerInventory.getHotbarSize(); ++i) {
        ItemStack itemstack = hotbarsnapshot.get(i).copy();
        clientplayerentity.inventory.setInventorySlotContents(i, itemstack);
        client.playerController.sendSlotPacket(itemstack, 36 + i);
      }

      clientplayerentity.container.detectAndSendChanges();
    } else if (save) {
      for (int j = 0; j < PlayerInventory.getHotbarSize(); ++j) {
        hotbarsnapshot.set(j, clientplayerentity.inventory.getStackInSlot(j).copy());
      }

      ITextComponent itextcomponent = client.gameSettings.keyBindsHotbar[index].func_238171_j_();
      ITextComponent itextcomponent1 = client.gameSettings.keyBindLoadToolbar.func_238171_j_();
      client.ingameGUI.setOverlayMessage(new TranslationTextComponent("inventory.hotbarSaved", itextcomponent1, itextcomponent), false);
      creativesettings.save();
    }

  }

  @SuppressWarnings("WeakerAccess")
  @OnlyIn(Dist.CLIENT)
  public static class ManifestContainer extends Container {
    *//**
     * the list of items in this container
     *//*
    public final NonNullList<ItemStack> itemList = NonNullList.create();

    public ManifestContainer(PlayerEntity player) {
      super(null, 0);
      PlayerInventory playerinventory = player.inventory;

      for (int i = 0; i < 5; ++i) {
        for (int j = 0; j < 9; ++j) {
          this.addSlot(new ManifestScreen.LockedSlot(ManifestScreen.TMP_INVENTORY, i * 9 + j, 9 + j * 18, 18 + i * 18));
        }
      }

      for (int k = 0; k < 9; ++k) {
        this.addSlot(new Slot(playerinventory, k, 9 + k * 18, 112));
      }

      this.scrollTo(0.0F);
    }

    *//**
     * Determines whether supplied player can use this container
     *//*
    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity playerIn) {
      return true;
    }

    *//**
     * Updates the gui slots ItemStack's based on scroll position.
     *//*
    public void scrollTo(float pos) {
      int i = (this.itemList.size() + 9 - 1) / 9 - 5;
      int j = (int) ((double) (pos * (float) i) + 0.5D);
      if (j < 0) {
        j = 0;
      }

      for (int k = 0; k < 5; ++k) {
        for (int l = 0; l < 9; ++l) {
          int i1 = l + (k + j) * 9;
          if (i1 >= 0 && i1 < this.itemList.size()) {
            ManifestScreen.TMP_INVENTORY.setInventorySlotContents(l + k * 9, this.itemList.get(i1));
          } else {
            ManifestScreen.TMP_INVENTORY.setInventorySlotContents(l + k * 9, ItemStack.EMPTY);
          }
        }
      }

    }

    public boolean canScroll() {
      return this.itemList.size() > 45;
    }

    *//**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     *//*
    @Override
    @Nonnull
    public ItemStack transferStackInSlot(@Nonnull PlayerEntity playerIn, int index) {
      if (index >= this.inventorySlots.size() - 9 && index < this.inventorySlots.size()) {
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
          slot.putStack(ItemStack.EMPTY);
        }
      }

      return ItemStack.EMPTY;
    }

    *//**
     * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in
     * is null for the initial slot that was double-clicked.
     *//*
    @Override
    public boolean canMergeSlot(@Nonnull ItemStack stack, @Nonnull Slot slotIn) {
      return slotIn.inventory != ManifestScreen.TMP_INVENTORY;
    }

    *//**
     * Returns true if the player can "drag-spilt" items into this slot,. returns true by default. Called to check if
     * the slot can be added to a list of Slots to split the held ItemStack across.
     *//*
    @Override
    public boolean canDragIntoSlot(@Nonnull Slot slotIn) {
      return slotIn.inventory != ManifestScreen.TMP_INVENTORY;
    }
  }

  @SuppressWarnings("WeakerAccess")
  @OnlyIn(Dist.CLIENT)
  static class ManifestSlot extends Slot {
    private final Slot slot;

    public ManifestSlot(Slot p_i229959_1_, int p_i229959_2_, int p_i229959_3_, int p_i229959_4_) {
      super(p_i229959_1_.inventory, p_i229959_2_, p_i229959_3_, p_i229959_4_);
      this.slot = p_i229959_1_;
    }

    @Override
    @Nonnull
    public ItemStack onTake(@Nonnull PlayerEntity thePlayer, @Nonnull ItemStack stack) {
      return this.slot.onTake(thePlayer, stack);
    }

    *//**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     *//*
    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
      return this.slot.isItemValid(stack);
    }

    *//**
     * Helper fnct to get the stack in the slot.
     *//*
    @Override
    @Nonnull
    public ItemStack getStack() {
      return this.slot.getStack();
    }

    *//**
     * Returns if this slot contains a stack.
     *//*
    @Override
    public boolean getHasStack() {
      return this.slot.getHasStack();
    }

    *//**
     * Helper method to put a stack in the slot.
     *//*
    @Override
    public void putStack(@Nonnull ItemStack stack) {
      this.slot.putStack(stack);
    }

    *//**
     * Called when the stack in a Slot changes
     *//*
    @Override
    public void onSlotChanged() {
      this.slot.onSlotChanged();
    }

    *//**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the
     * case of armor slots)
     *//*
    @Override
    public int getSlotStackLimit() {
      return this.slot.getSlotStackLimit();
    }

    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack) {
      return this.slot.getItemStackLimit(stack);
    }

    @Override
    @Nullable
    public Pair<ResourceLocation, ResourceLocation> getBackground() {
      return this.slot.getBackground();
    }

    *//**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     *//*
    @Override
    @Nonnull
    public ItemStack decrStackSize(int amount) {
      return this.slot.decrStackSize(amount);
    }

    *//**
     * Actualy only call when we want to render the white square effect over the slots. Return always True, except for
     * the armor slot of the Donkey/Mule (we can't interact with the Undead and Skeleton horses)
     *//*
    @Override
    public boolean isEnabled() {
      return this.slot.isEnabled();
    }

    *//**
     * Return whether this slot's stack can be taken from this slot.
     *//*
    @Override
    public boolean canTakeStack(@Nonnull PlayerEntity playerIn) {
      return this.slot.canTakeStack(playerIn);
    }

    @Override
    public int getSlotIndex() {
      return this.slot.getSlotIndex();
    }

    @Override
    public boolean isSameInventory(Slot other) {
      return this.slot.isSameInventory(other);
    }

    @Override
    @Nonnull
    public Slot setBackground(@Nonnull ResourceLocation atlas, @Nonnull ResourceLocation sprite) {
      this.slot.setBackground(atlas, sprite);
      return this;
    }
  }

  @SuppressWarnings("WeakerAccess")
  @OnlyIn(Dist.CLIENT)
  static class LockedSlot extends Slot {
    public LockedSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
      super(inventoryIn, index, xPosition, yPosition);
    }

    *//**
     * Return whether this slot's stack can be taken from this slot.
     *//*
    @Override
    public boolean canTakeStack(@Nonnull PlayerEntity playerIn) {
      if (super.canTakeStack(playerIn) && this.getHasStack()) {
        return this.getStack().getChildTag("CustomManifestLock") == null;
      } else {
        return !this.getHasStack();
      }
    }
  }

  @SuppressWarnings("WeakerAccess")
  public class ManifestCraftingListener implements IContainerListener {
    private final Minecraft mc;

    public ManifestCraftingListener(Minecraft mc) {
      this.mc = mc;
    }

    *//**
     * update the crafting window inventory with the items in the list
     *//*
    @Override
    public void sendAllContents(@Nonnull Container containerToSend, @Nonnull NonNullList<ItemStack> itemsList) {
    }

    *//**
     * Sends the contents of an inventory slot to the client-side Container. This doesn't have to match the actual
     * contents of that slot.
     *//*
    @SuppressWarnings("ConstantConditions")
    @Override
    public void sendSlotContents(@Nonnull Container containerToSend, int slotInd, @Nonnull ItemStack stack) {
      this.mc.playerController.sendSlotPacket(stack, slotInd);
    }

    *//**
     * Sends two ints to the client-side Container. Used for furnace burning time, smelting progress, brewing progress,
     * and enchanting level. Normally the first int identifies which variable to update, and the second contains the new
     * value. Both are truncated to shorts in non-local SMP.
     *//*
    @Override
    public void sendWindowProperty(@Nonnull Container containerIn, int varToUpdate, int newValue) {
    }
  }
}*/
