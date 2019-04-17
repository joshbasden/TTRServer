package Command.ServerCommand;

import Request.RegisterRequest;
import Request.iRequest;
import Result.RegisterResult;
import Service.RegisterService;

/**
 * Created by jbasden on 1/29/19.
 */

public class RegisterCommand implements iServerCommand {
    private iRequest data;
    private ServerCommandType type = ServerCommandType.S_REGISTER;
    public RegisterCommand(iRequest request) {
        data = request;
    }

    @Override
    public RegisterResult execute() {
        RegisterService registerService = new RegisterService();
        return registerService.register((RegisterRequest)data);
    }

    public iRequest getData() {
        return data;
    }

    public void setData(iRequest data) {
        this.data = data;
    }

    @Override
    public String getType() {
        return type.name();
    }
}
