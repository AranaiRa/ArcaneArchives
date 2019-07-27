package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity;
import com.aranaira.arcanearchives.util.ItemUtils;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.List;

public class RadiantAmphoraItem extends ItemTemplate {
	public static final String NAME = "radiant_amphora";

	public RadiantAmphoraItem () {
		super(NAME);
		setMaxStackSize(1);

		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DispenseAmphora.getInstance());
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities (ItemStack stack, @Nullable NBTTagCompound nbt) {
		if (!stack.isEmpty()) {
			return new AmphoraCapabilityProvider(new AmphoraUtil(stack));
		}
		return super.initCapabilities(stack, nbt);
	}

	@Override
	public void registerModels () {
		ModelResourceLocation unlinked = new ModelResourceLocation(getRegistryName() + "_unlinked", "inventory");
		ModelResourceLocation empty = new ModelResourceLocation(getRegistryName() + "_empty", "inventory");
		ModelResourceLocation fill = new ModelResourceLocation(getRegistryName() + "_fill", "inventory");

		ModelBakery.registerItemVariants(this, unlinked, empty, fill);

		ModelLoader.setCustomMeshDefinition(this, stack -> {
			AmphoraUtil util = new AmphoraUtil(stack);
			if (!util.isLinked()) {
				return unlinked;
			} else if (util.getMode() == TankMode.DRAIN) {
				return empty;
			} else {
				return fill;
			}
		});
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.radiant_amphora"));
		AmphoraUtil util = new AmphoraUtil(stack);
		if (util.isLinked()) {
			BlockPos bp = util.getHomePos();
			int dimID = util.getHomeDim();
			String fluidType = util.getFluidType();
			tooltip.add("Linked to " + bp.getX() + "/" + bp.getY() + "/" + bp.getZ() + " containing " + fluidType + " in \"" + DimensionType.getById(dimID).getName() + "\"");
		}
	}

	@Override
	public EnumActionResult onItemUseFirst (EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		IBlockState state = world.getBlockState(pos);
		ItemStack stack = player.getHeldItem(hand);
		if (state.getBlock() == BlockRegistry.RADIANT_TANK && stack.getItem() == this && player.isSneaking()) {
			AmphoraUtil util = new AmphoraUtil(stack);
			util.setHome(pos, world.provider.getDimension());
			return EnumActionResult.SUCCESS;
		}

		return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
	}

	@Override
	@Nonnull
	public ActionResult<ItemStack> onItemRightClick (@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		AmphoraUtil util = new AmphoraUtil(itemstack);

		if (util.getMode() == TankMode.FILL) {
			RayTraceResult mop = this.rayTrace(world, player, true);

			ActionResult<ItemStack> result = ForgeEventFactory.onBucketUse(player, world, itemstack, mop);
			if (result == null) {
				return ActionResult.newResult(EnumActionResult.FAIL, itemstack);
			} else {
				return result;
			}
		} else {
			RayTraceResult mop = this.rayTrace(world, player, false);

			if (mop == null || mop.typeOfHit != RayTraceResult.Type.BLOCK) {
				return ActionResult.newResult(EnumActionResult.PASS, itemstack);
			}

			IFluidHandler cap = util.getCapability();
			if (cap == null) {
				return ActionResult.newResult(EnumActionResult.FAIL, itemstack);
			}
			boolean canDrain = false;
			for (IFluidTankProperties prop : cap.getTankProperties()) {
				if (prop.getCapacity() < 1000) {
					continue;
				}
				canDrain = true;
				break;
			}
			if (!canDrain) {
				return ActionResult.newResult(EnumActionResult.FAIL, itemstack);
			}

			FluidStack fluidStack = util.getFluidStack(cap); // This is a fake fluid stack

			BlockPos clickPos = mop.getBlockPos();
			// can we place liquid there?
			if (world.isBlockModifiable(player, clickPos)) {
				// the block adjacent to the side we clicked on
				BlockPos targetPos = clickPos.offset(mop.sideHit);

				// can the player place there?
				if (player.canPlayerEdit(targetPos, mop.sideHit, itemstack)) {
					// try placing liquid
					if (!world.isRemote) {
						FluidActionResult result = FluidUtil.tryPlaceFluid(player, world, targetPos, itemstack, fluidStack);
						if (result.isSuccess()) {
							// success!
							player.addStat(StatList.getObjectUseStats(this));

							return ActionResult.newResult(EnumActionResult.SUCCESS, itemstack);
						}
					} else {
						return ActionResult.newResult(EnumActionResult.SUCCESS, itemstack);
					}
				}
			}

			// couldn't place liquid there2
			return ActionResult.newResult(EnumActionResult.FAIL, itemstack);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW) // low priority so other mods can handle their stuff first
	public void onFillBucket (FillBucketEvent event) {
		if (event.getResult() != Event.Result.DEFAULT) {
			return;
		}

		if (event.getEmptyBucket().getItem() != this) {
			return;
		}

		if (event.getWorld().isRemote) {
			return;
		}

		ItemStack amphora = event.getEmptyBucket();
		AmphoraUtil util = new AmphoraUtil(amphora);

		if (util.getMode() != TankMode.FILL) {
			return;
		}

		RayTraceResult target = event.getTarget();
		if (target == null || target.typeOfHit != RayTraceResult.Type.BLOCK) {
			return;
		}

		World world = event.getWorld();
		BlockPos pos = target.getBlockPos();

		FluidActionResult filledResult = FluidUtil.tryPickUpFluid(amphora, event.getEntityPlayer(), world, pos, target.sideHit);
		if (filledResult.isSuccess()) {
			event.setResult(Event.Result.ALLOW);
			event.setFilledBucket(filledResult.getResult());
		} else {
			event.setCanceled(true);
		}
	}

	public static class AmphoraUtil {
		private ItemStack stack;
		private NBTTagCompound nbt;
		private WeakReference<RadiantTankTileEntity> te;

		public AmphoraUtil (ItemStack incoming) {
			stack = incoming;
			nbt = ItemUtils.getOrCreateTagCompound(stack);
		}

		public ItemStack getStack () {
			return stack;
		}

		public boolean isRemote () {
			if (te == null || te.get() == null) {
				return true;
			}

			return te.get().getWorld().isRemote;
		}

		public void setHome (BlockPos pos, int dimension) {
			nbt.setLong("homeTank", pos.toLong());
			nbt.setInteger("homeTankDim", dimension);
		}

		public BlockPos getHomePos () {
			if (!nbt.hasKey("homeTank")) {
				return null;
			}

			return BlockPos.fromLong(nbt.getLong("homeTank"));
		}

		public int getHomeDim () {
			return nbt.getInteger("homeTankDim");
		}

		public TankMode getMode () {
			if (!nbt.hasKey("mode")) {
				nbt.setInteger("mode", TankMode.FILL.ordinal());
			}
			return TankMode.fromOrdinal(nbt.getInteger("mode"));
		}

		public void setMode (TankMode mode) {
			if (nbt != null) {
				nbt.setInteger("mode", mode.ordinal());
			}
		}

		public void toggleMode () {
			if (!nbt.hasKey("mode")) {
				nbt.setInteger("mode", TankMode.FILL.ordinal());
			} else {
				TankMode current = TankMode.fromOrdinal(nbt.getInteger("mode"));
				if (current == TankMode.FILL) {
					nbt.setInteger("mode", TankMode.DRAIN.ordinal());
				} else {
					nbt.setInteger("mode", TankMode.FILL.ordinal());
				}
			}
		}

		public boolean isLinked () {
			return nbt.hasKey("homeTank") && nbt.hasKey("homeTankDim");
		}

		@SideOnly(Side.CLIENT)
		@SuppressWarnings("deprecation")
		public String getFluidType () {
			IFluidHandler handler = getCapability();
			if (handler != null) {
				FluidStack stack = getFluidStack(handler);

				if (stack == null) {
					return "Unknown fluid";
				}

				if (net.minecraft.util.text.translation.I18n.canTranslate(stack.getFluid().getName())) {
					return net.minecraft.util.text.translation.I18n.translateToLocal(stack.getFluid().getName());
				}

				return net.minecraft.util.text.translation.I18n.translateToLocalFormatted(stack.getLocalizedName());
			}

			return "Unknown fluid";
		}

		public Fluid getFluid () {
			IFluidHandler cap = getCapability();
			if (cap == null) {
				return null;
			}
			return getFluid(cap);
		}

		public Fluid getFluid (IFluidHandler capability) {
			IFluidTankProperties[] props = capability.getTankProperties();
			if (props.length == 0) {
				return null;
			}

			FluidStack contents = props[0].getContents();
			if (contents == null) {
				return null;
			}

			return contents.getFluid();
		}

		public FluidStack getFluidStack (IFluidHandler capability) {
			Fluid fluid = getFluid(capability);
			if (fluid == null) {
				return null;
			}


			return new FluidStack(fluid, 1000);
		}

		private void validate () {
			nbt = stack.getTagCompound();
		}

		@Nullable
		public IFluidHandler getCapability () {
			validate();

			if (nbt == null || nbt.isEmpty()) {
				return null;
			}

			if (te == null || te.get() == null) {
				if (nbt.hasKey("homeTank") && nbt.hasKey("homeTankDim")) {
					BlockPos home = BlockPos.fromLong(nbt.getLong("homeTank"));
					int dim = nbt.getInteger("homeTankDim");
					te = new WeakReference<>(WorldUtil.getTileEntity(RadiantTankTileEntity.class, dim, home));
				} else {
					return null;
				}
			}

			if (te == null || te.get() == null) {
				return null;
			}

			IFluidHandler capability = te.get().getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
			if (capability == null) {
				return null;
			}

			return capability;
		}

		public boolean valid () {
			return getCapability() != null;
		}
	}


	private static class FluidTankWrapper implements IFluidHandlerItem {
		private static long VALIDITY_DELAY = 5000;
		protected IFluidHandler tank;
		private long lastUpdated;
		protected AmphoraUtil util;

		public FluidTankWrapper (AmphoraUtil util) {
			this.util = util;
			update();
		}

		public void update () {
			this.tank = util.getCapability();
			this.lastUpdated = System.currentTimeMillis();
		}

		public boolean valid () {
			if (tank == null) {
				return false;
			}

			return System.currentTimeMillis() - this.lastUpdated < VALIDITY_DELAY;
		}

		public void validate () {
			if (!valid()) {
				update();
			}
		}

		@Override
		public IFluidTankProperties[] getTankProperties () {
			validate();
			if (tank == null) {
				return new IFluidTankProperties[]{};
			}

			return tank.getTankProperties();

			/*IFluidTankProperties[] props = tank.getTankProperties();
			return new IFluidTankProperties[]{new FluidTankPropWrapper(props[0].getContents(), props[0].getCapacity(), util)};*/
		}

		@Override
		public int fill (FluidStack resource, boolean doFill) {
			validate();

			if (tank == null || util.getMode() == TankMode.DRAIN) {
				return 0;
			}

			//resource.amount = Math.min(resource.amount, 1000);
			//if (!util.isRemote()) {
			return tank.fill(resource, doFill);
			//} else {
			//	return tank.fill(resource, false);
			//}
		}

		@Override
		@Nullable
		public FluidStack drain (FluidStack resource, boolean doDrain) {
			validate();

			if (tank == null || util.getMode() == TankMode.FILL) {
				return null;
			}

			//resource.amount = Math.min(resource.amount, 1000);
			//if (!util.isRemote()) {
			return tank.drain(resource, doDrain);
			//} else {
			//	return tank.drain(resource, false);
			//}
		}

		@Override
		@Nullable
		public FluidStack drain (int maxDrain, boolean doDrain) {
			validate();

			if (tank == null || util.getMode() == TankMode.FILL) {
				return null;
			}

			//if (!util.isRemote()) {
			return tank.drain(maxDrain, doDrain);
			//} else {
			//	return tank.drain(maxDrain, false);
			//}
		}

		@Nonnull
		@Override
		public ItemStack getContainer () {
			return util.getStack();
		}
	}

	public static class AmphoraCapabilityProvider implements ICapabilityProvider {
		private AmphoraUtil util;

		public AmphoraCapabilityProvider (AmphoraUtil util) {
			this.util = util;
		}

		@Override
		public boolean hasCapability (@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
			return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;

		}

		@Nullable
		@Override
		@SuppressWarnings("unchecked")
		public <T> T getCapability (@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY) {
				return (T) new FluidTankWrapper(util);
			}

			return null;
		}
	}

	public enum TankMode {
		DRAIN, FILL;

		public static TankMode fromOrdinal (int ordinal) {
			if (ordinal == 0) {
				return DRAIN;
			}
			return FILL;
		}
	}

}