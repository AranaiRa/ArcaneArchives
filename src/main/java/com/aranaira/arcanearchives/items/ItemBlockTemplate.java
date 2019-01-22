package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.BlockTemplate;
import com.aranaira.arcanearchives.data.ArcaneArchivesNetwork;
import com.aranaira.arcanearchives.tileentities.AATileEntity;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.util.NetworkHelper;
import com.aranaira.arcanearchives.util.Placeable;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemBlockTemplate extends ItemBlock {
    private BlockTemplate blockTemplate;

    public ItemBlockTemplate (@Nonnull BlockTemplate block) {
        super(block);

        this.blockTemplate = block;

        assert block.getRegistryName() != null;

        setRegistryName(block.getRegistryName());
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (player instanceof FakePlayer) {
            // Deal with this someway
        }

        ArcaneArchivesNetwork network = NetworkHelper.getArcaneArchivesNetwork(player.getUniqueID());

        int placeLimit = blockTemplate.getPlaceLimit();

        if (placeLimit != -1)  {
            Class c = blockTemplate.getEntityClass();
            if (c == null) {
                player.sendMessage(new TextComponentTranslation("arcanearchives.error.invaliditemblock"));
                return EnumActionResult.FAIL;
            }

            int count = network.CountTileEntities(blockTemplate.getEntityClass());

            if (count > placeLimit || count == 1 && placeLimit == 1) {
                player.sendStatusMessage(new TextComponentTranslation("arcanearchives.error.toomanyplaced", blockTemplate.getPlaceLimit(), blockTemplate.getNameComponent()), true);
                return EnumActionResult.FAIL;
            }

            if (blockTemplate.hasAccessors()) {
                if (!Placeable.CanPlaceSize(world, pos.up(), blockTemplate.getSize(), facing)) {
                    player.sendStatusMessage(new TextComponentTranslation("arcanearchives.error.notenoughspace", blockTemplate.getNameComponent()), true);
                    return EnumActionResult.FAIL;
                }
            }
        }

        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        boolean res = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);

        if (stack.getItem() instanceof ItemBlockTemplate & !world.isRemote)
        {
            TileEntity te = world.getTileEntity(pos);
            BlockTemplate block = (BlockTemplate) ((ItemBlockTemplate) stack.getItem()).getBlock();
            ArcaneArchivesNetwork network = NetworkHelper.getArcaneArchivesNetwork(player.getUniqueID());

            if(te instanceof AATileEntity)
            {
                if(player instanceof FakePlayer)
                {
                    ArcaneArchives.logger.error(String.format("TileEntity placed by FakePlayer at %d,%d,%d is invalid and not linked to the network.", pos.getX(), pos.getY(), pos.getZ()));
                } else
                {
                    // If it's a network tile entity
                    if(te instanceof ImmanenceTileEntity)
                    {
                        ImmanenceTileEntity ite = (ImmanenceTileEntity) te;

                        UUID newId = player.getUniqueID();
                        ite.SetNetworkID(newId);
                        ite.Dimension = player.dimension;

                        // Any custom handling of name (like the matrix core) should be done here
                        network.AddTileToNetwork(ite);
                    }

                    // Store its size
                    AATileEntity ate = (AATileEntity) te;
                    ate.setSize(block.getSize());
                }

                if(block.hasAccessors())
                {
                    Placeable.ReplaceBlocks(world, pos, block.getSize(), ((AATileEntity) te).getFacing(world));
                }
            }
        }

        return res;
    }
}
