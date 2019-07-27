package com.aranaira.arcanearchives.integration.baubles;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.api.cap.BaubleItem;
import baubles.api.cap.BaublesCapabilities;
import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BaubleBodyCapabilityHandler implements ICapabilityProvider {
	public static final BaubleBodyCapabilityHandler instance = new BaubleBodyCapabilityHandler();
	public static final ResourceLocation NAME = new ResourceLocation(ArcaneArchives.MODID, "gem_socket_body_capability");

	public static IBauble bauble = new BaubleItem(BaubleType.BODY);

	@Override
	public boolean hasCapability (@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == BaublesCapabilities.CAPABILITY_ITEM_BAUBLE;
	}

	@Nullable
	@Override
	public <T> T getCapability (@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == BaublesCapabilities.CAPABILITY_ITEM_BAUBLE) {
			return BaublesCapabilities.CAPABILITY_ITEM_BAUBLE.cast(bauble);
		}

		return null;
	}
}
