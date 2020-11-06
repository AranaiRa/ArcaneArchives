package com.aranaira.arcanearchives.integration.top;

import com.aranaira.arcanearchives.blocks.RadiantChestBlock;
import com.aranaira.arcanearchives.blocks.RadiantResonatorBlock;
import com.aranaira.arcanearchives.blocks.RadiantTrove;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantResonatorTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity;
import com.aranaira.arcanearchives.util.WorldUtil;
import mcjty.theoneprobe.api.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.function.Function;

@SuppressWarnings("unused")
public class TOPPlugin implements Function<ITheOneProbe, Void>, IProbeInfoProvider {
	@Override
	public String getID () {
		return "arcanearchives:top_integration";
	}

	@Override
	public void addProbeInfo (ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
		Block block = blockState.getBlock();
		if (block instanceof RadiantChestBlock) {
			RadiantChestTileEntity chest = WorldUtil.getTileEntity(RadiantChestTileEntity.class, world, data.getPos());
			if (chest != null) {
				String chestName = chest.getChestName();
				if (!chestName.isEmpty()) {
					probeInfo.text(TextFormatting.GOLD + "{*arcanearchives.data.tooltip.chest_name*} " + chestName);
				}
			}
		} else if (block instanceof RadiantResonatorBlock) {
			RadiantResonatorTileEntity resonator = WorldUtil.getTileEntity(RadiantResonatorTileEntity.class, world, data.getPos());
			if (resonator != null) {
				probeInfo.text(TextFormatting.GOLD + "{*arcanearchives.data.tooltip.resonator_progress*} " + String.format("%d%%", resonator.getPercentageComplete()));
				RadiantResonatorTileEntity.TickResult res = resonator.canTick();
				probeInfo.text(res.getFormat() + "{*" + res.getKey() + "*}");
			}
		} else if (block instanceof RadiantTrove) {
			RadiantTroveTileEntity trove = WorldUtil.getTileEntity(RadiantTroveTileEntity.class, world, data.getPos());
			if (trove != null) {
				probeInfo.text(TextFormatting.GOLD + "" + trove.getInventory().getCount() + " {*arcanearchives.data.tooltip.trove_count*} " + "{*" + trove.getInventory().getItem().getTranslationKey() + ".name*}");
			}
		}
	}

	@Override
	public Void apply (ITheOneProbe iTheOneProbe) {
		init(iTheOneProbe);
		return null;
	}

	public void init (ITheOneProbe top) {
		top.registerProvider(this);
	}
}
