package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.AAGuiHandler;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.inventory.handlers.ITroveItemHandler;
import com.aranaira.arcanearchives.inventory.handlers.OptionalUpgradesHandler;
import com.aranaira.arcanearchives.inventory.handlers.SizeUpgradeItemHandler;
import com.aranaira.arcanearchives.inventory.handlers.TroveUpgradeItemHandler;
import com.aranaira.arcanearchives.util.ItemUtils;
import com.aranaira.arcanearchives.types.enums.UpgradeType;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.UUID;

public class RadiantTroveTileEntity extends ImmanenceTileEntity implements IManifestTileEntity, IUpgradeableStorage, IBrazierRouting {
	private final TroveItemHandler inventory = new TroveItemHandler(this);
	private long lastClick = 0;
	private int lastTick = 0;
	private UUID lastUUID = null;
	public boolean wasCreativeDrop = false;

	public long getLastClick () {
		return lastClick;
	}

	public int getLastTick () {
		return lastTick;
	}

	public UUID getLastUUID () {
		return lastUUID;
	}

	public TroveUpgradeItemHandler getSizeUpgrades () {
		return sizeUpgrades;
	}

	public OptionalUpgradesHandler getOptionalUpgrades () {
		return optionalUpgrades;
	}

	private TroveUpgradeItemHandler sizeUpgrades = new TroveUpgradeItemHandler() {
		@Override
		public void onContentsChanged () {
			if (!RadiantTroveTileEntity.this.world.isRemote) {
				RadiantTroveTileEntity.this.markDirty();
				RadiantTroveTileEntity.this.defaultServerSideUpdate();
			}
			inventory.setUpgrades(getModifiedCapacity());
		}

		@Override
		public boolean canReduceMultiplierTo (int size) {
			RadiantTroveTileEntity te = RadiantTroveTileEntity.this;
			return te.inventory.getCount() <= te.inventory.getMaxCount(size);
		}
	};
	private OptionalUpgradesHandler optionalUpgrades = new OptionalUpgradesHandler();

	@Override
	public void update () {
		if (world.isRemote) {
			return;
		}

		defaultServerSideUpdate();
	}

	public RadiantTroveTileEntity () {
		super("radianttrove");
	}

	public void onRightClickTrove (EntityPlayer player) {
		ItemStack mainhand = player.getHeldItemMainhand();
		if (mainhand.isEmpty()) {
			mainhand = player.getHeldItemOffhand();
		}

		boolean fake_hand = false;

		if (mainhand.isEmpty()) {
			if (inventory.isEmpty()) {
				return;
			} else {
				mainhand = inventory.getItem();
				fake_hand = true;
			}
		}

		this.markDirty();

		if (inventory.isEmpty()) {
			inventory.setItem(mainhand);
		}

		ItemStack reference = inventory.getItem();

		if (!ItemUtils.areStacksEqualIgnoreSize(reference, mainhand) && !fake_hand) {
			player.sendStatusMessage(new TextComponentTranslation("arcanearchives.error.trove_insertion_failed.wrong"), true);
			return;
		}

		UUID playerId = player.getUniqueID();
		boolean doubleClick = false;

		if (lastUUID == playerId && (System.currentTimeMillis() - lastClick) <= 1500) {
			doubleClick = true;
		}

		lastUUID = playerId;
		lastClick = System.currentTimeMillis();

		ItemStack result;
		if (!fake_hand) {
			result = inventory.insertItem(0, mainhand, false);

			if (!result.isEmpty()) {
				player.sendStatusMessage(new TextComponentTranslation("arcanearchives.error.trove_insertion_failed.full"), true);
				mainhand.setCount(result.getCount());
				return;
			} else {
				mainhand.setCount(0);
			}
		}

		if (doubleClick) {
			IItemHandler playerMain = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
			if (playerMain != null) {
				for (int i = 0; i < playerMain.getSlots(); i++) {
					ItemStack inSlot = playerMain.getStackInSlot(i);
					if (ItemUtils.areStacksEqualIgnoreSize(reference, inSlot)) {
						result = inventory.insertItem(0, inSlot, true);
						if (!result.isEmpty()) {
							int diff = inSlot.getCount() - result.getCount();
							inventory.insertItem(0, playerMain.extractItem(i, diff, false), false);
							player.sendStatusMessage(new TextComponentTranslation("arcanearchives.error.trove_insertion_failed.full"), true);
							this.markDirty();
							return;
						} else {
							int thisCount = inSlot.getCount();
							inventory.insertItem(0, playerMain.extractItem(i, thisCount, false), false);
						}
					}
				}
			}
		}
	}

	@Override
	@Nonnull
	public SPacketUpdateTileEntity getUpdatePacket () {
		NBTTagCompound compound = writeToNBT(new NBTTagCompound());

		return new SPacketUpdateTileEntity(pos, 0, compound);
	}

	public boolean isEmpty () {
		return inventory.isEmpty();
	}

	public void onLeftClickTrove (EntityPlayer player) {
		if (world.isRemote) {
			return;
		}

		int curTick = world.getMinecraftServer().getTickCounter();
		if (curTick - lastTick < 3) {
			return;
		}
		lastTick = curTick;

		this.markDirty();

		int count = 1;

		ItemStack stack = inventory.extractItem(0, count, true);
		if (stack.isEmpty()) {
			return;
		}

		if (player.isSneaking()) {
			count = stack.getMaxStackSize();
		}

		stack = inventory.extractItem(0, count, false);

		EntityItem item = new EntityItem(world, player.posX, player.posY, player.posZ, stack);
		world.spawnEntity(item);
	}

	@Override
	public NBTTagCompound getUpdateTag () {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT (NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag(Tags.HANDLER_ITEM, this.inventory.serializeNBT());
		compound.setTag(Tags.SIZE_UPGRADES, this.sizeUpgrades.serializeNBT());
		compound.setTag(Tags.OPTIONAL_UPGRADES, this.optionalUpgrades.serializeNBT());

		return compound;
	}

	@Override
	public void readFromNBT (NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.inventory.deserializeNBT(compound.getCompoundTag(Tags.HANDLER_ITEM));
		this.sizeUpgrades.deserializeNBT(compound.getCompoundTag(Tags.SIZE_UPGRADES));
		this.optionalUpgrades.deserializeNBT(compound.getCompoundTag(Tags.OPTIONAL_UPGRADES));
	}

	@Override
	public void onDataPacket (NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
		super.onDataPacket(net, pkt);
	}

	@Override
	public boolean hasCapability (@Nonnull Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability (@Nonnull Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean isSingleStackInventory () {
		return true;
	}

	@Override
	public ItemStack getSingleStack () {
		if (isEmpty()) {
			return ItemStack.EMPTY;
		}

		ItemStack stack = inventory.getItem().copy();
		stack.setCount(inventory.getCount());
		return stack;
	}

	@Override
	public String getDescriptor () {
		return "Trove";
	}

	@Override
	public String getChestName () {
		return "";
	}

	@Override
	public TroveItemHandler getInventory () {
		return inventory;
	}

	@Override
	public SizeUpgradeItemHandler getSizeUpgradesHandler () {
		return sizeUpgrades;
	}

	@Override
	public OptionalUpgradesHandler getOptionalUpgradesHandler () {
		return optionalUpgrades;
	}

	@Override
	public int getModifiedCapacity () {
		return sizeUpgrades.getUpgradesCount();
	}

	@Override
	public boolean handleManipulationInterface (EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player.world.isRemote) return true;

		player.openGui(ArcaneArchives.instance, AAGuiHandler.UPGRADES, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	private Int2IntOpenHashMap result = new Int2IntOpenHashMap();

	@Override
	public Int2IntOpenHashMap getOrCalculateReference () {
		result.put(RecipeItemHelper.pack(inventory.getItem()), inventory.getCount());
		return result;
	}

	@Override
	public BrazierRoutingType getRoutingType () {
		return BrazierRoutingType.NO_NEW_STACKS;
	}

	@Override
	public int countEmptySlots () {
		return totalEmptySlots();
	}

	@Override
	public int totalEmptySlots () {
		return inventory.getMaxCount() - inventory.getCount();
	}

	@Override
	public int totalSlots () {
		return 1;
	}

	@Override
	public int slotMultiplier () {
		return 1;
	}

	@Override
	public ItemStack acceptStack (ItemStack stack) {
		ItemStack result = ItemHandlerHelper.insertItemStacked(this.inventory, stack, false);
		this.markDirty();
		return result;
	}

	@Override
	public int troveScore (ItemStack stack) {
		if (!ItemUtils.areStacksEqualIgnoreSize(stack, inventory.getItem())) return -1;

		if (!optionalUpgrades.hasUpgrade(UpgradeType.VOID)) return 350;

		if (inventory.getCount() < inventory.getMaxCount()) return 500;

		return 400;
	}

	public static class Tags {
		public static final String HANDLER_ITEM = "handler_item";
		public static final String SIZE_UPGRADES = "size_upgrades";
		public static final String OPTIONAL_UPGRADES = "optional_upgrades";

		private Tags () {
		}
	}

	public static class TroveItemHandler implements ITroveItemHandler, INBTSerializable<NBTTagCompound> {
		public static int BASE_COUNT = 64 * 512;
		private int upgrades = 0;
		private int count = 0;
		private ItemStack reference = ItemStack.EMPTY;
		private RadiantTroveTileEntity tile;

		public TroveItemHandler (RadiantTroveTileEntity tile) {
			this.tile = tile;
		}

		@Override
		public boolean isVoiding () {
			return this.tile.getOptionalUpgrades().hasUpgrade(UpgradeType.VOID);
		}

		@Override
		public void update () {
			this.tile.update();
		}

		@Override
		public int getSlots () {
			return 2;
		}

		public void setUpgrades (int upgrades) {
			this.upgrades = upgrades;
		}

		public int getUpgrades () {
			return upgrades;
		}

		@Override
		public int getMaxCount () {
			return getMaxCount(this.upgrades);
		}

		public int getMaxCount (int upgrades) {
			return BASE_COUNT * (upgrades + 1);
		}

		public ItemStack getItemCurrent () {
			if (count == 0) {
				return ItemStack.EMPTY;
			}

			return this.reference;
		}

		@Override
		public ItemStack getReference () {
			return this.reference;
		}

		@Override
		public int getCount () {
			return count;
		}

		@Override
		public void setCount (int count) {
			this.count = count;
		}

		@Override
		public ItemStack getItem () {
			return this.reference;
		}

		@Override
		public void setItem (ItemStack reference) {
			this.reference = reference.copy();
			this.reference.setCount(1);
			update();
		}

		@Override
		public boolean isEmpty () {
			return count == 0;
		}

		@Override
		public NBTTagCompound serializeNBT () {
			NBTTagCompound result = new NBTTagCompound();
			result.setInteger(Tags.COUNT, this.count);
			result.setTag(Tags.REFERENCE, this.reference.serializeNBT());
			result.setInteger(Tags.UPGRADES, this.upgrades);
			return result;
		}

		@Override
		public void deserializeNBT (NBTTagCompound nbt) {
			this.count = nbt.getInteger(Tags.COUNT);
			this.reference = new ItemStack(nbt.getCompoundTag(Tags.REFERENCE));
			this.upgrades = nbt.getInteger(Tags.UPGRADES);
		}

		public static class Tags {
			public static final String COUNT = "COUNT";
			public static final String REFERENCE = "REFERENCE";
			public static final String UPGRADES = "UPGRADES";

			public Tags () {
			}
		}
	}
}
