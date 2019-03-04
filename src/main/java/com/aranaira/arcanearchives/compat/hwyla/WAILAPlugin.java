package com.aranaira.arcanearchives.compat.hwyla;

import com.aranaira.arcanearchives.compat.hwyla.providers.ProviderRadiantChest;
import com.aranaira.arcanearchives.compat.hwyla.providers.ProviderResonator;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantResonatorTileEntity;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
@SuppressWarnings("unused")
public class WAILAPlugin implements IWailaPlugin
{
	@SuppressWarnings("unused")
	public WAILAPlugin()
	{
	}

	@Override
	public void register(IWailaRegistrar registrar)
	{
		registrar.registerBodyProvider(new ProviderResonator(), RadiantResonatorTileEntity.class);
		registrar.registerBodyProvider(new ProviderRadiantChest(), RadiantChestTileEntity.class);
	}
}
