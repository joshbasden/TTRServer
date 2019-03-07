package Model;

import java.util.List;

public class TrainCarCardHand implements iHand {
    private List<TrainCarCard> cards;

    public List<TrainCarCard> getCards() {
        return cards;
    }

    public void setCards(List<TrainCarCard> cards) {
        this.cards = cards;
    }
}
