package Result;

import Model.DestinationCard;
import Model.DestinationCardHand;

import java.util.List;

public class AssignDestinationCardsResult implements iResult {
    private String errorMessage;
    private boolean success;
    private List<DestinationCard> cards;

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

    public List<DestinationCard> getCards() {
        return cards;
    }

    public void setCards(List<DestinationCard> cards) {
        this.cards = cards;
    }
}

