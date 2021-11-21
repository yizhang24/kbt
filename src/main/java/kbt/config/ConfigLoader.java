package kbt.config;

import java.io.File;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import kbt.Constants;

public class ConfigLoader {

    public static Config coreConfig;

    public ConfigLoader () {
        LoadConfig();
    }

    private void LoadConfig () {

        File file = new File(Constants.coreConfPath.resolve("core.config").toString());
        System.out.println(file.toPath().toString());

        coreConfig = ConfigFactory.parseFile(file);
        if (coreConfig.isEmpty()) {
            ConfigWriter.createConfig();
        }
        coreConfig = ConfigFactory.parseFile(file);
    }
}
