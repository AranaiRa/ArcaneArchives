package com.aranaira.arcanearchives.command;

import com.aranaira.arcanearchives.api.domain.DomainManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class CommandArcaneArchives {
  public static void register(CommandDispatcher<CommandSource> dispatcher) {
    dispatcher.register(builder(Commands.literal("aa").requires(p -> p.hasPermission(2))));
  }

  public static LiteralArgumentBuilder<CommandSource> builder(LiteralArgumentBuilder<CommandSource> builder) {
    builder.executes(c -> {
      c.getSource().sendSuccess(new TranslationTextComponent("lootr.commands.usage"), false);
      return 1;
    });
    builder.then(Commands.literal("refresh").executes(c -> {
      DomainManager.forceRefresh();
      return 1;
    }));
    return builder;
  }
}

