package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity;
import com.aranaira.arcanearchives.util.NBTUtils;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.List;

public class RadiantAmphoraItem extends ItemTemplate {
	public static final String NAME = "item_radiantamphora";

	public RadiantAmphoraItem () {
		super(NAME);
		setMaxStackSize(1);
	}

	@Override
	public void registerModels () {
		ModelResourceLocation unlinked = new ModelResourceLocation(getRegistryName() + "_unlinked", "inventory");
		ModelResourceLocation empty = new ModelResourceLocation(getRegistryName() + "_empty", "inventory");
		ModelResourceLocation fill = new ModelResourceLocation(getRegistryName() + "_fill", "inventory");

		ModelBakery.registerItemVariants(this, unlinked, empty, fill);

		ModelLoader.setCustomMeshDefinition(this, stack -> {
			if (!isLinked(stack)) {
				return unlinked;
			} else if (getEmptyMode(stack)) {
				return empty;
			} else {
				return fill;
			}
		});
	}

	@Override
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.radiantamphora"));
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("homeTank")) {
			BlockPos bp = BlockPos.fromLong(stack.getTagCompound().getLong("homeTank"));
			int dimID = stack.getTagCompound().getInteger("homeTankDim");
			tooltip.add("Linked to <" + bp.getX() + ", " + bp.getY() + ", " + bp.getZ() + "> in \"" + DimensionType.getById(dimID).getName() + "\"");
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick (World world, EntityPlayer playerIn, EnumHand hand) {
		ItemStack stack = playerIn.getHeldItem(hand);
		if (!world.isRemote) {
			NBTTagCompound nbt = stack.getTagCompound();
			if (nbt == null) {
				nbt = new NBTTagCompound();
				stack.setTagCompound(nbt);
			}

			//Only progress if linked to a tank
			if (isLinked(stack)) {
				//Slurp up fluid if in fill mode
				if (!getEmptyMode(stack)) {
					RayTraceResult raytraceresult = this.rayTrace(world, playerIn, true);
					/*ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, world, stack, raytraceresult);
					if (ret != null) return ret;*/
					// ^^^ This is problmematic because it allows mods to modify the event as though it were a normal bucket.

					// Actually nullable vvv
					if (raytraceresult == null) {
						return new ActionResult<>(EnumActionResult.PASS, stack);
					} else if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
						return new ActionResult<>(EnumActionResult.PASS, stack);
					} else {
						BlockPos pos = raytraceresult.getBlockPos();
						BlockPos home = BlockPos.fromLong(nbt.getLong("homeTank"));
						IBlockState state = world.getBlockState(pos);
						Block block = state.getBlock();

						RadiantTankTileEntity rtte = WorldUtil.getTileEntity(RadiantTankTileEntity.class, world, home);
						IFluidHandler cap = rtte == null ? null : rtte.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
						// Null = missing tank or unloaded tank or doesn't have a capability
						if (rtte == null) {
							playerIn.sendStatusMessage(new TextComponentTranslation("arcanearchives.error.tankmissing"), true);
							return new ActionResult<>(EnumActionResult.SUCCESS, stack);
						}

						Fluid fluid = FluidRegistry.lookupFluidForBlock(block);
						if (fluid != null) {
							FluidStack fs = new FluidStack(fluid, 1000);
							if (cap.fill(fs, false) == 1000) {
								cap.fill(fs, true);
								world.setBlockState(pos, Blocks.AIR.getDefaultState());
								playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
							}
							return new ActionResult<>(EnumActionResult.SUCCESS, stack);
						}
					}
				}
			}
		}
		return new ActionResult<>(EnumActionResult.PASS, stack);
	}

	@Override
	public EnumActionResult onItemUse (EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			Block hit = world.getBlockState(pos).getBlock();
			ItemStack stack = getHeldBucket(player);
			NBTTagCompound nbt = NBTUtils.getOrCreateTagCompound(stack);

			boolean emptyMode = nbt.hasKey("isEmptyMode") && nbt.getBoolean("isEmptyMode");

			if (hit == BlockRegistry.RADIANT_TANK) {
				nbt.setLong("homeTank", pos.toLong());
				nbt.setInteger("homeTankDim", player.dimension);
			} else if (nbt.hasKey("homeTank")) {
				BlockPos home = BlockPos.fromLong(nbt.getLong("homeTank"));
				// getTileEntity automatically checks for loaded/unloaded
				RadiantTankTileEntity rtte = WorldUtil.getTileEntity(RadiantTankTileEntity.class, world, home);
				IFluidHandler cap = rtte == null ? null : rtte.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
				if (rtte == null || cap == null) {
					player.sendStatusMessage(new TextComponentTranslation("arcanearchives.error.tankmissing"), true);
					return EnumActionResult.SUCCESS;
				}

				//Remove fluid block from tank and place in world
				if (emptyMode) {
					FluidStack fs = cap.drain(1000, false);
					if (fs != null && fs.amount == 1000) {
						if (FluidUtil.tryPlaceFluid(player, world, pos.offset(facing), cap, fs)) {
							player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
							return EnumActionResult.SUCCESS;
						}
					}
				} else {
					//Check if the target has a fluid inventory
				}
			}
		}
		return EnumActionResult.PASS;
	}

	private boolean isLinked (ItemStack stack) {
		return NBTUtils.getOrCreateTagCompound(stack).hasKey("homeTank");
	}

	private boolean getEmptyMode (ItemStack stack) {
		NBTTagCompound nbt = NBTUtils.getOrCreateTagCompound(stack);
		if (!nbt.hasKey("isEmptyMode")) {
			nbt.setBoolean("isEmptyMode", false);
		}
		return nbt.getBoolean("isEmptyMode");
	}

	private ItemStack getHeldBucket (EntityPlayer player) {
		for (ItemStack stack : player.getHeldEquipment()) {
			if (stack.getItem() == ItemRegistry.RADIANT_AMPHORA) return stack;
		}

		return ItemStack.EMPTY;
	}
}