package Result;

import Model.iCard;

public class DrawFaceUpResult implements iResult {
    String errorMessage;
    Boolean success;
    iCard card;

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

    public iCard getCard() {
        return card;
    }

    public void setCard(iCard card) {
        this.card = card;
    }
}
