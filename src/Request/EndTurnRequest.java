package Request;

public class EndTurnRequest implements iRequest {
    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
