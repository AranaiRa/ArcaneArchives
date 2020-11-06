package com.aranaira.arcanearchives.items.itemblocks;

import com.aranaira.arcanearchives.blocks.templates.TemplateBlock;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class MonitoringCrystalItem extends ItemBlockTemplate {
	public MonitoringCrystalItem (@Nonnull TemplateBlock block) {
		super(block);
	}

	@Override
	public ActionResultType onItemUseFirst (PlayerEntity player, World world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ, Hand hand) {
		ImmanenceTileEntity ite = WorldUtil.getTileEntity(ImmanenceTileEntity.class, world, pos);
		if (ite != null) {
			ITextComponent message = new TranslationTextComponent("arcanearchives.message.invalid_crystal_target").setStyle(new Style().setColor(TextFormatting.GOLD).setBold(true));
			player.sendStatusMessage(message, true);
			return ActionResultType.FAIL;
		}

		return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
	}

	@Override
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + "Placed on inventories to relay their contents to a manifest.");
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
