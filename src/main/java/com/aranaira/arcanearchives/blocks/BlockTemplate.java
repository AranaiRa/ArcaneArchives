package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.ArcaneArchivesNetwork;
import com.aranaira.arcanearchives.init.BlockLibrary;
import com.aranaira.arcanearchives.init.ItemLibrary;
import com.aranaira.arcanearchives.items.ItemBlockTemplate;
import com.aranaira.arcanearchives.tileentities.AATileEntity;
import com.aranaira.arcanearchives.tileentities.AccessorTileEntity;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.util.IHasModel;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.util.types.Size;
import com.google.common.collect.Lists;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.UUID;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockTemplate extends Block implements IHasModel
{
	private int placeLimit = -1;
	public Size size;
	private Class<? extends AATileEntity> entityClass;

	public BlockTemplate(String name, Material materialIn)
	{
		super(materialIn);
		setTranslationKey(name);
		setRegistryName(new ResourceLocation(ArcaneArchives.MODID, name));
		setCreativeTab(ArcaneArchives.TAB);
		BlockLibrary.BLOCKS.add(this);
		ItemLibrary.ITEMS.add(new ItemBlockTemplate(this));
		setHarvestLevel("pickaxe", 0);
	}

	public static BlockTemplate getByType (int type) {
		return BlockLibrary.BLOCK_BIMAP.get(type);
	}

	public static int getByBlock (BlockTemplate block) {
		return BlockLibrary.BLOCK_BIMAP.inverse().get(block);
	}

	public int getAccessorId () {
		return BlockLibrary.BLOCK_BIMAP.inverse().get(this);
	}

	public ITextComponent getNameComponent()
	{
		return new TextComponentTranslation(String.format("%s.name", getTranslationKey()));
	}

	public Class<? extends AATileEntity> getEntityClass()
	{
		return this.entityClass;
	}

	void setEntityClass(Class<? extends AATileEntity> clazz)
	{
		this.entityClass = clazz;
	}

	public int getPlaceLimit()
	{
		return placeLimit;
	}

	void setPlaceLimit(int newPlaceLimit)
	{
		placeLimit = newPlaceLimit;
	}

	public Size getSize()
	{
		return size;
	}

	public void setSize(Size newSize)
	{
		size = newSize;
	}

	void setSize(int w, int h, int l)
	{
		size = new Size(w, h, l);
	}

	public boolean hasAccessors()
	{
		return size != null && size.hasAccessors();
	}

	public boolean hasOBJModel()
	{
		return false;
	}

	@Nonnull
	public EnumFacing getFacing(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock() instanceof BlockDirectionalTemplate)
		{
			return state.getValue(BlockDirectionalTemplate.FACING);
		} else
		{
			return EnumFacing.WEST;
		}
	}

	@Override
	public void registerModels()
	{
		ArcaneArchives.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}

	public List<BlockPos> calculateAccessors(World world, BlockPos pos) {
		return calculateAccessors(world, pos, null);
	}

	// This always includes the parent location
	public List<BlockPos> calculateAccessors(World world, BlockPos pos, @Nullable EnumFacing facing)
	{
		Size size = getSize();

		if (facing == null) facing = getFacing(world, pos);

		EnumFacing curOffset = EnumFacing.fromAngle(facing.getHorizontalAngle() - ((size.width == size.length) ? 90 : 180));

		BlockPos start = pos;
		BlockPos stop;

		if(size.width == 2)
		{
			stop = pos.offset(curOffset, 1);
		}
		else
		{
			int steps = (size.width - 1) / 2;

			stop = pos.offset(curOffset, steps);
			start = pos.offset(curOffset.getOpposite(), steps);
		}

		if (size.length == 2)
		{
			stop = stop.offset(facing, 1);
		}
		else
		{
			int steps = (size.length - 1) / 2;

			stop = stop.offset(facing, steps);
			start = start.offset(facing.getOpposite(), steps);
		}

		// for height, we move up the required number of steps;
		for(int i = 1; i < size.height; i++)
		{
			stop = stop.up();
		}

		List<BlockPos> output = Lists.newArrayList(BlockPos.getAllInBox(start, stop));
		output.removeIf((f) -> f.equals(pos));

		return output;
	}

	@Override
	public void onBlockPlacedBy(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityLivingBase placer, @Nonnull ItemStack stack)
	{
		super.onBlockPlacedBy(world, pos, state, placer, stack);

		if (!world.isRemote)
		{
			TileEntity te = world.getTileEntity(pos);
			ArcaneArchivesNetwork network = NetworkHelper.getArcaneArchivesNetwork(placer.getUniqueID());

			if(te instanceof AATileEntity)
			{
				if(placer instanceof FakePlayer)
				{
					ArcaneArchives.logger.error(String.format("TileEntity placed by FakePlayer at %d,%d,%d is invalid and not linked to the network.", pos.getX(), pos.getY(), pos.getZ()));
				} else
				{
					// If it's a network tile entity
					if(te instanceof ImmanenceTileEntity)
					{
						ImmanenceTileEntity ite = (ImmanenceTileEntity) te;

						UUID newId = placer.getUniqueID();
						ite.SetNetworkID(newId);
						ite.Dimension = placer.dimension;

						// Any custom handling of name (like the matrix core) should be done here
						network.AddTileToNetwork(ite);
					}

					// Store its size
					AATileEntity ate = (AATileEntity) te;
					ate.setSize(this.getSize());
				}
			}

			// The item block has already taken care of to make sure that the points can be replaced.
			if(this.hasAccessors())
			{
				for(BlockPos point : calculateAccessors(world, pos))
				{
					world.setBlockState(point, BlockLibrary.ACCESSOR.fromBlock(this));
					TileEntity ate = world.getTileEntity(point);
					if(ate != null)
					{
						((AccessorTileEntity) ate).setParent(pos);
					} else
					{
						ArcaneArchives.logger.info("TileEntity not created yet :(");
					}
				}
			}
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		if (hasAccessors() && !world.isRemote)
		{
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof AATileEntity)
			{
				((AATileEntity) te).breakBlock(state, false);
			}
		}
		super.breakBlock(world, pos, state);
	}


	@Override
	protected BlockStateContainer createBlockState()
	{
		if(hasTileEntity(getDefaultState()))
		{
			return new ExtendedBlockState(this, new IProperty[]{}, new IUnlistedProperty[]{Properties.AnimationProperty});
		}

		return new ExtendedBlockState(this, new IProperty[]{}, new IUnlistedProperty[]{});
	}
}


