package com.aranaira.arcanearchives.items.templates;

import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.data.ArcaneArchivesNetwork;
import com.aranaira.arcanearchives.data.NetworkHelper;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemBlockTemplate extends ItemBlock
{
	private BlockTemplate blockTemplate;

	public ItemBlockTemplate(@Nonnull BlockTemplate block)
	{
		super(block);

		this.blockTemplate = block;

		assert block.getRegistryName() != null;

		setRegistryName(block.getRegistryName());
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		if(world.isRemote || player instanceof FakePlayer)
		{
			return EnumActionResult.FAIL;
		}

		ArcaneArchivesNetwork network = NetworkHelper.getArcaneArchivesNetwork(player.getUniqueID());

		int placeLimit = blockTemplate.getPlaceLimit();

		if(placeLimit != -1)
		{
			if(network == null)
			{

				player.sendStatusMessage(new TextComponentTranslation("arcanearchives.error.invalidnetwork"), true);
				return EnumActionResult.FAIL;
			} else if(network.CountTileEntities(blockTemplate.getEntityClass()) >= placeLimit)
			{
				player.sendStatusMessage(new TextComponentTranslation("arcanearchives.error.toomanyplaced", blockTemplate.getPlaceLimit(), blockTemplate.getNameComponent()), true);
				return EnumActionResult.FAIL;
			}
		}

		BlockPos up = pos.up();
		int height = blockTemplate.getSize().height;

		if(up.getY() + height > world.getHeight())
		{
			player.sendStatusMessage(new TextComponentTranslation("arcanearchives.error.aboveworld", blockTemplate.getNameComponent()), true);
			return EnumActionResult.FAIL;
		}

		if(blockTemplate.hasAccessors())
		{
			boolean safe = true;

			EnumFacing dir = EnumFacing.fromAngle(player.rotationYaw - 90);

			for(BlockPos point : blockTemplate.calculateAccessors(world, up, dir))
			{
				IBlockState newState = world.getBlockState(point);
				Block newBlock = newState.getBlock();
				if(!newBlock.isAir(newState, world, point) && !newBlock.isReplaceable(world, point))
				{
					safe = false;
					break;
				}
			}

			if(!safe)
			{
				player.sendStatusMessage(new TextComponentTranslation("arcanearchives.error.notenoughspace", blockTemplate.getNameComponent()), true);
				return EnumActionResult.FAIL;
			}
		}

		return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
	}
}
