package Result;

/**
 * Created by jbasden on 1/29/19.
 */

public class JoinGameResult implements iResult {
    private String errorMessage;
    private boolean success;
    private String userName;
    private String gameName;
    private int numPlayers;
    private String playerColor;

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

    public String getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }
}
