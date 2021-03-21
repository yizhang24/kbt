package kiwibot.Config;

public class Guild{
    public String id;
    public String name;
    public String djRoleID;
    public String prefix;

    Guild(net.dv8tion.jda.api.entities.Guild _guild){
        this.id = _guild.getId();
        this.name = _guild.getName();
    }

}
