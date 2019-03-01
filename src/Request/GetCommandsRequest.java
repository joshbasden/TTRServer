package Request;

/**
 * Created by jbasden on 1/29/19.
 */

public class GetCommandsRequest implements iRequest {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
