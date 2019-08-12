package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.AAGuiHandler;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.inventory.handlers.ITroveItemHandler;
import com.aranaira.arcanearchives.inventory.handlers.OptionalUpgradesHandler;
import com.aranaira.arcanearchives.inventory.handlers.SizeUpgradeItemHandler;
import com.aranaira.arcanearchives.inventory.handlers.TroveUpgradeItemHandler;
import com.aranaira.arcanearchives.items.templates.IItemScepter;
import com.aranaira.arcanearchives.tileentities.interfaces.IBrazierRouting;
import com.aranaira.arcanearchives.tileentities.interfaces.IManifestTileEntity;
import com.aranaira.arcanearchives.tileentities.interfaces.IUpgradeableStorage;
import com.aranaira.arcanearchives.types.enums.UpgradeType;
import com.aranaira.arcanearchives.util.ItemUtils;
import com.aranaira.arcanearchives.util.PlayerUtil;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.UUID;

public class RadiantFurnaceTileEntity extends ImmanenceTileEntity implements IUpgradeableStorage {

	public RadiantFurnaceTileEntity(String name) {
		super(name);
	}

	@Override
	public SizeUpgradeItemHandler getSizeUpgradesHandler() {
		return null;
	}

	@Override
	public OptionalUpgradesHandler getOptionalUpgradesHandler() {
		return null;
	}

	@Override
	public int getModifiedCapacity() {
		return 0;
	}
}
