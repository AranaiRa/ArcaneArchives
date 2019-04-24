package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.AAGuiHandler;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.client.Keybinds;
import com.aranaira.arcanearchives.data.ClientNetwork;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.events.LineHandler;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ManifestItem extends ItemTemplate
{
	public static final String NAME = "item_manifest";

	public ManifestItem() {
		super(NAME);
		setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if(!worldIn.isRemote) return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));

		openManifest(worldIn, playerIn);

		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	public static void openManifest(World worldIn, EntityPlayer playerIn) {
		if(playerIn.isSneaking()) {
			LineHandler.clearChests(playerIn.dimension);
		} else {
			ClientNetwork network = NetworkHelper.getClientNetwork(playerIn.getUniqueID());
			network.manifestItems.clear();
			network.synchroniseManifest();

			playerIn.openGui(ArcaneArchives.instance, AAGuiHandler.MANIFEST, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
		}
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		String additional = "";
		if (Keybinds.manifestKey.getKeyCode() != 0) {
			additional = " or " + Keybinds.manifestKey.getDisplayName();
		}
		tooltip.add(TextFormatting.GOLD + "" + TextFormatting.BOLD + "Right-Click" + additional + TextFormatting.RESET + TextFormatting.GOLD + " to open the manifest.");
		if (Keybinds.manifestKey.getKeyCode() != 0) {
			additional = " or Sneak-" + Keybinds.manifestKey.getDisplayName();
		}
		tooltip.add(TextFormatting.GOLD + "" + TextFormatting.BOLD + "Sneak-Right-Click" + additional + TextFormatting.RESET + TextFormatting.GOLD + " to clear inventory tracking.");
		if (Keybinds.manifestKey.getKeyCode() == 0) {
		}
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.RARE;
	}
}
