package Server;

import Database.Database;
import Model.Model;
import Plugin.PluginDescriptor;
import Plugin.PluginRegistry;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by jbasden on 1/29/19.
 */

public class Server {
    private static final int MAX_WAITING_CONNECTIONS = 12;
    private HttpServer server;
    private void run(String portNumber) {
        try {
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        server.setExecutor(null);
        server.createContext("/command", new ServerCommandHandler());
        server.start();
        System.out.println("Server started");
    }

    private static void printUsage() {
        System.out.println("Usage: java TTRServer.jar <port> <plugin-name>");
    }

    public static void main(String[] args) {
        if (args[0].equals("register")) {
            PluginDescriptor descriptor = new PluginDescriptor(args[1], args[2], args[3], args[4]);
            PluginRegistry.instance.registerPlugin(descriptor);
            return;
        } else if (args[0].equals("clear")) {
            Database db = PluginRegistry.instance.getDatabase();
            try {
                PluginRegistry.instance.setDatabasePlugin(args[1]);
                db = PluginRegistry.instance.getDatabase();
                db.openConnection();
                db.clear();
                db.closeConnection(true);
            } catch (Exception e) {
                try {
                    db.closeConnection(false);
                } catch (Exception f) { return; }
                printUsage();
                e.printStackTrace();
            }
            return;
        }

        String portNumber = args[0];
        String databaseName = args[1];
        try {
            PluginRegistry.instance.setDatabasePlugin(databaseName);
            new Server().run(portNumber);
        } catch (PluginRegistry.PluginNotFoundException e) {
            System.out.println("Plugin " + databaseName + " is not registered.");
        } catch (Exception e) {
            printUsage();
            e.printStackTrace();
        }

        Model model = Model.getInstance();
        try {
            int N = Integer.parseInt(args[2]);
            model.setN(N);
            model.initialize();
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("The third argument must be a valid integer. Defaulting to 20.");
            model.setN(20);
        }
    }
}