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
        if (model.createGame(req.getGameName(), req.getNumPlayers(), req.getUsername())) {
            gameInfo.setGameName(req.getGameName());
            gameInfo.setNumPlayers(req.getNumPlayers());
            res.setGameInfo(gameInfo);
            res.setSuccess(true);
            return res;
        }
        res.setErrorMessage("Invalid Game Name");
        res.setSuccess(false);
        res.setGameInfo(null);
        return res;
    }
}
