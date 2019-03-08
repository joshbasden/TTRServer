package Service;

import Model.Model;
import Model.GameInfo;
import Request.CreateGameRequest;
import Result.CreateGameResult;

/**
 * Created by jbasden on 1/29/19.
 */

public class CreateGameService {
    Model model = Model.getInstance();
    public CreateGameResult createGame(CreateGameRequest req) {
        CreateGameResult res = new CreateGameResult();
        GameInfo gameInfo = new GameInfo();
        if (model.createGame(req.getGameName(), req.getNumPlayers())) {
            res.setSuccess(true);
            return res;
        }
        res.setErrorMessage("Invalid Game Name");
        res.setSuccess(false);
        return res;
    }
}
