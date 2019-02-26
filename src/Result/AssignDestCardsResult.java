package Result;

import Model.DestCard;

import java.util.List;

public class AssignDestCardsResult implements iResult {
    private String errorMessage;
    private boolean success;
    private List<DestCard> destCards;

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

    public List<DestCard> getDestCards() {
        return destCards;
    }

    public void setDestCards(List<DestCard> destCards) {
        this.destCards = destCards;
    }
}

