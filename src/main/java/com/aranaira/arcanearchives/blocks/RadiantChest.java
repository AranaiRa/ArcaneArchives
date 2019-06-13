package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.AAGuiHandler;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.events.LineHandler;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.DropHelper;
import com.aranaira.arcanearchives.util.ItemUtilities;
import com.aranaira.arcanearchives.util.WorldUtil;
import com.google.common.collect.Lists;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@SuppressWarnings("deprecation")
public class RadiantChest extends BlockTemplate {
	public static final String NAME = "radiant_chest";

	public RadiantChest () {
		super(NAME, Material.GLASS);
		setLightLevel(16 / 16f);
		setHardness(1.7f);
		setResistance(6000F);
		setHarvestLevel("axe", 0);
	}

	@Override
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.device.radiant_chest"));
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
	public boolean onBlockActivated (World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		LineHandler.removeLine(pos);

		if (worldIn.isRemote) {
			return true;
		}

		ItemStack mainHand = playerIn.getHeldItemMainhand();
		ItemStack offHand = playerIn.getHeldItemOffhand();
		ItemStack displayStack = ItemStack.EMPTY;

		if (mainHand.getItem() == ItemRegistry.SCEPTER_MANIPULATION && !offHand.isEmpty()) {
			displayStack = offHand;
		} else if (offHand.getItem() == ItemRegistry.SCEPTER_MANIPULATION && !mainHand.isEmpty()) {
			displayStack = mainHand;
		}

		boolean clearDisplayed = false;

		if ((mainHand.getItem() == ItemRegistry.SCEPTER_MANIPULATION && offHand.isEmpty() || offHand.getItem() == ItemRegistry.SCEPTER_MANIPULATION && mainHand.isEmpty()) && playerIn.isSneaking()) {
			clearDisplayed = true;
		}

		if (!displayStack.isEmpty() || clearDisplayed) {
			RadiantChestTileEntity te = WorldUtil.getTileEntity(RadiantChestTileEntity.class, worldIn, pos);
			if (te != null) {
				if (!displayStack.isEmpty()) {
					te.setDisplay(displayStack, facing);
				} else {
					te.setDisplay(ItemStack.EMPTY, EnumFacing.NORTH);
				}
			}
		} else {
			playerIn.openGui(ArcaneArchives.instance, AAGuiHandler.RADIANT_CHEST, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}

	@Override
	public boolean hasTileEntity (IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity (World world, IBlockState state) {
		return new RadiantChestTileEntity();
	}

	@Override
	public boolean hasOBJModel () {
		return true;
	}

	@Override
	public void breakBlock (World worldIn, BlockPos pos, IBlockState state) {
		LineHandler.removeLine(pos);

		if (!worldIn.isRemote) {
			TileEntity te = worldIn.getTileEntity(pos);
			if (te instanceof RadiantChestTileEntity) {
				ServerNetwork network = NetworkHelper.getServerNetwork(((RadiantChestTileEntity) te).networkId, worldIn);

				// This is never an IInventory
				IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				DropHelper.dropInventoryItems(worldIn, pos, inv);
			}
		}

		worldIn.updateComparatorOutputLevel(pos, this);
		super.breakBlock(worldIn, pos, state);
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