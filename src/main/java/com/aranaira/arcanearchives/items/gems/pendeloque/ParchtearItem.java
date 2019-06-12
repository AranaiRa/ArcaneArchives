package com.aranaira.arcanearchives.items.gems.pendeloque;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.network.NetworkHandler;
import com.aranaira.arcanearchives.network.PacketArcaneGem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.ArrayList;
import java.util.List;

public class ParchtearItem extends ArcaneGemItem {
    public static final String NAME = "item_parchtear";
    public static final int CUBOID_RADIUS = 1;

    public ParchtearItem() {
        super(NAME, GemCut.PENDELOQUE, GemColor.BLACK, 1000, 3375);
    }

    @Override
    public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("arcanearchives.tooltip.gemcharge") + ": " +getTooltipData(stack));
        tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.parchtear"));
    }

    @Override
    public boolean doesSneakBypassUse (ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick (World world, EntityPlayer player, EnumHand hand) {
        if(!world.isRemote) {
            if(GemUtil.getCharge(player.getHeldItem(hand)) > 0) {
                ArrayList<BlockPos> positions = new ArrayList<>();
                Vec3d start = new Vec3d(player.posX, player.posY+player.height, player.posZ);
                Vec3d dir = player.getLookVec();
                Vec3d rayTarget = new Vec3d(start.x + dir.x * 40, start.y + dir.y * 40, start.z + dir.z * 40);

                RayTraceResult ray = world.rayTraceBlocks(start, rayTarget, true, false, false);

                if(ray != null) {
                    BlockPos root = ray.getBlockPos();
                    ArcaneArchives.logger.info("Hit "+world.getBlockState(root).getBlock().getLocalizedName()+" fluid block");

                    if (!player.isSneaking()) {
                        positions = getPositionsInCuboidRadius(root, CUBOID_RADIUS);
                    }
                    else {
                        positions.add(root);
                    }

                    int chargeCost = 0;
                    for(BlockPos pos : positions) {
                        Block block = world.getBlockState(pos).getBlock();
                        if(block instanceof IFluidBlock || block instanceof BlockStaticLiquid || block instanceof BlockDynamicLiquid) {
                            world.setBlockState(pos, Blocks.AIR.getDefaultState());
                            chargeCost++;
                        }
                    }

                    GemUtil.consumeCharge(player.getHeldItemMainhand(), chargeCost);
                }

            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    private ArrayList<BlockPos> getPositionsInCuboidRadius(BlockPos root, int radius) {
        ArrayList<BlockPos> positions = new ArrayList<>();

        for(int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                for (int k = -radius; k <= radius; k++) {
                    positions.add(root.add(i,j,k));
                }
            }
        }

        return positions;
    }
}
