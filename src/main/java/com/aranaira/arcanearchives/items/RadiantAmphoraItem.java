package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity;
import com.aranaira.arcanearchives.util.ItemUtils;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
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
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
		return new FluidTankWrapper(new AmphoraUtil(stack));
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
			tooltip.add("");
			String dimensionType;
			try {
				dimensionType = DimensionType.getById(dimID).getName();
			} catch (IllegalArgumentException e) {
				dimensionType = "unknown dimension";
			}
			tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.amphora.linked", bp.getX(), bp.getY(), bp.getZ(), dimensionType, fluidType));
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

	@SuppressWarnings("ConstantConditions")
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
				player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
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
							world.playSound(null, player.getPosition(), SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
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

		public AmphoraUtil (ItemStack incoming) {
			if (incoming.isEmpty()) {
				incoming = new ItemStack(ItemRegistry.RADIANT_AMPHORA);
			}
			stack = incoming;
			ItemUtils.getOrCreateTagCompound(stack);
		}

		@Nullable
		public World getWorld () {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			if (side == Side.CLIENT) {
				try {
					return getClientWorld();
				} catch (NoClassDefFoundError e) {
					ArcaneArchives.logger.error("[Amphora] Was told we were on the client but client-side method results in Minecraft 'class not found' error. How could this happen!", new IllegalArgumentException());
					return null;
				}
			} else {
				return getServerWorld();
			}
		}

		@SideOnly(Side.CLIENT)
		private World getClientWorld () {
			return Minecraft.getMinecraft().world;
		}

		private World getServerWorld () {
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			return server.getEntityWorld();
		}

		public ItemStack getStack () {
			return stack;
		}

		public NBTTagCompound getTag () {
			return stack.getTagCompound();
		}

		public boolean isRemote () {
			RadiantTankTileEntity te = getTile();
			if (te == null) {
				return true;
			}

			return te.getWorld().isRemote;
		}

		public void setHome (BlockPos pos, int dimension) {
			getTag().setLong("homeTank", pos.toLong());
			getTag().setInteger("homeTankDim", dimension);
		}

		public BlockPos getHomePos () {
			if (!getTag().hasKey("homeTank")) {
				return null;
			}

			return BlockPos.fromLong(getTag().getLong("homeTank"));
		}

		public int getHomeDim () {
			return getTag().getInteger("homeTankDim");
		}

		public TankMode getMode () {
			if (!getTag().hasKey("mode")) {
				getTag().setInteger("mode", TankMode.FILL.ordinal());
			}
			return TankMode.fromOrdinal(getTag().getInteger("mode"));
		}

		public void setMode (TankMode mode) {
			if (getTag() != null) {
				getTag().setInteger("mode", mode.ordinal());
			}
		}

		public void toggleMode () {
			if (!getTag().hasKey("mode")) {
				getTag().setInteger("mode", TankMode.FILL.ordinal());
			} else {
				TankMode current = TankMode.fromOrdinal(getTag().getInteger("mode"));
				if (current == TankMode.FILL) {
					getTag().setInteger("mode", TankMode.DRAIN.ordinal());
				} else {
					getTag().setInteger("mode", TankMode.FILL.ordinal());
				}
			}
		}

		public boolean isLinked () {
			return getTag().hasKey("homeTank") && getTag().hasKey("homeTankDim");
		}

		@SideOnly(Side.CLIENT)
		@SuppressWarnings("deprecation")
		public String getFluidType () {
			IFluidHandler handler = getCapability();
			if (handler != null) {
				FluidStack stack = getFluidStack(handler);
				Fluid fluid = getFluid(handler);

				if (stack == null && fluid == null) {
					return "Unknown fluid";
				}

				if (net.minecraft.util.text.translation.I18n.canTranslate(stack.getFluid().getName())) {
					return net.minecraft.util.text.translation.I18n.translateToLocal(stack.getFluid().getName());
				}

				if (stack == null) {
					return "Unknown fluid";
				}
				return net.minecraft.util.text.translation.I18n.translateToLocalFormatted(stack.getLocalizedName());
			}

			return "Unknown fluid";
		}

		@Nullable
		public Fluid getFluid () {
			IFluidHandler cap = getCapability();
			if (cap == null) {
				return null;
			}
			return getFluid(cap);
		}

		@Nullable
		public Fluid getFluid (IFluidHandler capability) {
			if (capability == null) {
				return null;
			}

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

		@Nullable
		public FluidStack getFluidStack (IFluidHandler capability) {
			Fluid fluid = getFluid(capability);
			if (fluid == null) {
				return null;
			}


			return new FluidStack(fluid, 1000);
		}

		@Nullable
		public RadiantTankTileEntity getTile () {
			if (getTag().hasKey("homeTank") && getTag().hasKey("homeTankDim")) {
				World world = getWorld();
				int dim = getTag().getInteger("homeTankDim");
				BlockPos home = BlockPos.fromLong(getTag().getLong("homeTank"));
				if (world != null && world.provider.getDimension() == dim) {
					return WorldUtil.getTileEntity(RadiantTankTileEntity.class, world, home);
				}
			}

			return null;
		}

		@Nullable
		public IFluidHandler getCapability () {
			if (getTag() == null || getTag().isEmpty()) {
				return null;
			}

			RadiantTankTileEntity te = getTile();

			if (te == null) {
				return null;
			}

			IFluidHandler capability = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
			if (capability == null) {
				return null;
			}

			return capability;
		}

		public boolean valid () {
			return getCapability() != null;
		}
	}


	private static class FluidTankWrapper implements IFluidHandlerItem, ICapabilityProvider {
		private static long VALIDITY_DELAY = 5000;
		protected AmphoraUtil util;

		public FluidTankWrapper (AmphoraUtil util) {
			this.util = util;
		}

		@Override
		public IFluidTankProperties[] getTankProperties () {
			IFluidHandler tank = util.getCapability();
			if (tank == null) {
				return new IFluidTankProperties[]{};
			}

			return tank.getTankProperties();

			/*IFluidTankProperties[] props = tank.getTankProperties();
			return new IFluidTankProperties[]{new FluidTankPropWrapper(props[0].getContents(), props[0].getCapacity(), util)};*/
		}

		@Override
		public int fill (FluidStack resource, boolean doFill) {
			IFluidHandler tank = util.getCapability();

			if (tank == null/* || util.getMode() == TankMode.DRAIN*/) {
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
			IFluidHandler tank = util.getCapability();

			if (tank == null/* || util.getMode() == TankMode.FILL*/) {
				return null;
			}

			return tank.drain(resource, doDrain);
		}

		@Override
		@Nullable
		public FluidStack drain (int maxDrain, boolean doDrain) {
			IFluidHandler tank = util.getCapability();

			if (tank == null/* || util.getMode() == TankMode.FILL*/) {
				/*if (!doDrain && maxDrain == Integer.MAX_VALUE) {
					return tank.drain(maxDrain, false);
				}*/
				return null;
			}

			return tank.drain(maxDrain, doDrain);
		}

		@Nonnull
		@Override
		public ItemStack getContainer () {
			return util.getStack();
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