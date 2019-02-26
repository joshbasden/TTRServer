package Command.ServerCommand;

import Request.LoginRequest;
import Request.iRequest;
import Result.LoginResult;
import Service.LoginService;

/**
 * Created by jbasden on 1/29/19.
 */

public class LoginCommand implements iServerCommand {
    private iRequest data;
    public LoginCommand(iRequest request) {
        data = request;
    }
    @Override
    public LoginResult execute() {
        LoginService loginService = new LoginService();
        return loginService.login((LoginRequest)data);
    }

    public iRequest getData() {
        return data;
    }

    public void setData(iRequest data) {
        this.data = data;
    }
}
