package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.RadiantChestBlock;
import com.aranaira.arcanearchives.blocks.RadiantTankBlock;
import com.aranaira.arcanearchives.blocks.RadiantTrove;
import com.aranaira.arcanearchives.blocks.templates.TemplateBlock;
import com.aranaira.arcanearchives.data.DataHelper;
import com.aranaira.arcanearchives.data.types.ServerNetwork;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.inventory.handlers.OptionalUpgradesHandler;
import com.aranaira.arcanearchives.inventory.handlers.SizeUpgradeItemHandler;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity.VoidingFluidTank;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity.TroveItemHandler;
import com.aranaira.arcanearchives.types.IteRef;
import com.aranaira.arcanearchives.types.UpgradeType;
import com.aranaira.arcanearchives.types.lists.TileList;
import com.aranaira.arcanearchives.util.UploadUtils;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.List;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class DebugOrbItem extends ItemTemplate {
	public static final String NAME = "debugorb";

	public DebugOrbItem () {
		super(NAME);
		setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.debugorb"));
		tooltip.add(TextFormatting.RED + I18n.format("arcanearchives.tooltip.creativeonly"));
	}

	@Override
	public ActionResultType onItemUseFirst (PlayerEntity player, World world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ, Hand hand) {
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if (block.isAir(state, world, pos) || !(block instanceof TemplateBlock) || !block.hasTileEntity(state)) {
			return ActionResultType.PASS;
		}

		ImmanenceTileEntity ite = WorldUtil.getTileEntity(ImmanenceTileEntity.class, world, pos);
		if (ite == null) {
			return ActionResultType.PASS;
		}

		if (player.isSneaking() && block instanceof RadiantTrove && ite instanceof RadiantTroveTileEntity) {
			RadiantTroveTileEntity rte = (RadiantTroveTileEntity) ite;
			TroveItemHandler handler = rte.getInventory();
			if (handler.getItem().isEmpty()) {
				handler.setReference(new ItemStack(Items.SNOWBALL));
				handler.setCount(handler.getMaxCount());
				player.sendStatusMessage(new StringTextComponent("Filled your empty trove!").setStyle(new Style().setColor(TextFormatting.GOLD).setBold(true)), true);
				return ActionResultType.SUCCESS;
			} else {
				handler.setReference(ItemStack.EMPTY);
				handler.setCount(0);
				player.sendStatusMessage(new StringTextComponent("Hope you didn't need what was in there!").setStyle(new Style().setColor(TextFormatting.DARK_RED).setBold(true)), true);
				return ActionResultType.SUCCESS;
			}
		} else if (player.isSneaking()) {
			return ActionResultType.SUCCESS;
		}

		ServerNetwork sNetwork = null;
		if (!world.isRemote) {
			sNetwork = DataHelper.getServerNetwork(ite.getNetworkId());
		}

		if (world.isRemote) {
			player.sendMessage(new StringTextComponent(" --- BEGINNING DEBUG --- WARNING: MAY CAUSE LAG/SHORT LOCK-UP, DON'T PANIC!").setStyle(new Style().setColor(TextFormatting.GOLD).setBold(true)));
		}

		String debugOutput = "";

		if (world.isRemote) {
			debugOutput += "---------- CLIENT SIDE REPORT ----------\n";
		} else {
			debugOutput += "---------- SERVER SIDE REPORT ----------\n";
		}

		debugOutput += "Block located at: " + pos.getX() + "," + pos.getY() + "," + pos.getZ() + "\n";

		if (block instanceof RadiantChestBlock) {
			debugOutput += "- is a Radiant Chest\n";
			RadiantChestTileEntity te = WorldUtil.getTileEntity(RadiantChestTileEntity.class, world, pos);
			if (te == null) {
				debugOutput += "- has INVALID TILE ENTITY\n";
			} else {
				if (te.getNetworkId() != null) {
					debugOutput += "- Part of the network " + te.getNetworkId().toString() + "\n";
				} else {
					debugOutput += "- has INVALID NETWORK ID\n";
				}
				if (te.getUuid() != null) {
					debugOutput += "- Has tile id " + te.getUuid().toString() + "\n";
				} else {
					debugOutput += "- has INVALID TILE ID\n";
				}
				if (te.getDisplayStack().isEmpty()) {
					debugOutput += "- Has no set display item\n";
				} else {
					debugOutput += "- Has " + te.getDisplayStack().toString() + " (" + te.getDisplayStack().getItem().getRegistryName() + ") as display item\n";
				}
				debugOutput += "- Has display facing " + te.getDisplayFacing().toString() + "\n";
				debugOutput += "- Has brazier routing type " + te.getRoutingType().toString() + "\n";
				debugOutput += "- Has chest name '" + te.chestName + "'\n";
				debugOutput += "- Contains " + te.getOrCalculateReference().size() + " unique items\n";
				if (sNetwork != null) {
					if (sNetwork.containsTile(te)) {
						debugOutput += "- Is contained in network tile list\n";
					} else {
						debugOutput += "- Is NOT contained in network tile list\n";
					}
					TileList tiles = sNetwork.getTiles();
					IteRef ref = tiles.getReference(te.getUuid());
					if (ref == null) {
						debugOutput += "- Tile List reference is null\n";
					}
				}
			}
		} else if (block instanceof RadiantTrove) {
			debugOutput += "- is a Radiant Trove\n";
			RadiantTroveTileEntity te = WorldUtil.getTileEntity(RadiantTroveTileEntity.class, world, pos);
			if (te == null) {
				debugOutput += "- has INVALID TILE ENTITY\n";
			} else {
				if (te.getNetworkId() != null) {
					debugOutput += "- Part of the network " + te.getNetworkId().toString() + "\n";
				} else {
					debugOutput += "- has INVALID NETWORK ID\n";
				}
				if (te.getUuid() != null) {
					debugOutput += "- Has tile id " + te.getUuid().toString() + "\n";
				} else {
					debugOutput += "- has INVALID TILE ID\n";
				}
				debugOutput += "- last click is " + te.getLastClick() + "\n";
				debugOutput += "- last tick is " + te.getLastTick() + "\n";
				if (te.getLastUUID() == null) {
					debugOutput += "- no last UUID\n";
				} else {
					debugOutput += "- last UUID is " + te.getLastUUID().toString() + "\n";
				}
				SizeUpgradeItemHandler sizes = te.getSizeUpgradesHandler();
				debugOutput += "- has " + sizes.getUpgradesCount() + " upgrades\n";
				OptionalUpgradesHandler optionals = te.getOptionalUpgradesHandler();
				debugOutput += "- " + (optionals.hasUpgrade(UpgradeType.VOID) ? "has" : "does not have") + " void upgrade\n";
				TroveItemHandler inventory = te.getInventory();
				debugOutput += "- contains " + inventory.getItem().toString() + " (" + inventory.getItem().getItem().getRegistryName() + ")\n";
				debugOutput += "- has total of " + inventory.getCount() + " items stored\n";
				if (sNetwork != null) {
					if (sNetwork.containsTile(te)) {
						debugOutput += "- Is contained in network tile list\n";
					} else {
						debugOutput += "- Is NOT contained in network tile list\n";
					}
					TileList tiles = sNetwork.getTiles();
					IteRef ref = tiles.getReference(te.getUuid());
					if (ref == null) {
						debugOutput += "- Tile List reference is null\n";
					}
				}
			}
		} else if (block instanceof RadiantTankBlock) {
			debugOutput += "- is a Radiant Tank\n";
			RadiantTankTileEntity te = WorldUtil.getTileEntity(RadiantTankTileEntity.class, world, pos);
			if (te == null) {
				debugOutput += "- has INVALID TILE ENTITY\n";
			} else {
				if (te.getNetworkId() != null) {
					debugOutput += "- Part of the network " + te.getNetworkId().toString() + "\n";
				} else {
					debugOutput += "- has INVALID NETWORK ID\n";
				}
				if (te.getUuid() != null) {
					debugOutput += "- Has tile id " + te.getUuid().toString() + "\n";
				} else {
					debugOutput += "- has INVALID TILE ID\n";
				}
				SizeUpgradeItemHandler sizes = te.getSizeUpgradesHandler();
				debugOutput += "- has " + sizes.getUpgradesCount() + " upgrades\n";
				OptionalUpgradesHandler optionals = te.getOptionalUpgradesHandler();
				debugOutput += "- " + (optionals.hasUpgrade(UpgradeType.VOID) ? "has" : "does not have") + " void upgrade\n";
				VoidingFluidTank inventory = te.getInventory();
				FluidStack contents = inventory.getFluid();
				debugOutput += "- " + (contents == null ? "contains no fluid " : "contains " + contents.getUnlocalizedName()) + "\n";
				if (contents != null) {
					debugOutput += "- contains " + contents.amount + "mb of fluid\n";
				}
				if (sNetwork != null) {
					if (sNetwork.containsTile(te)) {
						debugOutput += "- Is contained in network tile list\n";
					} else {
						debugOutput += "- Is NOT contained in network tile list\n";
					}
					TileList tiles = sNetwork.getTiles();
					IteRef ref = tiles.getReference(te.getUuid());
					if (ref == null) {
						debugOutput += "- Tile List reference is null\n";
					}
				}
			}
		} else {
			return ActionResultType.PASS;
		}

		String url;
		String identifier = world.isRemote ? "Client" : "Server";
		try {
			url = UploadUtils.uploadToHaste("debug" + identifier, debugOutput);
		} catch (IOException e) {
			player.sendMessage(new StringTextComponent("Unable to upload " + identifier + " debug information to HasteBin."));
			e.printStackTrace();
			return ActionResultType.SUCCESS;
		}

		player.sendMessage(getCopyMessage("Successfully uploaded " + identifier + " report to HasteBin: " + url, url));
		return ActionResultType.SUCCESS;
	}

	public static ITextComponent getCopyMessage (String message, String copyMessage) {
		Style style = new Style();
		ClickEvent click = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clipboard " + copyMessage);
		style.setClickEvent(click);

		HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Click to copy [\u00A76" + copyMessage + "\u00A7r]"));
		style.setHoverEvent(hoverEvent);

		return new StringTextComponent(message).setStyle(style);
	}

	@SubscribeEvent
	public static void onLeftClickBlock (LeftClickBlock event) {
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		PlayerEntity player = event.getEntityPlayer();
		if (player.getHeldItemMainhand().getItem() != ItemRegistry.DEBUG_ORB) {
			return;
		}
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if (!(block instanceof TemplateBlock)) {
			return;
		}

		if (block instanceof RadiantChestBlock) {
			event.setUseBlock(Result.DENY);
			event.setUseItem(Result.DENY);
			RadiantChestTileEntity te = WorldUtil.getTileEntity(RadiantChestTileEntity.class, world, pos);
			player.sendMessage(new StringTextComponent(world.isRemote ? "Client-side data:" : "Server-side data:"));
			if (te == null) {
				player.sendMessage(new StringTextComponent("There's no Radiant Chest tile entity! WTF?"));
			}

			player.sendMessage(new StringTextComponent("Radiant chest is named: " + te.getChestName()));
			Direction facing = te.getDisplayFacing();
			ItemStack display = te.getDisplayStack();
			if (display.isEmpty()) {
				player.sendMessage(new StringTextComponent("Radiant chest has no item stack on display."));
			} else {
				player.sendMessage(new StringTextComponent("Radiant chest has a display item facing " + facing.toString() + " which is: " + display.toString()));
			}
		}
	}
}
