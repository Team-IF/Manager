package io.teamif.manager.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import io.teamif.manager.helper.ConfigHelper;
import net.dv8tion.jda.api.entities.ApplicationInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class About extends Command {
    private final static String embed_color = new ConfigHelper().main().getProperty("embed_color");
    private String oauthLink;

    public About()
    {
        this.name = "정보";
        this.aliases = new String[]{"about"};
        this.help = "봇의 상태 및 정보를 보여줍니다.";
        this.guildOnly = false;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.category = new Category("일반");
    }

    @Override
    protected void execute(CommandEvent msg) {
        if (oauthLink == null) {
            try {
                ApplicationInfo info = msg.getJDA().retrieveApplicationInfo().complete();
                oauthLink = info.isBotPublic() ? info.getInviteUrl(0L) : "";
            } catch (Exception e) {
                Logger log = LoggerFactory.getLogger("OAuth2");
                log.error("Could not generate invite link ", e);
                oauthLink = "";
            }
        }
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.decode(embed_color));
        builder.setAuthor(msg.getSelfUser().getName() + " 정보", null, msg.getSelfUser().getAvatarUrl());
//        boolean join = !(msg.getClient().getServerInvite() == null || msg.getClient().getServerInvite().isEmpty());
//        boolean inv = !oauthLink.isEmpty();
//        String invline = "저를 당신의 서버에 [`초대`](" + oauthLink + ") 해주세요!";
//
//        StringBuilder descr = new StringBuilder().append("안녕하세요! 저는 **").append(msg.getSelfUser().getName()).append("** 입니다!")
//                .append("\n저는 Java로 만들어졌어요!").append("\n`").append(msg.getClient().getTextualPrefix()).append(msg.getClient().getHelpWord())
//                .append("`를 입력해서 제 명령어들을 보세요!\n").append(join || inv ? invline : "").append("\n\n저는 이런것들을 할 수 있어요!: ```css\n- 일반적인 명령어```");
//        builder.setDescription(descr);
        if (msg.getJDA().getShardInfo() == null)
        {
            builder.addField("상태", msg.getJDA().getGuilds().size() + " 서버\n1 샤드", true);
            builder.addField("사용자 수", msg.getJDA().getUsers().size() + " 명\n" + msg.getJDA().getGuilds().stream().mapToInt(g -> g.getMembers().size()).sum(), true);
            builder.addField("채널 수", msg.getJDA().getTextChannels().size() + " 개\n" + msg.getJDA().getVoiceChannels().size(), true);
        }
        else
        {
            builder.addField("종합적 상태", (msg.getClient()).getTotalGuilds() + " 서버\n샤드: " + (msg.getJDA().getShardInfo().getShardId() + 1)
                    + "/" + msg.getJDA().getShardInfo().getShardTotal(), true);
            builder.addField("이 샤드 정보", msg.getJDA().getUsers().size() + " 명\n" + msg.getJDA().getGuilds().size() + " 개의 서버", true);
            builder.addField("", msg.getJDA().getTextChannels().size() + " 개의 텍스트 채널\n" + msg.getJDA().getVoiceChannels().size() + " 개의 음성 채널", true);
        }
        builder.setFooter("마지막 재시동", null);
        builder.setTimestamp(msg.getClient().getStartTime());
        msg.reply(builder.build());
    }
}