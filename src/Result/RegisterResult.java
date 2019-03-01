package Result;

/**
 * Created by jbasden on 1/29/19.
 */

public class RegisterResult implements iResult {
    private String errorMessage;
    private boolean success;
    private String username;

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

    public void setUsername(String username) {
        this.username = username;
    }
}
