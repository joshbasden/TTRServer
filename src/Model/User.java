package Model;

import Command.ClientCommand.iClientCommand;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jbasden on 1/29/19.
 */

public class User {
    private String userName;
    private String password;
    private List<iClientCommand> commandList = new ArrayList<>();

    public List<iClientCommand> getCommands() {
        System.out.println("user.getCommand");
        System.out.println(new Gson().toJson(commandList));
        return commandList;
    }

    public void addCommand(iClientCommand command) {
        System.out.println("User.addCommand");
        System.out.println(new Gson().toJson(command));
        System.out.println("Command List before");
        System.out.println(new Gson().toJson(commandList));
        commandList.add(command);
        System.out.println("Command List after");
        System.out.println(new Gson().toJson(commandList));
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void clearCommands() {
        commandList.clear();
    }
}
