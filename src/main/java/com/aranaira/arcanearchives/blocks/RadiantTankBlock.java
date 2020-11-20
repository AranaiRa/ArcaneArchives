/*package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.TemplateBlock;
import com.aranaira.arcanearchives.client.tracking.LineHandler;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity;
import com.aranaira.arcanearchives.util.ItemUtils;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
public class RadiantTankBlock extends TemplateBlock {
	public static final String NAME = "radiant_tank";
	public static AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.1, 0.0, 0.1, 0.9, 0.98, 0.9);

	public RadiantTankBlock () {
		super(NAME, Material.GLASS);
		setSize(1, 1, 1);
		setLightLevel(16 / 16f);
		setHardness(3f);
		setHarvestLevel("pickaxe", 0);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.device.radiant_tank"));
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube (BlockState state) {
		return false;
	}

	@Override
	public Item getItemDropped (BlockState state, Random rand, int fortune) {
		return Items.AIR;
	}

	@Override
	public void getDrops (NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, BlockState state, int fortune) {
	}

	@Override
	public void harvestBlock (World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
		player.addStat(Stats.getBlockStats(this));
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

		CompoundNBT tag = new CompoundNBT();
		te.serializeStack(tag);

		ItemStack stack = new ItemStack(BlockRegistry.RADIANT_TANK.getItemBlock());
		stack.setTag(tag);

		return stack;
	}

	@Override
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox (BlockState state, IBlockAccess source, BlockPos pos) {
		return BOUNDING_BOX;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube (BlockState state) {
		return false;
	}

	@Override
	public BlockRenderLayer getRenderLayer () {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean onBlockActivated (World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
		LineHandler.removeLine(pos, player.dimension);

		if (!world.isRemote) {
			ItemStack heldItem = player.getHeldItemMainhand();
			if (heldItem.isEmpty()) {
				heldItem = player.getHeldItemOffhand();
			}

			RadiantTankTileEntity te = WorldUtil.getTileEntity(RadiantTankTileEntity.class, world, pos);
			if (te == null) {
				return !(heldItem.getItem() instanceof BlockItem);
			}

			IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
			return FluidUtil.interactWithFluidHandler(player, hand, handler);
		}

		return true;
	}

	@Override
	public void onBlockHarvested (World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		RadiantTankTileEntity te = WorldUtil.getTileEntity(RadiantTankTileEntity.class, worldIn, pos);
		if (te != null) {
			te.wasCreativeDrop = player.capabilities.isCreativeMode;
		}
		super.onBlockHarvested(worldIn, pos, state, player);
	}

	@Override
	public boolean hasTileEntity (BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity (World world, BlockState state) {
		return new RadiantTankTileEntity();
	}

	@Override

	public void breakBlock (World world, BlockPos pos, BlockState state) {
		LineHandler.removeLine(pos, world.provider.getDimension());

		world.updateComparatorOutputLevel(pos, this);
		super.breakBlock(world, pos, state);
	}

	@Override
	public void onBlockPlacedBy (@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull LivingEntity placer, @Nonnull ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, placer, stack);

		if (stack.hasTagCompound()) {
			RadiantTankTileEntity te = WorldUtil.getTileEntity(RadiantTankTileEntity.class, world, pos);
			if (te != null) {
				te.deserializeStack(stack.getTag());
				te.markDirty();
				te.defaultServerSideUpdate();
			}
		}
	}

	@Override
	public boolean hasComparatorInputOverride (BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride (BlockState blockState, World worldIn, BlockPos pos) {
		return ItemUtils.calculateRedstoneFromTileEntity(worldIn.getTileEntity(pos));
	}
}*/
