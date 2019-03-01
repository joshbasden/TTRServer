package Request;

/**
 * Created by jbasden on 1/29/19.
 */

public class CreateGameRequest implements iRequest {
    private String gameName;
    private int numPlayers;
    private String username;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
