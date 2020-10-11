package io.teamif.manager.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Ping extends Command {
    public Ping() {
        this.name = "핑";
        this.aliases = new String[]{"ping"};
        this.help = "봇의 핑을 보여줍니다.";
        this.guildOnly = false;
        this.category = new Category("일반");
    }

    @Override
    protected void execute(CommandEvent msg) {
        msg.getChannel().sendMessage("Ping: " + msg.getJDA().getGatewayPing() + "ms").queue();
    }
}
