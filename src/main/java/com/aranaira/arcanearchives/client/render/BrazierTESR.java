package com.aranaira.arcanearchives.client.render;

import com.aranaira.arcanearchives.tileentities.BrazierTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.*;
import net.minecraftforge.client.model.animation.FastTESR;

import java.nio.ByteBuffer;

public class BrazierTESR extends FastTESR<BrazierTileEntity> {
    
    @Override
    public void renderTileEntityFast(BrazierTileEntity brazierTileEntity, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buff) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        TextureAtlasSprite sprite = mc.getTextureMapBlocks().getTextureExtry("minecraft:blocks/fire_layer_0");
        
        lightEnd(color(buff.pos(x + 0.50, y + 1.25, z + 0.25)).tex(sprite.getMinU(), sprite.getMinV()));
        lightEnd(color(buff.pos(x + 0.50, y + 1.25, z + 0.75)).tex(sprite.getMaxU(), sprite.getMinV()));
        lightEnd(color(buff.pos(x + 0.25, y + 0.75, z + 0.75)).tex(sprite.getMaxU(), sprite.getMaxV()));
        lightEnd(color(buff.pos(x + 0.25, y + 0.75, z + 0.25)).tex(sprite.getMinU(), sprite.getMaxV()));
        
        lightEnd(color(buff.pos(x + 0.50, y + 1.25, z + 0.25)).tex(sprite.getMinU(), sprite.getMinV()));
        lightEnd(color(buff.pos(x + 0.50, y + 1.25, z + 0.75)).tex(sprite.getMaxU(), sprite.getMinV()));
        lightEnd(color(buff.pos(x + 0.75, y + 0.75, z + 0.75)).tex(sprite.getMaxU(), sprite.getMaxV()));
        lightEnd(color(buff.pos(x + 0.75, y + 0.75, z + 0.25)).tex(sprite.getMinU(), sprite.getMaxV()));
        
        lightEnd(color(buff.pos(x + 0.25, y + 1.25, z + 0.50)).tex(sprite.getMinU(), sprite.getMinV()));
        lightEnd(color(buff.pos(x + 0.75, y + 1.25, z + 0.50)).tex(sprite.getMaxU(), sprite.getMinV()));
        lightEnd(color(buff.pos(x + 0.75, y + 0.75, z + 0.25)).tex(sprite.getMaxU(), sprite.getMaxV()));
        lightEnd(color(buff.pos(x + 0.25, y + 0.75, z + 0.25)).tex(sprite.getMinU(), sprite.getMaxV()));
        
        lightEnd(color(buff.pos(x + 0.25, y + 1.25, z + 0.50)).tex(sprite.getMinU(), sprite.getMinV()));
        lightEnd(color(buff.pos(x + 0.75, y + 1.25, z + 0.50)).tex(sprite.getMaxU(), sprite.getMinV()));
        lightEnd(color(buff.pos(x + 0.75, y + 0.75, z + 0.75)).tex(sprite.getMaxU(), sprite.getMaxV()));
        lightEnd(color(buff.pos(x + 0.25, y + 0.75, z + 0.75)).tex(sprite.getMinU(), sprite.getMaxV()));
    
    
    }
    
    // Helper method for duplicate code
    private BufferBuilder color(BufferBuilder buff){
        return buff.color(1f, 1f, 1f, 1f);
    }
    
    // Helper method for duplicate code
    private void lightEnd(BufferBuilder buff){
        // 200/200 are just values that I use, at 255 it is transparent
        buff.lightmap(200,200).endVertex();
    }
    
}
