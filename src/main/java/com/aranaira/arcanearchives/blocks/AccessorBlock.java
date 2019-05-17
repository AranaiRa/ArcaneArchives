package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.tileentities.AccessorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
// TODO: Break textures
// TODO: WAILA, TUMAT, TOP, etc support
public class AccessorBlock extends BlockTemplate {
	public AccessorBlock () {
		super("accessorblock", Material.ROCK);
		setTranslationKey("accessorblock");
	}

	@Override
	// Called before the tile entity itself is removed
	public void breakBlock (World world, BlockPos pos, IBlockState state) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof AccessorTileEntity) {
			((AccessorTileEntity) te).breakBlock();
		}

		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean hasAccessors () {
		return false;
	}

	@Override
	public boolean isTopSolid (IBlockState state) {
		return false;
	}

	/* -------------------------------- */
	/* Overriden calls via block state	*/
	@Override
	public boolean isFullBlock (IBlockState state) {
		return true;
	}

	@Override
	public boolean canEntitySpawn (IBlockState state, Entity entityIn) {
		return false;
	}

	@Override
	public int getLightOpacity (IBlockState state) {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isTranslucent (IBlockState state) {
		return true;
	}

	@Override
	public MapColor getMapColor (IBlockState state, IBlockAccess world, BlockPos pos) {
		Parent parent = getBlock(world, pos);
		if (parent == null) {
			return super.getMapColor(state, world, pos);
		}
		return parent.block().getMapColor(parent.state(), world, parent.pos());
	}

	@Override
	public boolean causesSuffocation (IBlockState state) {
		return true;
	}

	@Override
	public EnumBlockRenderType getRenderType (IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}

	@Override
	public float getBlockHardness (IBlockState blockState, World world, BlockPos pos) {
		Parent parent = getBlock(world, pos);
		if (parent == null) {
			return super.getBlockHardness(blockState, world, pos);
		}
		return parent.block().getBlockHardness(parent.state(), world, parent.pos());
	}

	@Override
	public boolean isOpaqueCube (IBlockState state) {
		return false;
	}

	@Override
	public void onBlockAdded (World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
	}

	@Override
	public Item getItemDropped (IBlockState state, Random rand, int fortune) {
		return Items.AIR;
	}

	@Override
	public float getPlayerRelativeBlockHardness (IBlockState state, EntityPlayer player, World world, BlockPos pos) {
		Parent parent = getBlock(world, pos);
		if (parent == null) {
			return super.getPlayerRelativeBlockHardness(state, player, world, pos);
		}
		return parent.block().getPlayerRelativeBlockHardness(parent.state(), player, world, parent.pos());
	}

	@Override
	public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof AccessorTileEntity) {
			return ((AccessorTileEntity) te).onBlockActivated(pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
		}

		return super.onBlockActivated(world, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public int getWeakPower (IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		Parent parent = getBlock(blockAccess, pos);
		if (parent == null) {
			return super.getWeakPower(blockState, blockAccess, pos, side);
		}
		return parent.block().getWeakPower(parent.state(), blockAccess, parent.pos(), side);
	}

	@Override
	public int getStrongPower (IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		Parent parent = getBlock(blockAccess, pos);
		if (parent == null) {
			return super.getStrongPower(blockState, blockAccess, pos, side);
		}
		return parent.block().getStrongPower(parent.state(), blockAccess, parent.pos(), side);
	}

	@Override
	public boolean eventReceived (IBlockState state, World world, BlockPos pos, int id, int param) {
		Parent parent = getBlock(world, pos);
		if (parent == null) {
			return super.eventReceived(state, world, pos, id, param);
		}
		return parent.block().eventReceived(parent.state(), world, parent.pos(), id, param);
	}

	@Override
	public int getComparatorInputOverride (IBlockState blockState, World world, BlockPos pos) {
		Parent parent = getBlock(world, pos);
		if (parent == null) {
			return super.getComparatorInputOverride(blockState, world, pos);
		}
		return parent.block().getComparatorInputOverride(parent.state(), world, parent.pos());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.RED + "" + TextFormatting.ITALIC + I18n.format("arcanearchives.tooltip.shouldnothave"));
	}

	@Override
	public boolean isSideSolid (IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Override
	public boolean hasTileEntity (IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity (World world, IBlockState state) {
		return new AccessorTileEntity();
	}

	@Override
	public float getExplosionResistance (World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
		Parent parent = getBlock(world, pos);
		if (parent == null) {
			return super.getExplosionResistance(world, pos, exploder, explosion);
		}
		return parent.block().getExplosionResistance(world, parent.pos(), exploder, explosion);
	}

	@Override
	public ItemStack getPickBlock (IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		TileEntity entity = world.getTileEntity(pos);
		if (entity instanceof AccessorTileEntity) {

			Block returnBlock = ((AccessorTileEntity) entity).getParentBlock();
			if (returnBlock != null) {
				return new ItemStack(Item.getItemFromBlock(returnBlock), 1, this.damageDropped(state));
			}
		}
		return super.getPickBlock(state, target, world, pos, player);
	}

	private Parent getBlock (IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (!(te instanceof AccessorTileEntity)) {
			return null;
		}
		BlockPos parent = ((AccessorTileEntity) te).getParent();
		Block block = world.getBlockState(parent).getBlock();
		return new Parent(block, parent);
	}

	private class Parent {
		private Block parentBlock;
		private BlockPos parentPos;

		Parent (Block block, BlockPos pos) {
			this.parentBlock = block;
			this.parentPos = pos;
		}

		public Block block () {
			return parentBlock;
		}

		public BlockPos pos () {
			return parentPos;
		}

		public IBlockState state () {
			return parentBlock.getDefaultState();
		}
	}
}

