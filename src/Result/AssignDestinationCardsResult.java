package Result;

import Model.DestinationCard;

import java.util.List;

public class AssignDestinationCardsResult implements iResult {
    private String errorMessage;
    private boolean success;
    private List<DestinationCard> destinationCards;

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

    public List<DestinationCard> getDestinationCards() {
        return destinationCards;
    }

    public void setDestinationCards(List<DestinationCard> destinationCards) {
        this.destinationCards = destinationCards;
    }
}

