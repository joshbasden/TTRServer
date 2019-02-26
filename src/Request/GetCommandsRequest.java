package Request;

/**
 * Created by jbasden on 1/29/19.
 */

public class GetCommandsRequest implements iRequest {
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
