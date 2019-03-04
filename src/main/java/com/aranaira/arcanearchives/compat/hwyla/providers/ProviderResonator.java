package com.aranaira.arcanearchives.compat.hwyla.providers;

import com.aranaira.arcanearchives.tileentities.RadiantResonatorTileEntity;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class ProviderResonator implements IWailaDataProvider
{
	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		RadiantResonatorTileEntity te = (RadiantResonatorTileEntity) accessor.getTileEntity();
		if (te != null) {
			tooltip.add(String.format(TextFormatting.GOLD + "Progress: %d%%", te.getPercentageComplete()));
			tooltip.add(te.canTick() ? TextFormatting.GREEN + "Resonating" : TextFormatting.RED + "Offline");
		}

		return tooltip;
	}
}
