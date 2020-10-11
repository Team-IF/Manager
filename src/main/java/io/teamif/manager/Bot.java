package io.teamif.manager;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.command.impl.CommandClientImpl;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import io.teamif.manager.helper.ConfigHelper;
import io.teamif.manager.commands.general.*;
import io.teamif.manager.commands.owners.*;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.util.List;

public class Bot extends ListenerAdapter {
    private static JDA api;
    public static List<Command> commands;

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent msg) {
        if (msg.getAuthor().isBot()) return;
    }

    @Override
    public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent msg) {
        return;
    }

    public static void main(String[] args) throws LoginException {
        ConfigHelper configHelper = new ConfigHelper();
        final String token = configHelper.with("config.ini").getProperty("token");
        final String prefix = configHelper.with("config.ini").getProperty("prefix");
        final String owner = configHelper.with("config.ini").getProperty("owner");

        EventWaiter waiter = new EventWaiter();

        CommandClientBuilder cmd = new CommandClientBuilder();
        cmd.setPrefix(prefix);
        cmd.setOwnerId(owner);
        cmd.addCommands(
                new Ping(),
                new About(),
                new Shutdown(),
                new Help(),
                new ServerInfo()
        );
        cmd.useHelpBuilder(false);
        cmd.setActivity(Activity.listening(prefix + "help | 당신의 명령어를"));

        api = new JDABuilder(token)
                .addEventListeners(new Bot())
                .addEventListeners(waiter, cmd.build())
                .build();

        List<Object> listeners = api.getRegisteredListeners();
        for (Object l : listeners) {
            if (l instanceof CommandClientImpl) {
                commands = ((CommandClientImpl)l).getCommands();
            }
        }
    }
}
