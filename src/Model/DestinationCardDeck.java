package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DestinationCardDeck implements iDeck {
    private List<iCard> cards = new ArrayList<>();

    public iCard draw() {
        int numRemaining = cards.size();
        Random rand = new Random();
        int randomCardIndex = rand.nextInt(numRemaining);
        DestinationCard card = (DestinationCard)cards.get(randomCardIndex);
        cards.remove(randomCardIndex);
        return card;
    }

    public List<iCard> getCards() {
        return cards;
    }

    public void setCards(List<iCard> cards) {
        this.cards = cards;
    }
}
