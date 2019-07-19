package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.events.LineHandler;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity;
import com.aranaira.arcanearchives.util.ItemUtilities;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

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
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.device.radiant_tank"));
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube (IBlockState state) {
		return false;
	}

	@Override
	public Item getItemDropped (IBlockState state, Random rand, int fortune) {
		return Items.AIR;
	}

	@Override
	public void getDrops (NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
	}

	@Override
	public void harvestBlock (World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
		player.addStat(StatList.getBlockStats(this));
		player.addExhaustion(0.005F);

		if (worldIn.isRemote) {
			return;
		}

		harvesters.set(player);
		List<ItemStack> items = new java.util.ArrayList<>();
		items.add(generateStack(te, worldIn, pos));
		net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0f, true, player);
		for (ItemStack item : items) {
			spawnAsEntity(worldIn, pos, item);
		}
		harvesters.set(null);
	}

	private static ItemStack generateStack (TileEntity incomingTe, IBlockAccess world, BlockPos pos) {
		if (!(incomingTe instanceof RadiantTankTileEntity)) {
			return new ItemStack(BlockRegistry.RADIANT_TANK.getItemBlock());
		}

		RadiantTankTileEntity te = (RadiantTankTileEntity) incomingTe;

		if (te.wasCreativeDrop) {
			return ItemStack.EMPTY;
		}

		NBTTagCompound tag = new NBTTagCompound();
		te.serializeStack(tag);

		ItemStack stack = new ItemStack(BlockRegistry.RADIANT_TANK.getItemBlock());
		stack.setTagCompound(tag);

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
	public BlockRenderLayer getRenderLayer () {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		LineHandler.removeLine(pos, player.dimension);

		if (!world.isRemote) {
			ItemStack heldItem = player.getHeldItemMainhand();
			if (heldItem.isEmpty()) {
				heldItem = player.getHeldItemOffhand();
			}

			RadiantTankTileEntity te = WorldUtil.getTileEntity(RadiantTankTileEntity.class, world, pos);
			if (te == null) {
				return !(heldItem.getItem() instanceof ItemBlock);
			}

			IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
			return FluidUtil.interactWithFluidHandler(player, hand, handler);
		}

		return true;
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
	@ParametersAreNonnullByDefault
	public void breakBlock (World world, BlockPos pos, IBlockState state) {
		LineHandler.removeLine(pos, world.provider.getDimension());

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
