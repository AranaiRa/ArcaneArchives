package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.common.AAGuiHandler;
import com.aranaira.arcanearchives.data.ArcaneArchivesClientNetwork;
import com.aranaira.arcanearchives.data.NetworkHelper;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ManifestItem extends ItemTemplate
{
	public static final String NAME = "item_manifest";

	public ManifestItem()
	{
		super(NAME);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if (!worldIn.isRemote) return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));

		ArcaneArchivesClientNetwork network = NetworkHelper.getArcaneArchivesClientNetwork(playerIn);
		network.synchroniseManifest();

		playerIn.openGui(ArcaneArchives.instance, AAGuiHandler.MANIFEST, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);

        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
}
