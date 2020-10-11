package io.teamif.manager.commands.owners;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Shutdown extends Command {

    public Shutdown() {
        this.name = "shutdown";
        this.aliases = new String[]{"종료"};
        this.help = "장비를 정지합니다.";
        this.guildOnly = false;
        this.ownerCommand = true;
        this.category = new Category("오너");
    }

    @Override
    protected void execute(CommandEvent msg) {
        msg.replyWarning("⚠️ 봇이 종료 됩니다. ⚠️");
        msg.getJDA().shutdown();
    }
}
