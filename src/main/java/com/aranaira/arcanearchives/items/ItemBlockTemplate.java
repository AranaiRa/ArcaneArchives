package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.blocks.BlockTemplate;
import com.aranaira.arcanearchives.blocks.MatrixCrystalCore;
import com.aranaira.arcanearchives.blocks.RadiantResonator;
import com.aranaira.arcanearchives.data.ArcaneArchivesNetwork;
import com.aranaira.arcanearchives.tileentities.MatrixCoreTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantResonatorTileEntity;
import com.aranaira.arcanearchives.util.NetworkHelper;
import com.aranaira.arcanearchives.util.Placeable;
import com.aranaira.arcanearchives.util.TileHelper;
import com.aranaira.arcanearchives.util.handlers.ConfigHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ItemBlockTemplate extends ItemBlock {
    public ItemBlockTemplate (@Nonnull BlockTemplate block) {
        super(block);

        assert block.getRegistryName() != null;

        setRegistryName(block.getRegistryName());
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (player instanceof FakePlayer) {
            // Deal with this someway
        }

        ArcaneArchivesNetwork network = NetworkHelper.getArcaneArchivesNetwork(player.getUniqueID());

        if (block instanceof RadiantResonator) {
            if (network.CountTileEntities(RadiantResonatorTileEntity.class) >= ConfigHandler.values.iRadiantResonatorLimit) {
                player.sendStatusMessage(new TextComponentTranslation("arcanearchives.error.toomanyplaced.resonator"), true);
            return EnumActionResult.FAIL;
            }
        }

        if (block instanceof MatrixCrystalCore) {
            // PlaceLimit is defined as 1
            if (network.CountTileEntities(MatrixCoreTileEntity.class) == 1) {
                player.sendStatusMessage(new TextComponentTranslation("arcanearchives.error.toomanyplaced.core"), true);
                return EnumActionResult.FAIL;
            } else {
                // We right clicked on teh block beneath where the core will go
                if (!Placeable.CanPlaceSize(world, pos.up(), MatrixCrystalCore.getSize(), facing)) {
                    player.sendStatusMessage(new TextComponentTranslation("arcanearchives.error.notenoughspace.core"), true);
                    return EnumActionResult.FAIL;
                }
            }
        }

        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        boolean res = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);

        float yaw = player.rotationYaw;
        EnumFacing direction = EnumFacing.fromAngle(yaw);

        if (player instanceof FakePlayer) {
            // We need to do something to handle this at some point. Perhaps just a warning?
            // i.e., using a mechanical activator to place the block means it will have no
            // network information.
        }

        if (!world.isRemote) return res;

        if (block.hasTileEntity(newState)) {
            TileHelper.markPosition(player.getUniqueID(), pos);
        }

        if (block instanceof MatrixCrystalCore)
        {
            Placeable.ReplaceBlocks(world, pos, MatrixCrystalCore.getSize(), direction);
        }

        return res;
    }
}
