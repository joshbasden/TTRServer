package Command.ClientCommand;

import Model.GameInfo;

/**
 * Created by jbasden on 1/29/19.
 */

public class AddGameCommand implements iClientCommand {
    private GameInfo gameInfo;

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

}
