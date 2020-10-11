package io.teamif.manager.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import io.teamif.manager.helper.ConfigHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.time.format.DateTimeFormatter;

public class ServerInfo extends Command {
    public final static String LINESTART = "\u25AB";
    private final static String GUILD_EMOJI = "\uD83D\uDDA5";
    private final static String embed_color = new ConfigHelper().main().getProperty("embed_color");

    public ServerInfo() {
        this.name = "서버정보";
        this.aliases = new String[]{"sinfo", "서버", "serverinfo"};
        this.help = "서버의 정보를 보여줍니다.";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.category = new Category("일반");
    }

    @Override
    protected void execute(CommandEvent msg) {
        if (msg.getChannelType() == ChannelType.PRIVATE || msg.getChannelType() == ChannelType.GROUP) {
            msg.reply("NOPE");
        } else {
            Guild guild = msg.getGuild();
            Member owner = guild.getOwner();
            long onlineCount = guild.getMembers().stream().filter(u -> u.getOnlineStatus() != OnlineStatus.OFFLINE).count();
            long botCount = guild.getMembers().stream().filter(m -> m.getUser().isBot()).count();
            EmbedBuilder builder = new EmbedBuilder();
            String title = (GUILD_EMOJI + " **" + guild.getName() + "** 서버 정보")
                    .replace("@everyone", "@\u0435veryone")
                    .replace("@here", "@h\u0435re")
                    .replace("discord.gg/", "dis\u0441ord.gg/");
            String verif;
            switch (guild.getVerificationLevel()) {
                case NONE:
                    verif = "없음";
                    break;
                case LOW:
                    verif = "낮음";
                    break;
                case MEDIUM:
                    verif = "중간";
                    break;
                case HIGH:
                    verif = "(╯°□°）╯︵ ┻━┻";
                    break;
                case VERY_HIGH:
                    verif = "┻━┻ミヽ(ಠ益ಠ)ノ彡┻━┻";
                    break;
                default:
                    verif = guild.getVerificationLevel().name();
                    break;
            }
            String str =
                    LINESTART + "서버 ID: **" + guild.getId() + "**\n"
                            + LINESTART + "소유자: " + (owner == null ? "알 수 없음" : "**" + owner.getUser().getName() + "**#" + owner.getUser().getDiscriminator()) + "\n"
                            + LINESTART + "생성일: **" + guild.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME) + "**\n"
                            + LINESTART + "사용자: **" + guild.getMemberCache().size() + "** (온라인 " + onlineCount + "명, 봇 " + botCount + "개)\n"
                            + LINESTART + "채널: **" + guild.getTextChannelCache().size() + "**개의 텍스트 채널, **" + guild.getVoiceChannelCache().size() + "**개의 음성 채널, **" + guild.getCategoryCache().size() + "**개의 카테고리\n"
                            + LINESTART + "보안 등급: **" + verif + "**";
            if (!guild.getFeatures().isEmpty())
                str += "\n" + LINESTART + "기능: **" + String.join("**, **", guild.getFeatures()) + "**";
            if (guild.getSplashId() != null) {
                builder.setImage(guild.getSplashUrl() + "?size=1024");
                str += "\n" + LINESTART + "서버 스플래쉬 이미지: ";
            }
            if (guild.getIconUrl() != null)
                builder.setThumbnail(guild.getIconUrl());
            builder.setColor(Color.decode(embed_color));
            builder.setTitle(title);
            builder.setDescription(str);
            msg.reply(new MessageBuilder().setEmbed(builder.build()).build());
        }
    }
}
