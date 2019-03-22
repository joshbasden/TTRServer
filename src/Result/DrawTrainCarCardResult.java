package Result;

import Model.TrainCarCard;

public class DrawTrainCarCardResult implements iResult {
    String errorMessage;
    Boolean success;
    TrainCarCard card;

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

    public TrainCarCard getCard() {
        return card;
    }

    public void setCard(TrainCarCard card) {
        this.card = card;
    }
}
