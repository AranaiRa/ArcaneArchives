package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.AAGuiHandler;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.templates.BlockDirectionalTemplate;
import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.data.ClientNetwork;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.events.LineHandler;
import com.aranaira.arcanearchives.init.BlockRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class LecternManifest extends BlockDirectionalTemplate {

	public static final String name = "lectern_manifest";

	public LecternManifest() {
		super(name, Material.WOOD);
		setHarvestLevel("axe", 0);
		this.setHardness(1.5f);
		setSize(1, 2, 1);
		setLightLevel(16f/16f);
	}

	@Override
	public boolean hasOBJModel () {
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube (IBlockState state) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.device.lectern_manifest"));
	}

	@Override
	public boolean onBlockActivated (World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ClientNetwork network = NetworkHelper.getClientNetwork(playerIn.getUniqueID());
		network.manifestItems.clear();
		network.synchroniseManifest();

		playerIn.openGui(ArcaneArchives.instance, AAGuiHandler.MANIFEST, worldIn, pos.getX(), pos.getY(), pos.getZ());

		return true;
	}

	@Override
	public BlockRenderLayer getRenderLayer () {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public void breakBlock (World world, BlockPos pos, IBlockState state) {
		if (!world.isAirBlock(pos.up())) {
			world.setBlockState(pos.up(), Blocks.AIR.getDefaultState());
		}

		super.breakBlock(world, pos, state);
	}
}
