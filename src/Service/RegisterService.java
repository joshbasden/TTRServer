package Service;

import Model.Model;
import Request.RegisterRequest;
import Result.RegisterResult;

/**
 * Created by jbasden on 1/29/19.
 */

public class RegisterService {
    Model model = Model.getInstance();
    public RegisterResult register(RegisterRequest req) {
        RegisterResult res = new RegisterResult();
        if (model.createUser(req.getUsername(), req.getPassword())) {
            res.setUsername(req.getUsername());
            res.setSuccess(true);
            model.addAllAddableGamesToCommandLists(req.getUsername());
            return res;
        }
        res.setErrorMessage("Invalid username or password");
        res.setSuccess(false);
        return res;
    }
}
