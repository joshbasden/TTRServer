package Result;

import Model.GameInfo;

/**
 * Created by jbasden on 1/29/19.
 */

public class CreateGameResult implements iResult {
    private String errorMessage;
    private boolean success;
    GameInfo gameInfo;

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

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }
}
