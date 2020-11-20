/*package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.DirectionalBlock;
import com.aranaira.arcanearchives.client.tracking.LineHandler;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.items.templates.IItemScepter;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity.TroveItemHandler;
import com.aranaira.arcanearchives.util.ItemUtils;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
public class RadiantTrove extends DirectionalBlock {
	public static final String NAME = "radiant_trove";

	public RadiantTrove () {
		super(NAME, Material.IRON);
		setSize(1, 1, 1);
		setLightLevel(16 / 16f);
		setHardness(3f);
		setResistance(6000F);
		setHarvestLevel("pickaxe", 0);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.device.radiant_trove"));
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean causesSuffocation (BlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube (BlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube (BlockState state) {
		return false;
	}

	@Override
	public Item getItemDropped (BlockState state, Random rand, int fortune) {
		return Items.AIR;
	}

	@Override
	public boolean removedByPlayer (BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest) {
		if (player.capabilities.isCreativeMode && !(player.getHeldItemMainhand().getItem() instanceof IItemScepter)) {
			onBlockClicked(world, pos, player);
			return false;
		}

		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}

	@Override
	public void harvestBlock (World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
		player.addStat(Stats.getBlockStats(this));
		player.addExhaustion(0.005F);

		if (worldIn.isRemote) {
			return;
		}

		harvesters.set(player);
		if (player.inventory.getCurrentItem().getItem() instanceof IItemScepter && player.isSneaking() && te instanceof RadiantTroveTileEntity) {
			RadiantTroveTileEntity rte = (RadiantTroveTileEntity) te;
			if (rte != null) {
				TroveItemHandler handler = rte.getInventory();
				while (!handler.isEmpty()) {
					ItemStack s = handler.extractItem(0, 64, false);
					spawnAsEntity(worldIn, pos, s);
				}
				IItemHandler optionals = rte.getOptionalUpgradesHandler();
				IItemHandler size = rte.getSizeUpgradesHandler();
				for (int i = 0; i < optionals.getSlots(); i++) {
					ItemStack s = optionals.getStackInSlot(i);
					if (!stack.isEmpty()) {
						spawnAsEntity(worldIn, pos, s);
					}
				}
				for (int i = 0; i < size.getSlots(); i++) {
					ItemStack s = size.getStackInSlot(i);
					if (!stack.isEmpty()) {
						spawnAsEntity(worldIn, pos, s);
					}
				}
				spawnAsEntity(worldIn, pos, new ItemStack(getItemBlock()));
			}
		} else {
			List<ItemStack> items = new java.util.ArrayList<>();
			items.add(generateStack(te, worldIn, pos));
			net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0f, true, player);
			for (ItemStack item : items) {
				spawnAsEntity(worldIn, pos, item);
			}
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

		CompoundNBT tag = new CompoundNBT();
		te.serializeStack(tag);

		ItemStack stack = new ItemStack(BlockRegistry.RADIANT_TROVE.getItemBlock());
		stack.setTag(tag);

		return stack;
	}

	@Override
	public void onBlockHarvested (World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
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
	public boolean onBlockActivated (World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
		LineHandler.removeLine(pos, player.dimension);

		if (!world.isRemote && hand == Hand.MAIN_HAND) {
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
	public void onBlockClicked (World world, BlockPos pos, PlayerEntity player) {
		BlockState state = world.getBlockState(pos);
		LineHandler.removeLine(pos, player.dimension);

		if (state.getBlock() == BlockRegistry.RADIANT_TROVE) {
			if (!world.isRemote) {
				Direction facing = Direction.fromAngle(state.get(getFacingProperty()).getHorizontalAngle() - 90.0);
				RayTraceResult rayResult = net.minecraftforge.common.ForgeHooks.rayTraceEyes(player, ((ServerPlayerEntity) player).interactionManager.getBlockReachDistance() + 1);
				if (rayResult == null) {
					return;
				}

				Direction side = rayResult.sideHit;

				if (side == facing || side == Direction.UP) {
					RadiantTroveTileEntity te = WorldUtil.getTileEntity(RadiantTroveTileEntity.class, world, pos);
					if (te == null) {
						return;
					}

					te.onLeftClickTrove(player);
				}
			}
		}
	}

	@Override
	public boolean hasTileEntity (BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity (World world, BlockState state) {
		return new RadiantTroveTileEntity();
	}

	@Override

	public void breakBlock (World world, BlockPos pos, BlockState state) {
		LineHandler.removeLine(pos, world.provider.getDimension());

		world.updateComparatorOutputLevel(pos, this);
		super.breakBlock(world, pos, state);
	}

	@Override
	@Nonnull
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox (
			BlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.2, 0.0, 0.2, 0.8, 1.0, 0.8);
	}

	@Override
	public boolean hasComparatorInputOverride (BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride (BlockState blockState, World worldIn, BlockPos pos) {
		return ItemUtils.calculateRedstoneFromTileEntity(worldIn.getTileEntity(pos));
	}

	@Override
	public void onBlockPlacedBy (
			@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull LivingEntity placer, @Nonnull ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, placer, stack);

		if (stack.hasTagCompound()) {
			RadiantTroveTileEntity te = WorldUtil.getTileEntity(RadiantTroveTileEntity.class, world, pos);
			if (te != null) {
				te.deserializeStack(stack.getTag());
				te.markDirty();
				te.defaultServerSideUpdate();
			}
		}
	}

	@Override
	@SuppressWarnings("warning")
	public float getPlayerRelativeBlockHardness (
			BlockState state, PlayerEntity player, World worldIn, BlockPos pos) {
		if (player.inventory.getCurrentItem().getItem() instanceof IItemScepter) {
			float hardness = state.getBlockHardness(worldIn, pos);
			return 5f / hardness / 30F;
		}
		return super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
	}
}*/
