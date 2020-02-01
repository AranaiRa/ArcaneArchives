package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.TemplateBlock;
import com.aranaira.arcanearchives.tiles.MakeshiftResonatorTile;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class MakeshiftResonatorBlock extends TemplateBlock {
	public static PropertyBool FILLED = PropertyBool.create("filled");

	public MakeshiftResonatorBlock () {
		super(Material.IRON);
		setHardness(3f);
		setHarvestLevel("pickaxe", 0);
		setDefaultState(getDefaultState().withProperty(FILLED, false));
	}

	@Override
	public boolean onBlockActivated (World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!state.getValue(FILLED)) {
			ItemStack stack = playerIn.getHeldItem(hand);
			IFluidHandlerItem cap = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			if (cap != null) {
				if (cap.getTankProperties().length > 0) {
					FluidStack contents = cap.getTankProperties()[0].getContents();
					if (contents != null) {
						if (contents.getFluid() == FluidRegistry.WATER) {
							if (contents.amount == 1000) {
								if (worldIn.isRemote) {
									return true;
								}
								cap.drain(1000, true);
								playerIn.setHeldItem(hand, cap.getContainer().getItem().getContainerItem(cap.getContainer()));
								worldIn.setBlockState(pos, state.withProperty(FILLED, true));
								return true;
							} else if (contents.amount > 1000) {
								if (!worldIn.isRemote) {
									cap.drain(1000, true);
									worldIn.setBlockState(pos, state.withProperty(FILLED, true));
								}

								return true;
							}
						}
					}
				}
			}
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public int damageDropped (IBlockState state) {
		return 0;
	}

	@Override
	public int getMetaFromState (IBlockState state) {
		return state.getValue(FILLED) ? 1 : 0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta (int meta) {
		return meta == 1 ? getDefaultState().withProperty(FILLED, true) : getDefaultState();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.device.wonky_resonator"));
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
	public boolean hasTileEntity (IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity (World world, IBlockState state) {
		return new MakeshiftResonatorTile();
	}

	@Override
	protected BlockStateContainer createBlockState () {
		return new BlockStateContainer(this, FILLED);
	}
}
