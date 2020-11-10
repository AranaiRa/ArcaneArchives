//package com.aranaira.arcanearchives.blocks;
//
//import com.aranaira.arcanearchives.AAGuiHandler;
//import com.aranaira.arcanearchives.ArcaneArchives;
//import com.aranaira.arcanearchives.blocks.templates.DirectionalBlock;
//import com.aranaira.arcanearchives.client.tracking.LineHandler;
//import com.aranaira.arcanearchives.data.DataHelper;
//import com.aranaira.arcanearchives.tileentities.RadiantFurnaceAccessorTileEntity;
//import com.aranaira.arcanearchives.tileentities.RadiantFurnaceTileEntity;
//import com.aranaira.arcanearchives.util.DropUtils;
//import com.google.common.collect.Lists;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.material.Material;
//import net.minecraft.block.properties.PropertyEnum;
//import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.client.resources.I18n;
//import net.minecraft.client.util.ITooltipFlag;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.*;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.text.TextFormatting;
//import net.minecraft.world.IBlockAccess;
//import net.minecraft.world.World;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//import net.minecraftforge.items.IItemHandler;
//
//import javax.annotation.Nullable;
//import java.util.List;
//import java.util.Objects;
//
//public class RadiantFurnace extends DirectionalBlock {
//	public static final AxisAlignedBB BB_MAIN = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
//	public static final AxisAlignedBB BB_ACCESSOR = new AxisAlignedBB(0, 0, 0, 1, 0.5, 1);
//	public static final PropertyEnum<AccessorType> ACCESSOR_TYPE = PropertyEnum.create("accessortype", AccessorType.class);
//	public static final PropertyEnum<Direction> FURNACE_FACING = PropertyEnum.create("furnace_facing", Direction.class, Direction.HORIZONTALS);
//
//	public static final String name = "radiant_furnace";
//
//	public RadiantFurnace () {
//		super(name, Material.ROCK);
//		setLightLevel(16 / 16f);
//		setHardness(3f);
//		setHarvestLevel("pickaxe", 0);
//		this.setDefaultState(this.getDefaultState().with(ACCESSOR_TYPE, AccessorType.BASE));
//	}
//
//	@Override
//	@SuppressWarnings("deprecation")
//	public AxisAlignedBB getBoundingBox (BlockState state, IBlockAccess source, BlockPos pos) {
//		AccessorType accessor = state.get(ACCESSOR_TYPE);
//		switch (accessor) {
//			case TOP:
//				return BB_ACCESSOR;
//			case BASE:
//			case BOTTOM:
//			default:
//				return BB_MAIN;
//		}
//	}
//
//	@Override
//	@SuppressWarnings("deprecation")
//	public BlockState getStateFromMeta (int meta) {
//		return getDefaultState().with(FURNACE_FACING, Direction.byHorizontalIndex(meta >> 2)).with(ACCESSOR_TYPE, AccessorType.byOrdinal(meta & 3));
//	}
//
//	@Override
//	public int getMetaFromState (BlockState state) {
//		return (state.get(FURNACE_FACING).getHorizontalIndex() << 2) ^ state.get(ACCESSOR_TYPE).ordinal();
//	}
//
//	@Override
//	public boolean hasOBJModel () {
//		return true;
//	}
//
//	@Override
//	public void breakBlock (World world, BlockPos pos, BlockState state) {
//		if (state.get(ACCESSOR_TYPE) != AccessorType.BASE) {
//			return;
//		}
//
//		LineHandler.removeLine(pos, world.provider.getDimension());
//
//		TileEntity te = world.getTileEntity(pos);
//		if (te != null) {
//			IItemHandler inv = null;
//			if (te instanceof RadiantFurnaceTileEntity) {
//				inv = ((RadiantFurnaceTileEntity) te).getInventory();
//			} else if (te instanceof RadiantFurnaceAccessorTileEntity) {
//				inv = Objects.requireNonNull(((RadiantFurnaceAccessorTileEntity) te).getParent()).getInventory();
//			}
//			if (inv != null) {
//				DropUtils.dropInventoryItems(world, pos, inv);
//			}
//		}
//
//		super.breakBlock(world, pos, state);
//	}
//
//	public BlockPos getConnectedPos (BlockPos pos, BlockState state) {
//		return pos.offset(state.get(FURNACE_FACING));
//	}
//
//	@Override
//	@SuppressWarnings("deprecation")
//	public void neighborChanged (BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
//		if (state.get(ACCESSOR_TYPE) == AccessorType.BOTTOM) {
//			if (world.isAirBlock(getConnectedPos(pos, state)) || world.isAirBlock(pos.up())) {
//				world.setBlockToAir(pos);
//				world.setBlockToAir(pos.up());
//			}
//		} else if (state.get(ACCESSOR_TYPE) == AccessorType.TOP) {
//			if (world.isAirBlock(pos) || world.isAirBlock(getConnectedPos(pos, state).down())) {
//				world.setBlockToAir(pos);
//				world.setBlockToAir(pos.down());
//			}
//		} else {
//			BlockPos thingy = pos.offset(Direction.fromAngle(state.get(FURNACE_FACING).getHorizontalAngle() - 180));
//			if (world.isAirBlock(thingy) || world.isAirBlock(thingy.up())) {
//				// TODO: PARTICLES
//				this.breakBlock(world, pos, state);
//				world.setBlockToAir(pos);
//			}
//		}
//
//		super.neighborChanged(state, world, pos, blockIn, fromPos);
//	}
//
//	@Override
//	public boolean onBlockActivated (World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
//		BlockState stateUp = worldIn.getBlockState(pos.up());
//
//		AccessorType accessor = state.get(ACCESSOR_TYPE);
//		if (accessor == AccessorType.TOP) {
//			pos = getConnectedPos(pos, state).down();
//		} else if (accessor == AccessorType.BOTTOM) {
//			pos = getConnectedPos(pos, state);
//		}
//
//		LineHandler.removeLine(pos, playerIn.dimension);
//
//		if (worldIn.isRemote) {
//			return true;
//		}
//
//		playerIn.openGui(ArcaneArchives.instance, AAGuiHandler.RADIANT_FURNACE, worldIn, pos.getX(), pos.getY(), pos.getZ());
//
//		return true;
//	}
//
//	@Override
//	@OnlyIn(Dist.CLIENT)
//	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
//		tooltip.add(TextFormatting.RED + "" + TextFormatting.BOLD + I18n.format("arcanearchives.tooltip.notimplemented1"));
//		tooltip.add(TextFormatting.RED + "" + TextFormatting.ITALIC + I18n.format("arcanearchives.tooltip.notimplemented2"));
//	}
//
//	@Override
//	@SuppressWarnings("deprecation")
//	public boolean isFullCube (BlockState state) {
//		return false;
//	}
//
//	@Override
//	@SuppressWarnings("deprecation")
//	public boolean isOpaqueCube (BlockState state) {
//		return false;
//	}
//
//	@Override
//	public BlockRenderLayer getRenderLayer () {
//		return BlockRenderLayer.CUTOUT;
//	}
//
//	@Override
//	protected BlockStateContainer createBlockState () {
//		return new BlockStateContainer(this, FURNACE_FACING, ACCESSOR_TYPE);
//	}
//
//	@Override
//	public boolean hasTileEntity (BlockState state) {
//		return true;
//	}
//
//	@Nullable
//	@Override
//	public TileEntity createTileEntity (World world, BlockState state) {
//		if (state.get(ACCESSOR_TYPE) == AccessorType.BASE) {
//			return new RadiantFurnaceTileEntity();
//		} else if (state.get(ACCESSOR_TYPE) == AccessorType.TOP) {
//			return new RadiantFurnaceAccessorTileEntity(Direction.fromAngle(state.get(FURNACE_FACING).getHorizontalAngle() - 180), false);
//		} else {
//			return new RadiantFurnaceAccessorTileEntity(Direction.fromAngle(state.get(FURNACE_FACING).getHorizontalAngle() - 180), true);
//		}
//	}
//
//	@Override
//	public void handleAccessors (World world, BlockPos pos, LivingEntity placer) {
//		Direction facing = getFacing(world, pos);
//		Direction curOffset = Direction.fromAngle(facing.getHorizontalAngle() - 180);
//
//		BlockPos bottom = pos.offset(curOffset);
//		BlockPos top = bottom.up();
//
//		if (!world.isRemote) {
//			AccessorSaveData data = DataHelper.getAcccessorData();
//			data.setAccessors(world.provider.getDimension(), pos, Lists.newArrayList(bottom, top));
//
//			world.setBlockState(bottom, this.getDefaultState().with(ACCESSOR_TYPE, AccessorType.BOTTOM).with(FURNACE_FACING, facing));
//			world.setBlockState(top, this.getDefaultState().with(ACCESSOR_TYPE, AccessorType.TOP).with(FURNACE_FACING, facing));
//		}
//	}
//
//	@Override
//	public PropertyEnum<Direction> getFacingProperty () {
//		return FURNACE_FACING;
//	}
//
//	public enum AccessorType implements IStringSerializable {
//		BASE("base"),
//		BOTTOM("bottom"),
//		TOP("top");
//
//		private String name;
//
//		AccessorType (String name) {
//			this.name = name;
//		}
//
//		@Override
//		public String getName () {
//			return name;
//		}
//
//		@Override
//		public String toString () {
//			return name;
//		}
//
//		public static AccessorType byOrdinal (int ordinal) {
//			int i = 0;
//			for (AccessorType type : values()) {
//				if (ordinal == i) {
//					return type;
//				}
//				i++;
//			}
//			return BASE;
//		}
//	}
//}
