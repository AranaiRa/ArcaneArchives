package com.aranaira.arcanearchives.integration.hwyla.providers;

import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity.TroveItemHandler;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class ProviderRadiantTrove implements IWailaDataProvider {
	@Override
	public List<String> getWailaBody (ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		RadiantTroveTileEntity te = (RadiantTroveTileEntity) accessor.getTileEntity();
		if (te != null) {
			TroveItemHandler handler = te.getInventory();
			if (handler.getCount() != 0) {
				tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.data.tooltip.trove_count_waila", handler.getCount(), handler.getItem().getDisplayName()));
			} else {
				tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.data.tooltip.empty"));
			}
		}

		return tooltip;
	}
}
