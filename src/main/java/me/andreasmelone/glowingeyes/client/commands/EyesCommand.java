package me.andreasmelone.glowingeyes.client.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import me.andreasmelone.glowingeyes.server.capability.eyes.GlowingEyesCapability;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class EyesCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("eye")
                        .requires(src -> src.hasPermission(0))
                        .then(Commands.literal("toggle")
                                .then(Commands.argument("target", EntityArgument.players())
                                        .then(Commands.argument("state", BoolArgumentType.bool())
                                                .executes(ctx -> {
                                                    Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "target");
                                                    boolean state = BoolArgumentType.getBool(ctx, "state");

                                                    List<ServerPlayer> players = ctx.getSource().getServer().getPlayerList()
                                                            .getPlayers()
                                                            .stream()
                                                            .toList();
                                                    for (ServerPlayer player : targets) {
                                                        GlowingEyesCapability.setToggledOn(player, state);
                                                        for (ServerPlayer getpla : players) {
                                                            GlowingEyesCapability.sendUpdate(player, getpla);
                                                        }
                                                    }
                                                    return targets.size();
                                                })
                                        )
                                )
                        )
                        .then(Commands.literal("eye")
                                .then(Commands.argument("target", EntityArgument.players())
                                        .then(Commands.argument("r", IntegerArgumentType.integer(0, 255))
                                                .then(Commands.argument("g", IntegerArgumentType.integer(0, 255))
                                                        .then(Commands.argument("b", IntegerArgumentType.integer(0, 255))
                                                                .executes(ctx -> {
                                                                    Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "target");
                                                                    int r = IntegerArgumentType.getInteger(ctx, "r");
                                                                    int g = IntegerArgumentType.getInteger(ctx, "g");
                                                                    int b = IntegerArgumentType.getInteger(ctx, "b");
                                                                    Color newColor = new Color(r, g, b);

                                                                    List<ServerPlayer> players = ctx.getSource().getServer().getPlayerList()
                                                                            .getPlayers()
                                                                            .stream()
                                                                            .toList();

                                                                    for(ServerPlayer getplayer : targets) {
                                                                        HashMap<Point, Color> oldGlowingMap = GlowingEyesCapability.getGlowingEyesMap(getplayer);
                                                                        HashMap<Point, Color> newGlowingMap = new HashMap<>();

                                                                        oldGlowingMap.forEach((point, color) -> {
                                                                            newGlowingMap.put(point, newColor);
                                                                        });
                                                                        GlowingEyesCapability.setGlowingEyesMap(getplayer, newGlowingMap);
                                                                        for (ServerPlayer getpla : players) {
                                                                            GlowingEyesCapability.sendUpdate(getplayer, getpla);
                                                                        }
                                                                        GlowingEyesCapability.sendUpdate();
                                                                    }
                                                                    return targets.size();
                                                                })
                                                        )
                                                )
                                        )
                                )
                        )
        );
    }
}
