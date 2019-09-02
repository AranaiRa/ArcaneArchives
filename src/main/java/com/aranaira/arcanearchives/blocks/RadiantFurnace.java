package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.BlockDirectionalTemplate;
import com.aranaira.arcanearchives.client.render.LineHandler;
import com.aranaira.arcanearchives.tileentities.RadiantFurnaceTileEntity;
import com.aranaira.arcanearchives.util.DropUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

public class RadiantFurnace extends BlockDirectionalTemplate {
	public static final AxisAlignedBB BB_MAIN = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	public static final AxisAlignedBB BB_ACCESSOR = new AxisAlignedBB(0, 0, 0, 1, 0.5, 1);
	public static final PropertyEnum<AccessorType> ACCESSOR_TYPE = PropertyEnum.create("accessortype", AccessorType.class);

	public static final String name = "radiant_furnace";

	public RadiantFurnace () {
		super(name, Material.ROCK);
		setLightLevel(16 / 16f);
		setHardness(3f);
		setHarvestLevel("pickaxe", 0);
		this.setDefaultState(this.getDefaultState().withProperty(ACCESSOR_TYPE, AccessorType.BASE));
	}

	@Override
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox (IBlockState state, IBlockAccess source, BlockPos pos) {
		AccessorType accessor = state.getValue(ACCESSOR_TYPE);
		switch (accessor) {
			case TOP:
				return BB_ACCESSOR;
			case BASE:
			case BOTTOM:
			default:
				return BB_MAIN;
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState getStateFromMeta (int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta >> 1)).withProperty(ACCESSOR_TYPE, AccessorType.fromOrdinal(meta & 1));
	}

	@Override
	public int getMetaFromState (IBlockState state) {
		return state.getValue(FACING).getIndex() << 1 ^ (state.getValue(ACCESSOR_TYPE).ordinal());
	}

	@Override
	public boolean hasOBJModel () {
		return true;
	}

	@Override
	public void breakBlock (World world, BlockPos pos, IBlockState state) {
		if (state.getValue(ACCESSOR_TYPE) != AccessorType.BASE) {
			return;
		}

		LineHandler.removeLine(pos, world.provider.getDimension());

		TileEntity te = world.getTileEntity(pos);
		if (te instanceof RadiantFurnaceTileEntity) {
			IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			DropUtils.dropInventoryItems(world, pos, inv);
		}

		super.breakBlock(world, pos, state);
	}

	public BlockPos getConnectedPos (BlockPos pos, IBlockState state) {
		return pos.offset(state.getValue(FACING));
	}

	@Override
	@SuppressWarnings("deprecation")
	public void neighborChanged (IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (state.getValue(ACCESSOR_TYPE) == AccessorType.BOTTOM) {
			if (world.isAirBlock(getConnectedPos(pos, state)) || world.isAirBlock(pos.up())) {
				world.setBlockToAir(pos);
				world.setBlockToAir(pos.up());
			}
		} else if (state.getValue(ACCESSOR_TYPE) == AccessorType.TOP) {
			if (world.isAirBlock(pos) || world.isAirBlock(getConnectedPos(pos, state).down())) {
				world.setBlockToAir(pos);
				world.setBlockToAir(pos.down());
			}
		} else {
			BlockPos thingy = pos.offset(EnumFacing.fromAngle(state.getValue(FACING).getHorizontalAngle() - 180));
			if (world.isAirBlock(thingy) || world.isAirBlock(thingy.up())) {
				// TODO: PARTICLES
				this.breakBlock(world, pos, state);
				world.setBlockToAir(pos);
			}
		}

		super.neighborChanged(state, world, pos, blockIn, fromPos);
	}

	@Override
	public boolean onBlockActivated (World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState stateUp = worldIn.getBlockState(pos.up());

		AccessorType accessor = state.getValue(ACCESSOR_TYPE);
		if (accessor == AccessorType.TOP) {
			pos = getConnectedPos(pos, state).down();
		} else if (accessor == AccessorType.BOTTOM) {
			pos = getConnectedPos(pos, state);
		}

		LineHandler.removeLine(pos, playerIn.dimension);

		if (worldIn.isRemote) {
			return true;
		}

		//playerIn.openGui(ArcaneArchives.instance, AAGuiHandler.GEMCUTTERS_TABLE, worldIn, pos.getX(), pos.getY(), pos.getZ());

		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.RED + "" + TextFormatting.BOLD + I18n.format("arcanearchives.tooltip.notimplemented1"));
		tooltip.add(TextFormatting.RED + "" + TextFormatting.ITALIC + I18n.format("arcanearchives.tooltip.notimplemented2"));
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
	protected BlockStateContainer createBlockState () {
		return new BlockStateContainer(this, FACING, ACCESSOR_TYPE);
	}

	@Override
	public void handleAccessors (World world, BlockPos pos, EntityLivingBase placer) {
		EnumFacing facing = getFacing(world, pos);
		EnumFacing curOffset = EnumFacing.fromAngle(facing.getHorizontalAngle() - 180);

		BlockPos bottom = pos.offset(curOffset);
		BlockPos top = bottom.up();

		world.setBlockState(bottom, this.getDefaultState().withProperty(ACCESSOR_TYPE, AccessorType.BOTTOM).withProperty(FACING, facing));
		world.setBlockState(top, this.getDefaultState().withProperty(ACCESSOR_TYPE, AccessorType.TOP).withProperty(FACING, facing));
	}

	public enum AccessorType implements IStringSerializable {
		BASE("base"),
		BOTTOM("bottom"),
		TOP("top");

		private String name;

		AccessorType (String name) {
			this.name = name;
		}

		@Override
		public String getName () {
			return name;
		}

		@Override
		public String toString () {
			return name;
		}

		public static AccessorType fromOrdinal (int ordinal) {
			int i = 0;
			for (AccessorType type : values()) {
				if (ordinal == i) {
					return type;
				}
				i++;
			}
			return BASE;
		}
	}
}
