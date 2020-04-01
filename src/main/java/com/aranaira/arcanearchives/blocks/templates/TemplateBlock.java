package com.aranaira.arcanearchives.blocks.templates;

import com.aranaira.arcanearchives.reference.Tags;
import com.aranaira.arcanearchives.tileentities.StoredIdTile;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@SuppressWarnings({"UnusedReturnValue", "WeakerAccess", "NullableProblems", "unchecked", "deprecation", "DeprecatedIsStillUsed"})
public class TemplateBlock extends Block {
  protected ItemBlock itemBlock = null;
  protected String tooltip = null;
  protected String formatting = "";
  protected boolean isOpaqueCube = true;
  protected boolean isFullCube = true;
  protected AxisAlignedBB axis = null;
  protected boolean storesId = false;

  public TemplateBlock(Material materialIn) {
    super(materialIn);
  }

  @Nullable
  public ItemBlock getItemBlock() {
    return itemBlock;
  }

  public TemplateBlock setBoundingBox(AxisAlignedBB bb) {
    this.axis = bb;
    return this;
  }

  public TemplateBlock setFullCube(boolean cube) {
    isFullCube = cube;
    return this;
  }

  public TemplateBlock setOpaqueCube(boolean cube) {
    isOpaqueCube = cube;
    return this;
  }

  // TODO: Oh god, I've turned into elulib
  @Override
  public boolean isFullCube(IBlockState state) {
    return isFullCube;
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return isOpaqueCube;
  }

  public TemplateBlock setItemBlock(ItemBlock itemBlock) {
    this.itemBlock = itemBlock;
    return this;
  }

  public TemplateBlock setTooltip(String text) {
    return setTooltip(text, "");
  }

  public TemplateBlock setTooltip(String text, TextFormatting formatting) {
    return setTooltip(text, "" + formatting);
  }

  public TemplateBlock setTooltip(String text, String formatting) {
    this.tooltip = text;
    this.formatting = formatting;
    return this;
  }

  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);

    if (tooltip != null) {
      tooltip.add("");
      tooltip.add(formatting + I18n.format(this.tooltip));
    }
  }

  @Override
  protected TemplateBlock setSoundType(SoundType sound) {
    return (TemplateBlock) super.setSoundType(sound);
  }

  @Override
  public TemplateBlock setLightOpacity(int opacity) {
    return (TemplateBlock) super.setLightOpacity(opacity);
  }

  @Override
  public TemplateBlock setLightLevel(float value) {
    return (TemplateBlock) super.setLightLevel(value);
  }

  @Override
  public TemplateBlock setResistance(float resistance) {
    return (TemplateBlock) super.setResistance(resistance);
  }

  @Override
  public TemplateBlock setHardness(float hardness) {
    return (TemplateBlock) super.setHardness(hardness);
  }

  @Override
  public TemplateBlock setBlockUnbreakable() {
    return (TemplateBlock) super.setBlockUnbreakable();
  }

  @Override
  public TemplateBlock setTickRandomly(boolean shouldTick) {
    return (TemplateBlock) super.setTickRandomly(shouldTick);
  }

  @Override
  public TemplateBlock setTranslationKey(String key) {
    return (TemplateBlock) super.setTranslationKey(key);
  }

  @Override
  public TemplateBlock setCreativeTab(CreativeTabs tab) {
    return (TemplateBlock) super.setCreativeTab(tab);
  }

  public TemplateBlock setDefault(IBlockState state) {
    setDefaultState(state);
    return this;
  }

  public TemplateBlock setSlipperiness(float s) {
    this.setDefaultSlipperiness(s);
    return this;
  }

  @Override
  @Deprecated
  public void setDefaultSlipperiness(float slipperiness) {
    super.setDefaultSlipperiness(slipperiness);
  }

  public TemplateBlock setHarvestTool(String toolClass, int level) {
    setHarvestLevel(toolClass, level);
    return this;
  }

  public TemplateBlock setHarvestTool(String toolClass, int level, IBlockState state) {
    setHarvestLevel(toolClass, level, state);
    return this;
  }

  @Override
  @Deprecated
  public void setHarvestLevel(String toolClass, int level) {
    super.setHarvestLevel(toolClass, level);
  }

  @Override
  @Deprecated
  public void setHarvestLevel(String toolClass, int level, IBlockState state) {
    super.setHarvestLevel(toolClass, level, state);
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    if (axis != null) {
      return axis;
    }

    return super.getBoundingBox(state, source, pos);
  }

  @Override
  public boolean canSilkHarvest() {
    return true;
  }

  protected List<ItemStack> generateItemDrops(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack harvestTool) {
    return Collections.emptyList();
  }

  protected List<ItemStack> generateSilkTouchDrops(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack harvestTool) {
    return Collections.emptyList();
  }

  @Override
  public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
    if (!storesId) {
      super.harvestBlock(worldIn, player, pos, state, te, stack);
    } else {
      player.addStat(Objects.requireNonNull(StatList.getBlockStats(this)));
      player.addExhaustion(0.005F);

      if (worldIn.isRemote) {
        return;
      }

      harvesters.set(player);
      List<ItemStack> items = new java.util.ArrayList<>(generateItemDrops(worldIn, player, pos, state, te, stack));
      net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0f, true, player);
      for (ItemStack item : items) {
        spawnAsEntity(worldIn, pos, item);
      }
      harvesters.set(null);
    }
  }

  @Override
  public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
    if (!storesId) {
      super.getDrops(drops, world, pos, state, fortune);
    }
  }

  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    if (storesId) {
      return Items.AIR;
    }

    return super.getItemDropped(state, rand, fortune);
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {
    if (storesId) {
      return true;
    }

    return super.hasTileEntity(state);
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    if (storesId) {
      return new StoredIdTile();
    }

    return super.createTileEntity(world, state);
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    super.onBlockPlacedBy(world, pos, state, placer, stack);

    if (stack.hasTagCompound()) {
      NBTTagCompound tag = stack.getTagCompound();

      StoredIdTile te = WorldUtil.getTileEntity(StoredIdTile.class, world, pos);
      if (te != null && tag != null && tag.hasUniqueId(Tags.networkId)) {
        te.setNetworkId(tag.getUniqueId(Tags.networkId));
        te.markDirty();
      }
    }
  }
}


