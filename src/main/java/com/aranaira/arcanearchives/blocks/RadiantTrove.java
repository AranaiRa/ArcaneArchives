package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.BlockDirectionalTemplate;
import com.aranaira.arcanearchives.events.LineHandler;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.inventory.handlers.OptionalUpgradesHandler;
import com.aranaira.arcanearchives.inventory.handlers.TroveItemHandler;
import com.aranaira.arcanearchives.tileentities.IUpgradeableStorage;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity;
import com.aranaira.arcanearchives.util.ItemUtilities;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@SuppressWarnings("deprecation")
public class RadiantTrove extends BlockDirectionalTemplate {
	public static final String NAME = "radiant_trove";

	public RadiantTrove () {
		super(NAME, Material.WOOD);
		setSize(1, 1, 1);
		setLightLevel(16 / 16f);
		setHardness(1.7f);
		setResistance(6000F);
		setHarvestLevel("axe", 0);
	}

	@Override
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.device.radiant_trove"));
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
		LineHandler.removeLine(pos);

		if (!world.isRemote && hand == EnumHand.MAIN_HAND) {
			RadiantTroveTileEntity te = WorldUtil.getTileEntity(RadiantTroveTileEntity.class, world, pos);
			if (te == null) {
				return false;
			}

			te.onRightClickTrove(player);
			return true;
		}

		return true;
	}

	@Override
	public void onBlockClicked (World world, BlockPos pos, EntityPlayer player) {
		IBlockState state = world.getBlockState(pos);
		LineHandler.removeLine(pos);

		if (state.getBlock() == BlockRegistry.RADIANT_TROVE) {
			if (!world.isRemote) {
				RadiantTroveTileEntity te = WorldUtil.getTileEntity(RadiantTroveTileEntity.class, world, pos);
				if (te == null) {
					return;
				}

				te.onLeftClickTrove(player);
			}
		}
	}

	@Override
	public boolean hasTileEntity (IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity (World world, IBlockState state) {
		return new RadiantTroveTileEntity();
	}

	@Override
	@ParametersAreNonnullByDefault
	public void breakBlock (World world, BlockPos pos, IBlockState state) {
		LineHandler.removeLine(pos);

		if (!world.isRemote) {
			RadiantTroveTileEntity te = WorldUtil.getTileEntity(RadiantTroveTileEntity.class, world, pos);
			if (te != null) {
				TroveItemHandler handler = te.getInventory();
				while (!handler.isEmpty()) {
					ItemStack stack = handler.extractItem(0, 64, false);
					spawnAsEntity(world, pos, stack);
				}
				IItemHandler optionals = te.getOptionalUpgradesHandler();
				IItemHandler size = te.getSizeUpgradesHandler();
				for (int i = 0; i < optionals.getSlots(); i++) {
					ItemStack stack = optionals.getStackInSlot(i);
					if (!stack.isEmpty()) {
						spawnAsEntity(world, pos, stack);
					}
				}
				for (int i = 0; i < size.getSlots(); i++) {
					ItemStack stack = size.getStackInSlot(i);
					if (!stack.isEmpty()) {
						spawnAsEntity(world, pos, stack);
					}
				}
			}
		}

		world.updateComparatorOutputLevel(pos, this);
		super.breakBlock(world, pos, state);
	}

	@Override
	@Nonnull
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox (IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.2, 0.0, 0.2, 0.8, 1.0, 0.8);
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
