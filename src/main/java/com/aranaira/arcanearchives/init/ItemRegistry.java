package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.items.ManifestItem;
import com.aranaira.arcanearchives.items.RawQuartzItem;
import com.aranaira.arcanearchives.items.gems.GemRechargePowder;
import com.aranaira.arcanearchives.items.gems.GemRechargePowderRainbow;
import com.aranaira.arcanearchives.items.gems.asscher.*;
import com.aranaira.arcanearchives.items.gems.oval.MunchstoneItem;
import com.aranaira.arcanearchives.items.gems.oval.OrderstoneItem;
import com.aranaira.arcanearchives.items.gems.oval.TransferstoneItem;
import com.aranaira.arcanearchives.items.gems.pampel.Elixirspindle;
import com.aranaira.arcanearchives.items.gems.pampel.MindspindleItem;
import com.aranaira.arcanearchives.items.gems.pendeloque.MountaintearItem;
import com.aranaira.arcanearchives.items.gems.pendeloque.ParchtearItem;
import com.aranaira.arcanearchives.items.gems.pendeloque.RivertearItem;
import com.aranaira.arcanearchives.items.gems.trillion.PhoenixwayItem;
import com.aranaira.arcanearchives.items.gems.trillion.StormwayItem;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ItemRegistry {

  public static final RawQuartzItem RAW_RADIANT_QUARTZ = new RawQuartzItem();
  public static final ManifestItem MANIFEST = new ManifestItem();
  public static final LetterOfInvitationItem LETTER_OF_INVITATION = new LetterOfInvitationItem();
  public static final LetterOfResignationItem LETTER_OF_RESIGNATION = new LetterOfResignationItem();
  public static final WritOfExpulsionItem WRIT_OF_EXPULSION = new WritOfExpulsionItem();

  public static void init() {
  }
}
