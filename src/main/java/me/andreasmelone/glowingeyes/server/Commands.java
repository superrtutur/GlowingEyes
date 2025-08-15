package me.andreasmelone.glowingeyes.server;

import me.andreasmelone.glowingeyes.client.commands.EyesCommand;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Commands {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        EyesCommand.register(event.getDispatcher());
    }
}
