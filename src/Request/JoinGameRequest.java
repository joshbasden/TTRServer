package Request;

/**
 * Created by jbasden on 1/29/19.
 */

public class JoinGameRequest implements iRequest {
    private String gameName;
    private String username;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
