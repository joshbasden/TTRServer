package Request;

public class DrawDestinationCardsRequest implements iRequest {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
