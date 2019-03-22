package Model;

import java.util.ArrayList;
import java.util.List;

public class TrainCarCardHand implements iHand {
    private List<TrainCarCard> cards = new ArrayList<>();

    public List<TrainCarCard> getCards() {
        return cards;
    }

    public void setCards(List<TrainCarCard> cards) {
        this.cards = cards;
    }
    public void addCard(iCard card){
        cards.add(card);
    }
}
