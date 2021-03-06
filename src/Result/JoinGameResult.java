package Result;

import Model.Player;
import Model.PlayerColor;

/**
 * Created by jbasden on 1/29/19.
 */

public class JoinGameResult implements iResult {
    private String errorMessage;
    private boolean success;
    private String username;
    private String gameName;
    private int numPlayers;
    private PlayerColor playerColor;

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

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(PlayerColor playerColor) {
        this.playerColor = playerColor;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }
}
