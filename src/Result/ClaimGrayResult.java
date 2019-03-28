package Result;

import Model.TrainCarCardType;

public class ClaimGrayResult implements iResult {
    String errorMessage;
    boolean success;
    int id;
    TrainCarCardType color;

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setColor(TrainCarCardType color) {
        this.color = color;
    }

    public void setId(int id) {
        this.id = id;
    }
}
