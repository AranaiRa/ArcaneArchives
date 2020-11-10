package com.aranaira.arcanearchives.blocks.templates;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings({"UnusedReturnValue", "WeakerAccess", "NullableProblems", "unchecked", "deprecation", "DeprecatedIsStillUsed"})
public class TemplateBlock extends Block {
  protected String tooltip = null;
  protected Consumer<IFormattableTextComponent> formatting = null;
  /*  protected boolean isOpaqueCube = true;
    protected boolean isFullCube = true;*/
  protected AxisAlignedBB axis = null;
  protected boolean storesId = false;

  public TemplateBlock(Block.Properties properties) {
    super(properties);
  }

  public TemplateBlock setBoundingBox(AxisAlignedBB bb) {
    this.axis = bb;
    return this;
  }

/*  public TemplateBlock setFullCube(boolean cube) {
    isFullCube = cube;
    return this;
  }

  public TemplateBlock setOpaqueCube(boolean cube) {
    isOpaqueCube = cube;
    return this;
  }*/

/*  @Override
  public boolean isFullCube(BlockState state) {
    return isFullCube;
  }

  @Override
  public boolean isOpaqueCube(BlockState state) {
    return isOpaqueCube;
  }*/

  public TemplateBlock setTooltip(String text) {
    return setTooltip(text, null);
  }

  public TemplateBlock setTooltip(String text, Consumer<IFormattableTextComponent> consumer) {
    this.tooltip = text;
    this.formatting = consumer;
    return this;
  }

  public TemplateBlock storesId() {
    this.storesId = true;
    return this;
  }

  @Override
  public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);

    if (tooltip != null) {
      tooltip.add(new StringTextComponent(""));
      IFormattableTextComponent component = new TranslationTextComponent(this.tooltip);
      if (formatting != null) {
        formatting.accept(component);
      }
      tooltip.add(component);
    }
  }

/*  @Override
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
  }*/

/*  @Override
  public TemplateBlock setResistance(float resistance) {
    return (TemplateBlock) super.setResistance(resistance);
  }

  @Override
  public TemplateBlock setHardness(float hardness) {
    return (TemplateBlock) super.setHardness(hardness);
  }*/

/*  @Override
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
  public TemplateBlock setCreativeTab(ItemGroup tab) {
    return (TemplateBlock) super.setCreativeTab(tab);
  }*/

  public TemplateBlock setDefault(BlockState state) {
    setDefaultState(state);
    return this;
  }

/*  public TemplateBlock setSlipperiness(float s) {
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

  public TemplateBlock setHarvestTool(String toolClass, int level, BlockState state) {
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
  public void setHarvestLevel(String toolClass, int level, BlockState state) {
    super.setHarvestLevel(toolClass, level, state);
  }*/

/*  @Override
  public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
    if (axis != null) {
      return axis;
    }

    return super.getBoundingBox(state, source, pos);
  }

  @Override
  public boolean canSilkHarvest() {
    return true;
  }*/

/*  protected List<ItemStack> generateItemDrops(World world, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te, ItemStack harvestTool) {
    return Collections.emptyList();
  }

  protected List<ItemStack> generateSilkTouchDrops(World world, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te, ItemStack harvestTool) {
    return Collections.emptyList();
  }

  @Override
  public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
    if (!storesId) {
      super.harvestBlock(worldIn, player, pos, state, te, stack);
    } else {
      player.addStat(Objects.requireNonNull(Stats.getBlockStats(this)));
      player.addExhaustion(0.005F);

      if (worldIn.isRemote) {
        return;
      }

      harvesters.set(player);
      List<ItemStack> items = new java.util.ArrayList<>(generateItemDrops(worldIn, player, pos, state, te, stack));
      items.add(generateStack(te, worldIn, pos));
      net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0f, true, player);
      for (ItemStack item : items) {
        spawnAsEntity(worldIn, pos, item);
      }
      harvesters.set(null);
    }
  }*/

/*  public ItemStack generateStack(TileEntity te, IReadAccess world, BlockPos pos) {
    if (te instanceof NetworkedBaseTile) {
      return generateStack((NetworkedBaseTile) te, world, pos);
    }

    return new ItemStack(getItemBlock());
  }

  public ItemStack generateStack(NetworkedBaseTile te, IBlockAccess world, BlockPos pos) {
    ItemStack stack = new ItemStack(getItemBlock());

    UUID networkId = te.getNetworkId();
    if (networkId != null) {
      NetworkItemUtil.setNetworkId(stack, te.getNetworkId());
    }
    return stack;
  }

  @Override
  public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull BlockState state, int fortune) {
    if (!storesId) {
      super.getDrops(drops, world, pos, state, fortune);
    } else {
      drops.add(generateStack(world.getTileEntity(pos), world, pos));
    }
  }

  @Override
  public Item getItemDropped(BlockState state, Random rand, int fortune) {
    if (storesId) {
      return Items.AIR;
    }

    return super.getItemDropped(state, rand, fortune);
  }*/

  @Override
  public boolean hasTileEntity(BlockState state) {
    if (storesId) {
      return true;
    }

    return super.hasTileEntity(state);
  }

/*  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader reader) {
    if (storesId) {
      return new StoredIdTile();
    }

    return super.createTileEntity(state, reader);
  }*/

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    super.onBlockPlacedBy(world, pos, state, placer, stack);

/*    if (stack.hasTag()) {
      UUID id = NetworkItemUtil.getNetworkId(stack);

      NetworkedBaseTile te = WorldUtil.getTileEntity(NetworkedBaseTile.class, world, pos);
      if (id != null && te != null) {
        te.setNetworkId(id);
        if (!world.isRemote) {
          te.markDirty();
          te.stateUpdate();
        }
      }
    }*/
  }
}


