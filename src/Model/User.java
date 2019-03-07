package Model;

import Command.ClientCommand.iClientCommand;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jbasden on 1/29/19.
 */

public class User {
    private String username;
    private String password;
    private List<iClientCommand> commandList = new ArrayList<>();

    public List<iClientCommand> getCommands() {
        return commandList;
    }

    public void addCommand(iClientCommand command) {
        commandList.add(command);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
