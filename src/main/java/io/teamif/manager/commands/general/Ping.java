package io.teamif.manager.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import io.teamif.manager.helper.ConfigHelper;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class Ping extends Command {
    private final static String embed_color = new ConfigHelper().with("config.ini").getProperty("embed_color");

    public Ping() {
        this.name = "핑";
        this.aliases = new String[]{"ping"};
        this.help = "봇의 핑을 보여줍니다.";
        this.guildOnly = false;
        this.category = new Category("일반");
    }

    @Override
    protected void execute(CommandEvent msg) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.decode(embed_color));
        builder.addField("Ping","**" + msg.getJDA().getGatewayPing() + "**ms",true);
        msg.getChannel().sendMessage(builder.build()).queue();
    }
}
