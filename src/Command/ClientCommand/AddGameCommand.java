package Command.ClientCommand;

import Result.GameInfoResult;

/**
 * Created by jbasden on 1/29/19.
 */

public class AddGameCommand implements iClientCommand {
    private GameInfoResult gameInfo;

    public GameInfoResult getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameInfoResult gameInfo) {
        this.gameInfo = gameInfo;
    }

}
