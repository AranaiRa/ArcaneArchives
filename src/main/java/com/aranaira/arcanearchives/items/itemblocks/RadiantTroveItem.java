/*package com.aranaira.arcanearchives.items.itemblocks;

import com.aranaira.arcanearchives.inventory.handlers.OptionalUpgradesHandler;
import com.aranaira.arcanearchives.inventory.handlers.TroveItemBlockItemHandler;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity.TroveItemHandler.Tags;
import com.aranaira.arcanearchives.types.UpgradeType;
import com.aranaira.arcanearchives.util.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class RadiantTroveItem extends BlockItem {
	public RadiantTroveItem (Block block) {
		super(block);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities (ItemStack stack, @Nullable CompoundNBT nbt) {
		return new TroveItemBlockItemHandler(stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		CompoundNBT tag = ItemUtils.getOrCreateTagCompound(stack);
		if (tag.hasKey(RadiantTroveTileEntity.Tags.HANDLER_ITEM)) {
			CompoundNBT incoming = tag.getCompoundTag(RadiantTroveTileEntity.Tags.HANDLER_ITEM);
			int count = incoming.getInteger(Tags.COUNT);
			ItemStack stored = new ItemStack(incoming.getCompoundTag(Tags.REFERENCE));
			int maxCapacity = (incoming.getInteger(Tags.UPGRADES) + 1) * RadiantTroveTileEntity.BASE_COUNT;
			String displayName = stored.getDisplayName();
			if (stored.hasDisplayName()) {
				displayName = TextFormatting.ITALIC + displayName;
			}
			displayName = stored.getItem().getForgeRarity(stored).getColor() + displayName + TextFormatting.RESET;
			tooltip.add(I18n.format("arcanearchives.tooltip.trove.items", displayName));
			tooltip.add(I18n.format("arcanearchives.tooltip.trove.contains", count, maxCapacity));
			OptionalUpgradesHandler optionals = new OptionalUpgradesHandler();
			optionals.deserializeNBT(tag.getCompoundTag(RadiantTroveTileEntity.Tags.OPTIONAL_UPGRADES));
			if (optionals.hasUpgrade(UpgradeType.VOID)) {
				tooltip.add(TextFormatting.DARK_PURPLE + "" + TextFormatting.BOLD + I18n.format("arcanearchives.tooltip.trove.voiding"));
			}
		}

		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}*/
