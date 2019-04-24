package com.aranaira.arcanearchives.items.templates;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.RadiantResonator;
import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.blocks.unused.MatrixCrystalCore;
import com.aranaira.arcanearchives.data.ClientNetwork;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.init.BlockRegistry;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemBlockTemplate extends ItemBlock
{
	private BlockTemplate blockTemplate;

	public ItemBlockTemplate(@Nonnull BlockTemplate block) {
		super(block);

		this.blockTemplate = block;
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		Block blockTemplate = ((ItemBlock) stack.getItem()).getBlock();
		if (blockTemplate == BlockRegistry.RADIANT_TROVE || blockTemplate == BlockRegistry.RADIANT_TANK || blockTemplate == BlockRegistry.GEMCUTTERS_TABLE) {
			return EnumRarity.EPIC;
		}
		if (blockTemplate == BlockRegistry.RADIANT_CHEST || blockTemplate == BlockRegistry.RADIANT_RESONATOR) {
			return EnumRarity.RARE;
		}

		return EnumRarity.COMMON;
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		int totalResonators = 0;
		int totalCores = 0;

		int placeLimit = blockTemplate.getPlaceLimit();

		if(placeLimit != -1) {
			if(world.isRemote) {
				ClientNetwork network = NetworkHelper.getClientNetwork(player.getUniqueID());
				totalResonators = network.getTotalResonators();
				totalCores = network.getTotalCores();
			} else {
				ServerNetwork network = NetworkHelper.getServerNetwork(player.getUniqueID(), world);
				if(network == null) {
					player.sendStatusMessage(new TextComponentTranslation("arcanearchives.error.invalidnetwork"), true);
					return EnumActionResult.FAIL;
				} else {
					totalResonators = network.getTotalResonators();
					totalCores = network.getTotalCores();
				}
			}

			if((blockTemplate instanceof RadiantResonator && totalResonators >= placeLimit) || blockTemplate instanceof MatrixCrystalCore && totalCores >= placeLimit) {
				if(!world.isRemote)
					player.sendStatusMessage(new TextComponentTranslation("arcanearchives.error.toomanyplaced", blockTemplate.getPlaceLimit(), blockTemplate.getNameComponent()), true);
				return EnumActionResult.FAIL;
			}
		}

		BlockPos up = pos.up();
		int height = blockTemplate.getSize().height;

		if(up.getY() + height > world.getHeight()) {
			if(!world.isRemote)
				player.sendStatusMessage(new TextComponentTranslation("arcanearchives.error.aboveworld", blockTemplate.getNameComponent()), true);
			return EnumActionResult.FAIL;
		}

		if(blockTemplate.hasAccessors()) {
			boolean safe = true;

			EnumFacing dir = EnumFacing.fromAngle(player.rotationYaw - 90);

			for(BlockPos point : blockTemplate.calculateAccessors(world, up, dir)) {
				IBlockState newState = world.getBlockState(point);
				Block newBlock = newState.getBlock();
				if(!newBlock.isAir(newState, world, point) && !newBlock.isReplaceable(world, point)) {
					safe = false;
					break;
				}
			}

			if(!safe) {
				if(!world.isRemote)
					player.sendStatusMessage(new TextComponentTranslation("arcanearchives.error.notenoughspace", blockTemplate.getNameComponent()), true);
				return EnumActionResult.FAIL;
			}
		}

		if(placeLimit != -1 && world.isRemote) {
			ArcaneArchives.logger.info(String.format("[DEBUG ONLY MESSAGE] Successfully placed %s, with %d total resonators and %d total cores in the client network.", blockTemplate.getLocalizedName(), totalResonators, totalCores));
		}

		return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
	}
}
