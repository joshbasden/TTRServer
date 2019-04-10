package Plugin;

import Database.Database;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class PluginRegistry {

    public static PluginRegistry instance = new PluginRegistry();

    private Database currentDatabase;

    public void registerPlugin(PluginDescriptor descriptor) {
        try {
            PrintWriter writer = new PrintWriter("Configuration.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Config file not found.");
        }
    }

    public void setPlugin(PluginDescriptor descriptor) {

    }

    public Database getPlugin() {
        return currentDatabase;
    }
}