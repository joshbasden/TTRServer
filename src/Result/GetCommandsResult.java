package Result;

import Command.ClientCommand.iClientCommand;

import java.util.List;

/**
 * Created by jbasden on 1/29/19.
 */

public class GetCommandsResult implements iResult {
    private String errorMessage;
    private boolean success;
    private String userName;
    private List<iClientCommand> commandList;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<iClientCommand> getCommandList() {
        return commandList;
    }

    public void setCommandList(List<iClientCommand> commandList) {
        this.commandList = commandList;
    }
}
