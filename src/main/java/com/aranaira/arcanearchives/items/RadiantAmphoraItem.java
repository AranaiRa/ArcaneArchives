package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity;
import com.aranaira.arcanearchives.util.NBTUtils;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class RadiantAmphoraItem extends ItemTemplate {
	public static final String NAME = "item_radiantamphora";
	// Check delay in milliseconds for fluid types
	public static long CHECK_DELAY = 15000L;

	public RadiantAmphoraItem () {
		super(NAME);
		setMaxStackSize(1);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities (ItemStack stack, @Nullable NBTTagCompound nbt) {
		if (!stack.isEmpty()) {
			return new AmphoraCapabilityProvider(stack, new AmphoraUtil(stack));
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
			} else if (util.getIsEmptyMode()) {
				return empty;
			} else {
				return fill;
			}
		});
	}

	@Override
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.radiantamphora"));
		AmphoraUtil util = new AmphoraUtil(stack);
		if (util.isLinked()) {
			BlockPos bp = util.getHomePos();
			int dimID = util.getHomeDim();
			String fluidType = util.getFluidType();
			tooltip.add("Linked to " + bp.getX() + "/" + bp.getY() + "/" + bp.getZ() + " containing " + fluidType + " in \"" + DimensionType.getById(dimID).getName() + "\"");
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick (World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote) {
			boolean result = onItemRightClickInternal(world, player, hand, stack);
			if (result) {
				return new ActionResult<>(EnumActionResult.SUCCESS, stack);
			}
		}
		return new ActionResult<>(EnumActionResult.PASS, stack);
	}

	public boolean onItemRightClickInternal (World world, EntityPlayer player, EnumHand hand, ItemStack stack) {
		//Only progress if linked to a tank
		AmphoraUtil util = new AmphoraUtil(stack);

		if (util.isLinked() && !util.getIsEmptyMode()) {
			RayTraceResult raytraceresult = this.rayTrace(world, player, true);

			// Actually nullable vvv
			if (raytraceresult == null || raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
				return false;
			} else {
				IFluidHandler cap = util.getCapability();
				if (cap == null) {
					player.sendStatusMessage(new TextComponentTranslation("arcanearchives.error.tankmissing"), true);
					return true;
				}

				BlockPos pos = raytraceresult.getBlockPos();
				Block block = world.getBlockState(pos).getBlock();
				Fluid fluid = FluidRegistry.lookupFluidForBlock(block);

				if (fluid == null) {
					return false;
				}
				FluidStack fs = new FluidStack(fluid, 1000);
				if (cap.fill(fs, false) == 1000) {
					cap.fill(fs, true);
					world.setBlockToAir(pos);
					// TODO: Create a new sound event with a different subtitle (cf. recent Quark changes)
					player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
				}
				return true;
			}
		}

		return false;
	}

	@Override
	public EnumActionResult onItemUse (EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			Block hit = world.getBlockState(pos).getBlock();
			ItemStack stack = getHeldBucket(player);
			AmphoraUtil util = new AmphoraUtil(stack);

			if (hit == BlockRegistry.RADIANT_TANK && player.isSneaking()) {
				util.setHome(pos, player.dimension);
			} else if (util.isLinked() && util.getIsEmptyMode()) {
				IFluidHandler cap = util.getCapability();
				if (cap == null) {
					player.sendStatusMessage(new TextComponentTranslation("arcanearchives.error.tankmissing"), true);
					return EnumActionResult.SUCCESS;
				}

				FluidStack fs = util.getFluidStack(cap);
				if (FluidUtil.tryPlaceFluid(player, world, pos.offset(facing), cap, fs)) {
					return EnumActionResult.SUCCESS;
				}
			}
		}
		return EnumActionResult.PASS;
	}

	private ItemStack getHeldBucket (EntityPlayer player) {
		for (ItemStack stack : player.getHeldEquipment()) {
			if (stack.getItem() == ItemRegistry.RADIANT_AMPHORA) {
				return stack;
			}
		}

		return ItemStack.EMPTY;
	}

	public static class AmphoraUtil {
		private ItemStack stack;
		private NBTTagCompound nbt;
		private RadiantTankTileEntity te;

		public AmphoraUtil (ItemStack incoming) {
			stack = incoming;
			nbt = NBTUtils.getOrCreateTagCompound(stack);
		}

		public ItemStack getStack () {
			return stack;
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

		public boolean getIsEmptyMode () {
			if (!nbt.hasKey("isEmptyMode")) {
				nbt.setBoolean("isEmptyMode", false);
			}
			return nbt.getBoolean("isEmptyMode");
		}

		public boolean isLinked () {
			return nbt.hasKey("homeTank") && nbt.hasKey("homeTankDim");
		}

		@SideOnly(Side.CLIENT)
		public String getFluidType () {
			if (!nbt.hasKey("fluidType")) {
				return I18n.format("arcanearchives.tooltip.amphora.unknown_fluid");
			}

			String key = nbt.getString("fluidType");
			if (!I18n.format(key + ".name").equals(key + ".name")) {
				key = key.replace("fluid.", "") + ".name";
			}

			return I18n.format(key);
		}

		public Fluid getFluid (IFluidHandler capability) {
			return capability.getTankProperties()[0].getContents().getFluid();
		}

		public FluidStack getFluidStack (IFluidHandler capability) {
			return new FluidStack(getFluid(capability), 1000);
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

			if (te == null) {
				if (nbt.hasKey("homeTank") && nbt.hasKey("homeTankDim")) {
					BlockPos home = BlockPos.fromLong(nbt.getLong("homeTank"));
					int dim = nbt.getInteger("homeTankDim");
					te = WorldUtil.getTileEntity(RadiantTankTileEntity.class, dim, home);
				} else {
					return null;
				}
			}

			if (te == null) {
				return null;
			}

			IFluidHandler capability = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
			if (capability == null) {
				return null;
			}

			boolean doCheck = true;

			if (nbt.hasKey("fluidType") && nbt.hasKey("fluidTimer")) {
				long lastCheck = nbt.getLong("fluidTimer");
				if ((System.currentTimeMillis() - lastCheck) < CHECK_DELAY) {
					doCheck = false;
				}
			}

			if (!doCheck) {
				return capability;
			}

			IFluidTankProperties props = capability.getTankProperties()[0];
			FluidStack content = props.getContents();
			if (content == null) {
				return capability;
			}

			nbt.setString("fluidType", content.getUnlocalizedName());
			nbt.setLong("fluidTimer", System.currentTimeMillis());
			// This is unneccessaarryyy?
			stack.setTagCompound(nbt);

			return capability;
		}

		public boolean valid () {
			return getCapability() != null;
		}
	}

	private static class FluidTankWrapperFill implements IFluidHandlerItem {
		private static long VALIDITY_DELAY = 5000;
		protected IFluidHandler tank;
		private long lastUpdated;
		protected AmphoraUtil util;

		public FluidTankWrapperFill (ItemStack stack, AmphoraUtil util) {
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
				return new IFluidTankProperties[] {};
			}

			// TODO: This may need tweaking
			return tank.getTankProperties();
		}

		@Override
		public int fill (FluidStack resource, boolean doFill) {
			validate();
			if (tank == null) return 0;

			return tank.fill(resource, doFill);
		}

		@Override
		@Nullable
		public FluidStack drain (FluidStack resource, boolean doDrain) {
			return null;
		}

		@Override
		@Nullable
		public FluidStack drain (int maxDrain, boolean doDrain) {
			return null;
		}

		@Nonnull
		@Override
		public ItemStack getContainer () {
			return util.getStack();
		}
	}

	public static class FluidTankWrapperDrain extends FluidTankWrapperFill {
		public FluidTankWrapperDrain (ItemStack stack, AmphoraUtil util) {
			super(stack, util);
		}

		@Override
		public IFluidTankProperties[] getTankProperties () {
			return super.getTankProperties();
		}

		@Override
		public int fill (FluidStack resource, boolean doFill) {
			return 0;
		}

		@Nullable
		@Override
		public FluidStack drain (FluidStack resource, boolean doDrain) {
			validate();
			if (tank == null) return null;

			return tank.drain(resource, doDrain);
		}

		@Nullable
		@Override
		public FluidStack drain (int maxDrain, boolean doDrain) {
			validate();
			if (tank == null) return null;

			return tank.drain(maxDrain, doDrain);
		}
	}

	public static class AmphoraCapabilityProvider implements ICapabilityProvider {
		private ItemStack container;
		private AmphoraUtil util;

		public AmphoraCapabilityProvider (ItemStack container, AmphoraUtil util) {
			this.container = container;
			this.util = util;
		}

		@Override
		public boolean hasCapability (@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
			return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;

		}

		@Nullable
		@Override
		public <T> T getCapability (@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY) {
				if (util.getIsEmptyMode()) {
					return (T) new FluidTankWrapperDrain(container, util);
				} else {
					return (T) new FluidTankWrapperFill(container, util);
				}
			}

			return null;
		}
	}
}