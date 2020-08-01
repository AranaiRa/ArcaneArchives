package com.aranaira.arcanearchives.containers;

import com.aranaira.arcanearchives.client.gui.framework.CustomCountSlot;
import com.aranaira.arcanearchives.client.gui.framework.IScrollabe;
import com.aranaira.arcanearchives.client.gui.framework.IScrollableContainer;
import com.aranaira.arcanearchives.client.gui.framework.ScrollEventManager;
import com.aranaira.arcanearchives.config.ManifestConfig;
import com.aranaira.arcanearchives.inventories.ManifestItemHandler;
import com.aranaira.arcanearchives.types.ManifestList;
import com.aranaira.arcanearchives.util.ManifestUtils.CollatedEntry;
import com.aranaira.arcanearchives.util.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ManifestContainer extends Container implements IScrollableContainer {
  // this must be a multiple of GRID_SPACING
  public static int SCROLL_STEP = 6;
  public static int GRID_SPACING = 18;
  public static int FIRST_CELL_X = 12;
  public static int FIRST_CELL_Y = 30;
  /// number of cells in x and y, for now this is square
  public static int NUM_CELLS = 9;

  private static class ManifestItemSlot extends CustomCountSlot implements IScrollabe {
    public int originalY;
    private boolean isEnabled;

    private void setIsEnabled() {
      isEnabled = (yPos > FIRST_CELL_Y - GRID_SPACING && yPos < FIRST_CELL_Y + (NUM_CELLS * GRID_SPACING));
    }

    public ManifestItemSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
      super(itemHandler, index, xPosition, yPosition);

      originalY = yPosition;
      setIsEnabled();
    }

    @Override
    public void updateY(int yOffset) {
      yPos = originalY - yOffset;
      setIsEnabled();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isEnabled() {
      if (getStack().isEmpty()) {
        return false;
      }
      return isEnabled;
    }
  }

  private ManifestItemHandler handler;
  private boolean serverSide;
  private EntityPlayer player;

  private ScrollEventManager scrollEventManager;
  final private List<ManifestItemSlot> manifestItemSlots;
  private int mMaxYOffset;

  public ManifestContainer(EntityPlayer playerIn) {
    this.serverSide = false; //ServerSide;
    this.player = playerIn;
    this.scrollEventManager = null;
    this.manifestItemSlots = new ArrayList<>();

/*    if (ServerSide) {
      serverNetwork = NetworkHelper.getServerNetwork(playerIn.getUniqueID(), playerIn.world);
      if (serverNetwork == null) {
        handler = new ManifestItemHandler(new ManifestList());
      } else {
        handler = serverNetwork.getManifestHandler();
      }
    } else {
      clientNetwork = DataHelper.getClientNetwork(this.player.getUniqueID());
      handler = clientNetwork.getManifestHandler();
    }*/
  }

  public void setScrollEventManager(ScrollEventManager scrollEventManager) {
    this.scrollEventManager = scrollEventManager;
    this.scrollEventManager.registerListener(this);
  }

  @Override
  public void registerScrollEventManager(ScrollEventManager scrollEventManager) {
    if (this.scrollEventManager != null) {
      this.scrollEventManager.setStepsPerPage(1);
      this.scrollEventManager.unregisterListener(this);
    }

    this.scrollEventManager = scrollEventManager;
    // number of steps per row times number of cells on a page
    this.scrollEventManager.setStepsPerPage((GRID_SPACING / SCROLL_STEP) * NUM_CELLS);
  }

  @Override
  public List<? extends IScrollabe> getScrollable() {
    return manifestItemSlots;
  }

  @Override
  public int getMaxYOffset() {
    return mMaxYOffset;
  }

  @Override
  protected Slot addSlotToContainer(Slot slotIn) {
    if (slotIn instanceof ManifestItemSlot) {
      manifestItemSlots.add((ManifestItemSlot) slotIn);
    }
    return super.addSlotToContainer(slotIn);
  }

  public void ensureCapacity(int capacity) {
    if (capacity > this.inventorySlots.size()) {
      // ceiling on number of rows we need - the NUM_CELLS displayed rows times the grid spacing
      // this means we want to
      mMaxYOffset = (((capacity + (NUM_CELLS - 1)) / NUM_CELLS) - NUM_CELLS) * GRID_SPACING;
      handler.setSlots(capacity);
      for (int i = this.inventoryItemStacks.size(); i < capacity; i++) {
        this.addSlotToContainer(new ManifestItemSlot(handler, i, (i % NUM_CELLS) * GRID_SPACING + FIRST_CELL_X, (i / NUM_CELLS) * GRID_SPACING + FIRST_CELL_Y));
      }

      if (scrollEventManager != null) {
        // ceiling on number of needed steps
        scrollEventManager.setNumSteps(Math.max(0, MathUtils.intDivisionCeiling(mMaxYOffset, SCROLL_STEP)));
      }
    }
  }

  @Nullable
  public CollatedEntry getEntry(int slotId) {
    return handler.getManifestEntryInSlot(slotId);
  }

  @Override
  @Nonnull
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
    if (this.serverSide) {
      return ItemStack.EMPTY;
    }

    CollatedEntry entry = handler.getManifestEntryInSlot(slotId);

    if (entry == null) {
      return ItemStack.EMPTY;
    }

    if (entry.descriptions.get(0).dimension != player.dimension) {
      return ItemStack.EMPTY;
    }

    //List<Vec3d> visPositions = entry.getVecPositions();
    //visPositions.forEach(k -> LineHandler.addLine(k, player.dimension));

    // TODO: Handle tracking
/*    if (dragType == 0) {
      ManifestTrackingUtils.add(entry);
    } else if (dragType == 1) {
      ManifestTrackingUtils.remove(entry);
    }*/

    if ((ManifestConfig.holdShift && !GuiScreen.isShiftKeyDown() && dragType == 0) || (!ManifestConfig.holdShift && GuiScreen.isShiftKeyDown() && dragType == 0)) {
      Minecraft mc = Minecraft.getMinecraft();
      mc.player.closeScreen();
    }

    return ItemStack.EMPTY;
  }

  @Override
  public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
    return true;
  }

  public void setSearchString(String SearchText) {
    handler.setSearchText(SearchText);
    handler.setSearchItem(null);
  }

  public String getSearchString() {
    if (handler.getSearchItem() != null) {
      return handler.getSearchItem().getDisplayName();
    }

    return handler.getSearchText();
  }

  public ManifestList.SortingType getSortingType() {
    return handler.getSortingType();
  }

  public ManifestList.SortingDirection getSortingDirection() {
    return handler.getSortingDirection();
  }

  public void setSortingType(ManifestList.SortingType type) {
    handler.setSortingType(type);
  }

  public void setSortingDirection(ManifestList.SortingDirection direction) {
    handler.setSortingDirection(direction);
  }

  // Potentially handle weird instances of putting stacks in slots
  // when those slots don't actually exist yet.
  @Override
  public void putStackInSlot(int slotID, ItemStack stack) {
    Slot slot = getSlot(slotID);
    if (slot != null) {
      super.putStackInSlot(slotID, stack);
    }
  }

  @Override
  @Nullable
  public Slot getSlot(int slotId) {
    try {
      return super.getSlot(slotId);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }
}
