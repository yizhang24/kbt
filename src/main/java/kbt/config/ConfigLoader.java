package kbt.config;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import kbt.Constants;
import net.dv8tion.jda.api.entities.Guild;

public class ConfigLoader {

    public Config coreConfig;

    private ConfigWriter configWriter = new ConfigWriter();

    private Map<String, Object> guildInfo = new HashMap<String, Object>();

    public ConfigLoader() {
        loadConfig();
    }

    private void loadConfig() {
        File file = Constants.CORE_CONF_PATH.toFile();
        System.out.println(file.toPath().toString());

        if (ConfigFactory.parseFile(file).isEmpty()) {
            configWriter.createConfig();
        }
        coreConfig = ConfigFactory.parseFile(file);
    }

    public void addGuilds(List<Guild> guilds) {
        boolean hasGuilds = false;
        if(coreConfig.hasPath("guilds")) {
            hasGuilds = true;
            guildInfo = coreConfig.getConfig("guilds").root().unwrapped();
        }
        for(Guild guild : guilds) {
            String guildId = guild.getId(); 
            if(hasGuilds) {
                if (coreConfig.getConfig("guilds").root().keySet().contains(guildId)) {
                    continue;
                }
            }
            guildInfo.put(guildId + ".prefix", "!");
        }
        updateGuilds();
    }

    private void updateGuilds() {
        Config guildConfig = ConfigFactory.parseMap(guildInfo);
        coreConfig = coreConfig.withFallback(guildConfig);
        configWriter.writeConfig(coreConfig);
    }
}
