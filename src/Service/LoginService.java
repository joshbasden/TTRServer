package Service;

import Request.LoginRequest;
import Result.LoginResult;

/**
 * Created by jbasden on 1/29/19.
 */

public class LoginService {
    Model.Model model = Model.Model.getInstance();
    public LoginResult login(LoginRequest req) {
        LoginResult res = new LoginResult();
        if (model.authenticateUser(req.getUserName(), req.getPassword())) {
            res.setSuccess(true);
	        res.setUserName(req.getUserName());
	        model.addAllAddableGamesToCommandLists(req.getUserName());
        }
        else {
            res.setErrorMessage("Invalid Login Credentials");
            res.setSuccess(false);
        }
        return res;
    }
}
