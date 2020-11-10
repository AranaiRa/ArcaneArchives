/*package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.AAGuiHandler;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.templates.DirectionalBlock;
import com.aranaira.arcanearchives.data.types.ClientNetwork;
import com.aranaira.arcanearchives.data.DataHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class LecternManifest extends DirectionalBlock {

	public static final String name = "lectern_manifest";

	public LecternManifest () {
		super(name, Material.WOOD);
		setHarvestLevel("axe", 0);
		this.setHardness(1.5f);
		setSize(1, 2, 1);
		setLightLevel(16f / 16f);
		this.setDefaultState(this.getDefaultState().with(ACCESSOR, false));
	}

	@Override
	public boolean hasOBJModel () {
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube (BlockState state) {
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.device.lectern_manifest"));
	}

	@Override
	public boolean onBlockActivated (World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			ClientNetwork network = DataHelper.getClientNetwork(playerIn.getUniqueID());
			network.manifestItems.clear();
			network.synchroniseManifest();
			playerIn.openGui(ArcaneArchives.instance, AAGuiHandler.MANIFEST, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}

	@Override
	public BlockRenderLayer getRenderLayer () {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	@SuppressWarnings("deprecation")
	public BlockState getStateFromMeta (int meta) {
		return getDefaultState().with(getFacingProperty(), Direction.byIndex(meta >> 1)).with(ACCESSOR, (meta & 1) != 0);
	}

	@Override
	public int getMetaFromState (BlockState state) {
		return state.get(getFacingProperty()).getIndex() << 1 ^ (state.get(ACCESSOR) ? 1 : 0);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void neighborChanged (BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (state.get(ACCESSOR)) {
			if (world.isAirBlock(pos.down())) {
				world.setBlockToAir(pos);
			}
		} else {
			if (world.isAirBlock(pos.up())) {
				// TODO: PARTICLES
				world.setBlockToAir(pos);
			}
		}

		super.neighborChanged(state, world, pos, blockIn, fromPos);
	}

	@Override
	protected BlockStateContainer createBlockState () {
		return new BlockStateContainer(this, getFacingProperty(), ACCESSOR);
	}

}*/
