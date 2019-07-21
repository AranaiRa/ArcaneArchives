package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.BlockDirectionalTemplate;
import com.aranaira.arcanearchives.client.render.LineHandler;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity.TroveItemHandler;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity;
import com.aranaira.arcanearchives.util.ItemUtils;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
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
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
public class RadiantTrove extends BlockDirectionalTemplate {
	public static final String NAME = "radiant_trove";

	public RadiantTrove () {
		super(NAME, Material.WOOD);
		setSize(1, 1, 1);
		setLightLevel(16 / 16f);
		setHardness(3f);
		setResistance(6000F);
		setHarvestLevel("pickaxe", 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
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
	public Item getItemDropped (IBlockState state, Random rand, int fortune) {
		return Items.AIR;
	}

	@Override
	public void harvestBlock (World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
		player.addStat(StatList.getBlockStats(this));
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
		if (!(incomingTe instanceof RadiantTroveTileEntity)) {
			return new ItemStack(BlockRegistry.RADIANT_TROVE.getItemBlock());
		}

		RadiantTroveTileEntity te = (RadiantTroveTileEntity) incomingTe;

		if (te.wasCreativeDrop) {
			return ItemStack.EMPTY;
		}

		NBTTagCompound tag = new NBTTagCompound();
		te.serializeStack(tag);

		ItemStack stack = new ItemStack(BlockRegistry.RADIANT_TROVE.getItemBlock());
		stack.setTagCompound(tag);

		return stack;
	}

	@Override
	public void onBlockHarvested (World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		RadiantTroveTileEntity te = WorldUtil.getTileEntity(RadiantTroveTileEntity.class, worldIn, pos);
		if (te != null) {
			te.wasCreativeDrop = player.capabilities.isCreativeMode;
		}
		super.onBlockHarvested(worldIn, pos, state, player);
	}

	@Override
	public BlockRenderLayer getRenderLayer () {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		LineHandler.removeLine(pos, player.dimension);

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
		LineHandler.removeLine(pos, player.dimension);

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

	/*@Override
	@ParametersAreNonnullByDefault
	public void breakBlock (World world, BlockPos pos, IBlockState state) {
		LineHandler.removeLine(pos, world.provider.getDimension());

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
	}*/

	@Override
	@ParametersAreNonnullByDefault
	public void breakBlock (World world, BlockPos pos, IBlockState state) {
		LineHandler.removeLine(pos, world.provider.getDimension());

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
		return ItemUtils.calculateRedstoneFromTileEntity(worldIn.getTileEntity(pos));
	}

	@Override
	public void onBlockPlacedBy (@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityLivingBase placer, @Nonnull ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, placer, stack);

		if (stack.hasTagCompound()) {
			RadiantTroveTileEntity te = WorldUtil.getTileEntity(RadiantTroveTileEntity.class, world, pos);
			if (te != null) {
				te.deserializeStack(stack.getTagCompound());
				te.markDirty();
				te.defaultServerSideUpdate();
			}
		}

	}
}
