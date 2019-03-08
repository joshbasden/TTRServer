package Result;

import Model.DestinationCard;
import Model.DestinationCardHand;

import java.util.List;

public class AssignDestinationCardsResult implements iResult {
    private String errorMessage;
    private boolean success;
    private List<DestinationCard> destinationCards;
    private DestinationCardHand hand;

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
        hand = new DestinationCardHand();
        hand.setCards(destinationCards);
    }

    public DestinationCardHand getHand() {
        return hand;
    }

    public void setHand(DestinationCardHand hand) {
        this.hand = hand;
    }
}

