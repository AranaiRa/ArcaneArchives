package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.AAGuiHandler;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.tileentities.RadiantCraftingTableTileEntity;
import com.aranaira.arcanearchives.util.WorldUtil;
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

import javax.annotation.Nullable;
import java.util.List;

public class RadiantCraftingTable extends BlockTemplate {

	public static final String NAME = "radiant_crafting_table";

	public RadiantCraftingTable () {
		super(NAME, Material.GLASS);
		setLightLevel(16 / 16f);
		setHardness(1.7f);
		setHarvestLevel("pickaxe", 0);
	}

	@Override
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.device.radiantcraftingtable"));
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
		// ArcaneArchives.logger.info("TRYING TO OPEN GUI");
		playerIn.openGui(ArcaneArchives.instance, AAGuiHandler.RADIANT_CRAFTING_TABLE, worldIn, pos.getX(), pos.getY(), pos.getZ());

		return true;
	}

	@Override
	public boolean hasTileEntity (IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity (World world, IBlockState state) {
		return new RadiantCraftingTableTileEntity();
	}

	@Override
	public boolean hasOBJModel () {
		return true;
	}

	@Override
	public void breakBlock (World world, BlockPos pos, IBlockState state) {
		RadiantCraftingTableTileEntity te = WorldUtil.getTileEntity(RadiantCraftingTableTileEntity.class, world, pos);
		if (te != null) {
			te.blockBroken();
		}
		super.breakBlock(world, pos, state);
	}
}
