package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.AAGuiHandler;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.inventory.handlers.GemSocketHandler;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.items.gems.GemUtil;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class GemSocket extends ItemTemplate {
	public static final String NAME = "gemsocket";

	public GemSocket () {
		super(NAME);
		setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.bauble.gemsocket"));
		if (stack.hasTagCompound()) {
			if (stack.getTagCompound().hasKey("gem")) {
				ItemStack containedStack = GemSocketHandler.getHandler(stack).getGem();
				if (containedStack.getItem() instanceof ArcaneGemItem) {
					String name = containedStack.getItem().getItemStackDisplayName(containedStack);
					int chargeCur = GemUtil.getTooltipCharge(containedStack);
					int chargeMax = GemUtil.getTooltipMaxCharge(containedStack);
					tooltip.add(I18n.format("arcanearchives.tooltip.bauble.gemsocket.contains") + " " + name + " [" + chargeCur + "/" + chargeMax + "]");
				}
			}
		}
	}

	@Override
	@Nonnull
	public ActionResult<ItemStack> onItemRightClick (World world, EntityPlayer player, @Nonnull EnumHand hand) {
		if (world.isRemote) {
			return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
		}
		player.openGui(ArcaneArchives.instance, AAGuiHandler.BAUBLE_GEMSOCKET, world, (int) player.posX, (int) player.posY, (int) player.posZ);
		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
}
