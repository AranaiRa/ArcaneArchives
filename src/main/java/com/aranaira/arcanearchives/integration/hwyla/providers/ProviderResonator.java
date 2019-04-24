package com.aranaira.arcanearchives.integration.hwyla.providers;

import com.aranaira.arcanearchives.tileentities.RadiantResonatorTileEntity;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class ProviderResonator implements IWailaDataProvider
{
	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		RadiantResonatorTileEntity te = (RadiantResonatorTileEntity) accessor.getTileEntity();
		if(te != null) {
			tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.data.tooltip.resonator_progress") + String.format(" %d%%", te.getPercentageComplete()));
			RadiantResonatorTileEntity.TickResult res = te.canTick();
			tooltip.add(res.getFormat() + I18n.format(res.getKey()));
		}

		return tooltip;
	}
}
