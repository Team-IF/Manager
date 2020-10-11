package io.teamif.manager.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import io.teamif.manager.helper.ConfigHelper;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

import static io.teamif.manager.Bot.commands;

public class Help extends Command {
    private final static String embed_color = new ConfigHelper().with("config.ini").getProperty("embed_color");
    final static String prefix = new ConfigHelper().with("config.ini").getProperty("prefix");

    public Help() {
        this.name = "도움";
        this.help = "봇의 도움말을 보여줍니다.";
        this.arguments = "[명령어 (선택적)]";
        this.aliases = new String[]{"help", "도움말"};
        this.category = new Category("일반");
        this.guildOnly = false;
    }

    public void execute(CommandEvent msg) {
        if (msg.getArgs().split(" ").length > 1) {
            msg.reply("너무 많은 인자를 주셨어요!" + getHelp());
        }
        else {
            if (msg.getArgs().equals("")) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(Color.decode(embed_color));
                eb.setDescription("`" + prefix + "help [명령어]`를 입력해서 명령어의 도움말을 받아보세요!");

                String general = ">>> ";
                String moderation = ">>> ";

                for (Command command : commands) {
                    if (!command.isOwnerCommand()) {
                        if (command.getCategory().getName() == "일반") {
                            general += command.getName() + " - " +
                                    command.getHelp() + "\n";
                        }
                    }
                }
                eb.addField("일반 명령어", general, true);
                msg.reply(eb.build());
            }
            else {
                Command cmd = null;
                for (Command command : commands) {
                    if (msg.getArgs().split(" ")[0].equalsIgnoreCase(command.getName())) {
                        cmd = command;
                    }
                }
                if (cmd == null) {
                    msg.reply("요청한 명령어가 없어요!" + getHelp());
                } else {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setColor(Color.decode(embed_color));
                    eb.setTitle(cmd.getName());
                    eb.setDescription(cmd.getHelp());

                    if (cmd.getArguments() != null) {
                        eb.addField("인자", cmd.getArguments(), true);
                    }

                    msg.reply(eb.build());
                }
            }
        }
    }
}
