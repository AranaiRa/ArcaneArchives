package com.aranaira.arcanearchives.commands;

import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.PacketClipboard.CopyToClipboard;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;

public class CommandCopy extends CommandBase {
  @Override
  public String getName() {
    return "clipboard";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/clipboard <text to copy>";
  }

  @Override
  public int getRequiredPermissionLevel() {
    return 2;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    StringBuilder message = new StringBuilder();
    for (int i = 0; i < args.length; i++) {
      message.append(args[i]);
      if (i != args.length - 1) {
        message.append(" ");
      }
    }

    if (sender.getCommandSenderEntity() instanceof PlayerEntity) {
      CopyToClipboard packet = new CopyToClipboard(message.toString());
      Networking.CHANNEL.sendTo(packet, (ServerPlayerEntity) sender);
      sender.sendMessage(new StringTextComponent("Copied to the clipboard."));
    }
  }
}
