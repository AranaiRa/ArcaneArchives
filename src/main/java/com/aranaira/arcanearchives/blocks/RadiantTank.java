package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.events.LineHandler;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.items.itemblocks.RadiantTankItem.RadiantTankFluidHandlerItemStack;
import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity;
import com.aranaira.arcanearchives.util.ItemUtilities;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@SuppressWarnings("deprecation")
public class RadiantTank extends BlockTemplate {
	public static final String NAME = "radiant_tank";
	public static AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.1, 0.0, 0.1, 0.9, 0.98, 0.9);

	public RadiantTank () {
		super(NAME, Material.GLASS);
		setSize(1, 1, 1);
		setLightLevel(16 / 16f);
		setHardness(1.7f);
		setHarvestLevel("pickaxe", 0);
	}

	@Override
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.device.radiant_tank"));
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean causesSuffocation (IBlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube (IBlockState state) {
		return false;
	}

	@Override
	public void getDrops (NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		super.getDrops(drops, world, pos, state, fortune);
	}

	public static ItemStack generateStack (IBlockState state, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		ItemStack stack = new ItemStack(BlockRegistry.RADIANT_TANK.getItemBlock());

		RadiantTankTileEntity te = WorldUtil.getTileEntity(RadiantTankTileEntity.class, world, pos);
		if (te == null) {
			return stack;
		}

		if (te.wasCreativeDrop) {
			return ItemStack.EMPTY;
		}

		NBTTagCompound tag = stack.getTagCompound();
		if (tag == null) {
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		}

		te.serializeStack(tag);

		IFluidHandlerItem cap = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		if (cap instanceof RadiantTankFluidHandlerItemStack) {
			RadiantTankFluidHandlerItemStack cap2 = (RadiantTankFluidHandlerItemStack) cap;
			cap2.setCapacity(te.getCapacity());
		}

		return stack;
	}

	@Override
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox (IBlockState state, IBlockAccess source, BlockPos pos) {
		return BOUNDING_BOX;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube (IBlockState state) {
		return false;
	}

	@Override
	public void dropBlockAsItemWithChance (World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
	}

	@Override
	public BlockRenderLayer getRenderLayer () {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		LineHandler.removeLine(pos);

		if(!world.isRemote) {
			ItemStack heldItem = player.getHeldItemMainhand();
			if (heldItem.isEmpty()) {
				heldItem = player.getHeldItemOffhand();
			}

			RadiantTankTileEntity te = WorldUtil.getTileEntity(RadiantTankTileEntity.class, world, pos);
			if (te == null) {
				return !(heldItem.getItem() instanceof ItemBlock);
			}

			if (heldItem.getItem() == ItemRegistry.COMPONENT_CONTAINMENTFIELD) {
				if (player.isSneaking()) {
					te.onRightClickUpgrade(player, heldItem);
				} else {
					player.sendStatusMessage(new TextComponentTranslation("arcanearchives.warning.sneak_to_upgrade_tank"), true);
				}

				return true;
			}

			IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
			return FluidUtil.interactWithFluidHandler(player, hand, handler);
		}

		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	public ItemStack getItem (World worldIn, BlockPos pos, IBlockState state) {
		return generateStack(state, worldIn, pos, null);
	}

	@Override
	public void onBlockHarvested (World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		RadiantTankTileEntity te = WorldUtil.getTileEntity(RadiantTankTileEntity.class, worldIn, pos);
		if (te != null) {
			te.wasCreativeDrop = player.capabilities.isCreativeMode;
		}
		super.onBlockHarvested(worldIn, pos, state, player);
	}

	@Override
	public boolean hasTileEntity (IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity (World world, IBlockState state) {
		return new RadiantTankTileEntity();
	}

	@Override
	public void onBlockExploded (World world, BlockPos pos, Explosion explosion) {
		ItemStack stack = generateStack(null, world, pos, null);
		EntityItem tank = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
		world.spawnEntity(tank);

		super.onBlockExploded(world, pos, explosion);
	}

	@Override
	public ItemStack getPickBlock (IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return generateStack(state, world, pos, player);
	}

	@Override
	@ParametersAreNonnullByDefault
	public void breakBlock (World world, BlockPos pos, IBlockState state) {
		LineHandler.removeLine(pos);

		ItemStack stack = generateStack(state, world, pos, null);
		if (!stack.isEmpty()) {
			spawnAsEntity(world, pos, stack);
		}

		world.updateComparatorOutputLevel(pos, this);
		super.breakBlock(world, pos, state);
	}

	@Override
	public void onBlockPlacedBy (@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityLivingBase placer, @Nonnull ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, placer, stack);

		if (stack.hasTagCompound()) {
			RadiantTankTileEntity te = WorldUtil.getTileEntity(RadiantTankTileEntity.class, world, pos);
			if (te != null) {
				te.deserializeStack(stack.getTagCompound());
			}
		}
	}

	@Override
	public boolean hasComparatorInputOverride (IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride (IBlockState blockState, World worldIn, BlockPos pos) {
		return ItemUtilities.calculateRedstoneFromTileEntity(worldIn.getTileEntity(pos));
	}
}
