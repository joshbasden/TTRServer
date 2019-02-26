package Result;

/**
 * Created by jbasden on 1/29/19.
 */

public class RegisterResult implements iResult {
    private String errorMessage;
    private boolean success;
    private String userName;

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

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
