package com.aranaira.arcanearchives.items.gems;

import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem.*;
import com.aranaira.arcanearchives.util.NBTUtils;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class GemRechargePowder extends ItemTemplate {
    public static final String NAME = "chromatic_powder";

    public GemRechargePowder() {
        super(NAME);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        String key = "arcanearchives.tooltip."+getTranslationKey(stack);
        tooltip.add(I18n.format(key));
        tooltip.add(TextFormatting.RED + I18n.format("arcanearchives.tooltip.creativeonly"));
    }

    @Override
    public void registerModels () {
        ModelResourceLocation r = getColorizedPowderResourceLocation(GemColor.RED);
        ModelResourceLocation o = getColorizedPowderResourceLocation(GemColor.ORANGE);
        ModelResourceLocation y = getColorizedPowderResourceLocation(GemColor.YELLOW);
        ModelResourceLocation g = getColorizedPowderResourceLocation(GemColor.GREEN);
        ModelResourceLocation c = getColorizedPowderResourceLocation(GemColor.CYAN);
        ModelResourceLocation b = getColorizedPowderResourceLocation(GemColor.BLUE);
        ModelResourceLocation v = getColorizedPowderResourceLocation(GemColor.PURPLE);
        ModelResourceLocation p = getColorizedPowderResourceLocation(GemColor.PINK);
        ModelResourceLocation w = getColorizedPowderResourceLocation(GemColor.WHITE);
        ModelResourceLocation k = getColorizedPowderResourceLocation(GemColor.BLACK);

        ModelBakery.registerItemVariants(this, w, r, o, y, g, c, b, v, p, k);

        ModelLoader.setCustomMeshDefinition(this, stack -> {
            GemColor color = getColor(stack);
            switch(color){
                case RED: return r;
                case ORANGE: return o;
                case YELLOW: return y;
                case GREEN: return g;
                case CYAN: return c;
                case BLUE: return b;
                case PURPLE: return v;
                case PINK: return p;
                case BLACK: return k;
                default: return w;
            }
        });
    }

    public static GemColor getColor(ItemStack stack) {
        NBTTagCompound tag = NBTUtils.getOrCreateTagCompound(stack);
        if(tag.hasKey("color")) return GemColor.fromOrdinal(tag.getInteger("color"));
        return GemColor.NOCOLOR;
    }

    protected ModelResourceLocation getColorizedPowderResourceLocation (GemColor color) {
        String loc = "arcanearchives:gems/powder/";
        loc += color.toString().toLowerCase();
        return new ModelResourceLocation(loc, "inventory");
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        switch(getColor(stack)) {
            case RED: return "item.chromatic_powder.red";
            case ORANGE: return "item.chromatic_powder.orange";
            case YELLOW: return "item.chromatic_powder.yellow";
            case GREEN: return "item.chromatic_powder.green";
            case CYAN: return "item.chromatic_powder.cyan";
            case BLUE: return "item.chromatic_powder.blue";
            case PURPLE: return "item.chromatic_powder.purple";
            case PINK: return "item.chromatic_powder.pink";
            case BLACK: return "item.chromatic_powder.black";
            case WHITE: return "item.chromatic_powder.white";
        }
        return super.getTranslationKey(stack);
    }

    @Override
    public void getSubItems (CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            for(GemColor c : GemColor.values()) {
                if(c == GemColor.NOCOLOR) continue;
                ItemStack powder = new ItemStack(this);
                NBTTagCompound tag = NBTUtils.getOrCreateTagCompound(powder);
                tag.setInteger("color", c.ordinal());
                subItems.add(powder);
            }
        }
    }
}
