package Model;

import java.util.ArrayList;
import java.util.List;

public class DestinationCardHand implements iHand {
    private List<DestinationCard> cards = new ArrayList<>();

    public void addCard(DestinationCard card) {
        cards.add(card);
    }

    public List<DestinationCard> getCards() {
        return cards;
    }

    public void setCards(List<DestinationCard> cards) {
        this.cards = cards;
    }
}
