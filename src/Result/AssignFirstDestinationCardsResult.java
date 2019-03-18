package Result;

import Model.DestinationCardHand;

public class AssignFirstDestinationCardsResult implements iResult {
    private String errorMessage;
    private Boolean success;
    private DestinationCardHand hand;

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

    public DestinationCardHand getHand() {
        return hand;
    }

    public void setHand(DestinationCardHand hand) {
        this.hand = hand;
    }

}
