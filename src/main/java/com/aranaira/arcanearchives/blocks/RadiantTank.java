package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.BlockDirectionalTemplate;
import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.events.LineHandler;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.inventory.handlers.TroveItemHandler;
import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class RadiantTank extends BlockTemplate
{
	public static final String NAME = "radiant_tank";

	public RadiantTank()
	{
		super(NAME, Material.GLASS);
		setSize(1, 1, 1);
		setLightLevel(16 / 16f);
		setHardness(1.7f);
		setHarvestLevel("pickaxe", 0);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		LineHandler.removeLine(pos);

		ItemStack heldItem = player.getHeldItem(hand);

		RadiantTankTileEntity te = WorldUtil.getTileEntity(RadiantTankTileEntity.class, world, pos);
		if (te == null) {
			return !(heldItem.getItem() instanceof ItemBlock);
		}

		IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
		FluidUtil.interactWithFluidHandler(player, hand, handler);

		// I saw Darkhax do this!
		return !(heldItem.getItem() instanceof ItemBlock);
	}

	public static ItemStack generateStack (IBlockState state, World world, BlockPos pos, EntityPlayer player) {
		ItemStack stack = new ItemStack(BlockRegistry.RADIANT_TANK);

		RadiantTankTileEntity te = WorldUtil.getTileEntity(RadiantTankTileEntity.class, world, pos);
		if (te == null) {
			return stack;
		}

		NBTTagCompound tag = stack.getTagCompound();
		if (tag == null) {
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		}

		tag.setTag("tank", te.serializeStack());

		return stack;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return generateStack(state, world, pos, player);
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		ItemStack stack = generateStack(state, world, pos, player);
		EntityItem tank = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
		world.spawnEntity(tank);

		return world.setBlockToAir(pos);
	}

	@Override
	public void onBlockPlacedBy(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityLivingBase placer, @Nonnull ItemStack stack)
	{
		super.onBlockPlacedBy(world, pos, state, placer, stack);

		if (stack.hasTagCompound()) {
			RadiantTankTileEntity te = WorldUtil.getTileEntity(RadiantTankTileEntity.class, world, pos);
			if (te != null) {
				te.deserializeStack(stack.getTagCompound().getCompoundTag("tank"));
			}
		}
	}

	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion)
	{
		ItemStack stack = generateStack(null, world, pos, null);
		EntityItem tank = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
		world.spawnEntity(tank);

		super.onBlockExploded(world, pos, explosion);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean causesSuffocation(IBlockState state)
	{
		return false;
	}

	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new RadiantTankTileEntity();
	}

	@Override
	@ParametersAreNonnullByDefault
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		LineHandler.removeLine(pos);

		super.breakBlock(world, pos, state);
	}
}
