package Result;

/**
 * Created by jbasden on 1/29/19.
 */

public class CreateGameResult implements iResult {
    private String errorMessage;
    private boolean success;
    GameInfoResult gameInfo;

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

    public GameInfoResult getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameInfoResult gameInfo) {
        this.gameInfo = gameInfo;
    }
}
