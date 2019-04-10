package Plugin;

import Database.Database;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;

public class PluginRegistry {

    public static PluginRegistry instance = new PluginRegistry();

    public class PluginNotFoundException extends Exception {}

    private Database currentDatabase;

    public void registerPlugin(PluginDescriptor descriptor) {
        try {
            PrintWriter writer = new PrintWriter("Configuration.txt");
            writer.println(descriptor.getName() + " " + descriptor.getJarPath() + " " +
                    descriptor.getJarName() + " " + descriptor.getClassName());
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Config file not found.");
        }
    }

    public void setDatabasePlugin(String name) throws Exception {
        PluginDescriptor descriptor = getPluginInfo(name);

        // Get class loader to load the jar file
        File pluginJarFile = new File(descriptor.getJarPath(), descriptor.getJarName());
        URL pluginURL = pluginJarFile.toURI().toURL();
        URLClassLoader loader = new URLClassLoader(new URL[]{pluginURL});

        // Load the jar file's plugin class and set the instance
        Class<? extends Database> databasePlugin = (Class<Database>) loader.loadClass(descriptor.getClassName());
        currentDatabase = databasePlugin.getDeclaredConstructor().newInstance();
    }

    public Database getDatabasePlugin() {
        return currentDatabase;
    }

    private PluginDescriptor getPluginInfo(String name) throws PluginNotFoundException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("Configuration.txt"));
            String line = reader.readLine();
            while (line != null) {
                String[] split = line.split("\\s+");
                if (split[0] == name) {
                    return new PluginDescriptor(split[0], split[1], split[2], split[3]);
                }
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Config file not found.");
        } catch (IOException e) {
            System.out.println("Config file could not be read.");
        }

        throw new PluginNotFoundException();
    }
}