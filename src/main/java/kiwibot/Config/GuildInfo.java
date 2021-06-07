package kiwibot.Config;

public class GuildInfo {
    public String id;
    public String name;
    public String djRoleID;
    public String prefix;

    GuildInfo(net.dv8tion.jda.api.entities.Guild _guild){
        this.id = _guild.getId();
        this.name = _guild.getName();
    }

}
