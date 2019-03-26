package Result;

import Model.TrainCarCardType;

public class ClaimRouteResult implements iResult {
    private String errorMessage;
    private Boolean success;
    private int id;
    private TrainCarCardType colorIfGray;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TrainCarCardType getColorIfGray() {
        return colorIfGray;
    }

    public void setColorIfGray(TrainCarCardType colorIfGray) {
        this.colorIfGray = colorIfGray;
    }
}
