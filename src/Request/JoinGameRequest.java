package Request;

/**
 * Created by jbasden on 1/29/19.
 */

public class JoinGameRequest implements iRequest {
    private String gameName;
    private String userName;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
