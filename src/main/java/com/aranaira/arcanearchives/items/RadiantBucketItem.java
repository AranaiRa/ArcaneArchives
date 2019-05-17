package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class RadiantBucketItem extends ItemTemplate {
	public static final String NAME = "item_radiantbucket";

	public RadiantBucketItem() {
		super(NAME);
	}

	@Override
	public void registerModels() {
		ModelResourceLocation unlinked = new ModelResourceLocation(getRegistryName()+"_unlinked", "inventory");
		ModelResourceLocation empty    = new ModelResourceLocation(getRegistryName()+"_empty",    "inventory");
		ModelResourceLocation fill     = new ModelResourceLocation(getRegistryName()+"_fill",     "inventory");

		ModelBakery.registerItemVariants(this, unlinked, empty, fill);

		ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				if (!isLinked(stack)) {
					return unlinked;
				} else if(isEmptyMode(stack)) {
					return empty;
				} else {
					return fill;
				}
			}
		});
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.radiantbucket"));
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("homeTank"))
		{
			tooltip.add(stack.getTagCompound().getLong("homeTank")+"");
		}
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("isEmptyMode"))
		{
			tooltip.add(" :: "+stack.getTagCompound().getBoolean("isEmptyMode"));
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer playerIn, EnumHand hand) {
		ItemStack stack = playerIn.getHeldItem(hand);
		NBTTagCompound nbt = stack.getTagCompound();

		//Only progress if linked to a tank
		if (isLinked(stack)) {
			//Swap between fill and empty mode
			if (playerIn.isSneaking()) {
				if (stack.hasTagCompound()) {
					nbt = stack.getTagCompound();
				} else {
					nbt = new NBTTagCompound();
				}

				if (nbt.hasKey("isEmptyMode")) {
					if (nbt.getBoolean("isEmptyMode")) {
						nbt.setBoolean("isEmptyMode", false);
					} else {
						nbt.setBoolean("isEmptyMode", true);
					}
				} else {
					nbt.setBoolean("isEmptyMode", false);
				}
				stack.setTagCompound(nbt);
			}
			//Slurp up fluid if in fill mode
			else if (!isEmptyMode(stack)){
				RayTraceResult raytraceresult = this.rayTrace(world, playerIn, true);
				ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, world, stack, raytraceresult);
				if (ret != null) return ret;

				if (raytraceresult == null) {
					return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
				} else if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
					return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
				} else {
					BlockPos pos = raytraceresult.getBlockPos();

					RadiantTankTileEntity rtte = (RadiantTankTileEntity) world.getTileEntity(BlockPos.fromLong(nbt.getLong("homeTank")));
					IFluidHandler cap = rtte.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
					if(FluidRegistry.lookupFluidForBlock(world.getBlockState(pos).getBlock()) != null) {
						FluidStack fs = new FluidStack(FluidRegistry.lookupFluidForBlock(world.getBlockState(pos).getBlock()), 1000);
						cap.fill(fs, true);
						world.setBlockState(pos, Blocks.AIR.getDefaultState());
						playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
						return new ActionResult<>(EnumActionResult.SUCCESS, stack);
					}
				}
			}
		}
		return new ActionResult<>(EnumActionResult.PASS, stack);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		Block hit = world.getBlockState(pos).getBlock();
		ItemStack stack = getHeldBucket(player);
		NBTTagCompound nbt = getTagCompoundSafe(stack);

		ArcaneArchives.logger.info("hit "+hit.getLocalizedName());
		if(hit == BlockRegistry.RADIANT_TANK) {

			if(stack.hasTagCompound()) {
				nbt = stack.getTagCompound();
			} else {
				nbt = new NBTTagCompound();
			}

			nbt.setLong("homeTank", pos.toLong());
			stack.setTagCompound(nbt);
		}
		else if(nbt.hasKey("homeTank")) {
			//TODO: check if area is loaded
			if(world.getBlockState(BlockPos.fromLong(nbt.getLong("homeTank"))).getBlock() == BlockRegistry.RADIANT_TANK) {
				RadiantTankTileEntity rtte = (RadiantTankTileEntity)world.getTileEntity(BlockPos.fromLong(nbt.getLong("homeTank")));
				IFluidHandler cap = rtte.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);

				//Remove fluid block from tank and place in world
				if(nbt.getBoolean("isEmptyMode")) {
					FluidStack fs = cap.drain(1000, false);
					if (fs != null) {

						if (fs.amount == 1000) {
							cap.drain(1000, true);

							if (fs.getFluid().canBePlacedInWorld()) {
								world.setBlockState(pos.offset(facing), fs.getFluid().getBlock().getDefaultState(), 11);
								player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
								return EnumActionResult.SUCCESS;
							}
						}
					}
				}
				//Remove fluid block from world and place in tank
				else {
					//Check if the target has a fluid inventory
					if(false) {
						//TODO: inserting fluids into inventories
					}
				}
			}
		}
		return EnumActionResult.PASS;
	}

	private NBTTagCompound getTagCompoundSafe(ItemStack stack) {
		NBTTagCompound tagCompound = stack.getTagCompound();
		if (tagCompound == null) {
			tagCompound = new NBTTagCompound();
			stack.setTagCompound(tagCompound);
		}
		return tagCompound;
	}

	private boolean isLinked(ItemStack stack) {
		return getTagCompoundSafe(stack).hasKey("homeTank");
	}

	private boolean isEmptyMode(ItemStack stack) {
		if(getTagCompoundSafe(stack).hasKey("isEmptyMode")){
			return stack.getTagCompound().getBoolean("isEmptyMode");
		}
		stack.getTagCompound().setBoolean("isEmptyMode", false);
		return false;
	}

	private ItemStack getHeldBucket(EntityPlayer player){
		ItemStack stack = player.getHeldItemMainhand();
		if(stack.getItem() == ItemRegistry.RADIANT_BUCKET) {
			return stack;
		} else {
			stack = player.getHeldItemOffhand();
		}

		return stack;
	}
}