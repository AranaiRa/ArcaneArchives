package com.aranaira.arcanearchives.compat.top;

import com.aranaira.arcanearchives.blocks.RadiantChest;
import com.aranaira.arcanearchives.blocks.RadiantResonator;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantResonatorTileEntity;
import com.aranaira.arcanearchives.util.WorldUtil;
import mcjty.theoneprobe.api.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.function.Function;

@SuppressWarnings("unused")
public class TOPPlugin implements Function<ITheOneProbe, Void>, IProbeInfoProvider
{
	public void init(ITheOneProbe top)
	{
		top.registerProvider(this);
	}

	@Override
	public String getID()
	{
		return "arcanearchives:top_integration";
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data)
	{
		Block block = blockState.getBlock();
		if(block instanceof RadiantChest)
		{
			RadiantChestTileEntity chest = WorldUtil.getTileEntity(RadiantChestTileEntity.class, world, data.getPos());
			if(chest != null)
			{
				String chestName = chest.getChestName();
				if(!chestName.isEmpty())
				{
					probeInfo.text(TextFormatting.GOLD + "Name: " + chestName);
				}
			}
		} else if(block instanceof RadiantResonator)
		{
			RadiantResonatorTileEntity resonator = WorldUtil.getTileEntity(RadiantResonatorTileEntity.class, world, data.getPos());
			if(resonator != null)
			{
				probeInfo.text(String.format(TextFormatting.GOLD + "Progress: %d%%", resonator.getPercentageComplete()));
				probeInfo.text(resonator.canTick() ? TextFormatting.GREEN + "Resonating" : TextFormatting.RED + "Offline");
			}
		}
	}

	@Override
	public Void apply(ITheOneProbe iTheOneProbe)
	{
		init(iTheOneProbe);
		return null;
	}
}
