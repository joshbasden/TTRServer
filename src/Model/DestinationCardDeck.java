package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DestinationCardDeck implements iDeck {
    private List<DestinationCard> cards = new ArrayList<>();
    private List<DestinationCard> cardsTakenOut = new ArrayList<>();

    public DestinationCard draw() {
        int numRemaining = cards.size();
        Random rand = new Random();
        int randomCardIndex = rand.nextInt(numRemaining);
        DestinationCard card = (DestinationCard)cards.get(randomCardIndex);
        cards.remove(randomCardIndex);
        cardsTakenOut.add(card);
        return card;
    }

    public DestinationCard getCardById(int id) {
        DestinationCard destinationCard;
        for (iCard card: cards) {
            destinationCard = (DestinationCard)card;
            if (destinationCard.getId() == id) {
                return destinationCard;
            }
        }
        for (iCard card: cardsTakenOut) {
            destinationCard = (DestinationCard)card;
            if (destinationCard.getId() == id) {
                return destinationCard;
            }
        }
        System.out.println("ERROR: CARD WITH THAT ID WAS NOT FOUND!!!");
        return null;
    }

    public void addCard(DestinationCard card) {
        cards.add(card);
    }

    public List<DestinationCard> getCards() {
        return cards;
    }

    public void setCards(List<DestinationCard> cards) {
        this.cards = cards;
    }

    public List<DestinationCard> getCardsTakenOut() {
        return cardsTakenOut;
    }

    public void setCardsTakenOut(List<DestinationCard> cardsTakenOut) {
        this.cardsTakenOut = cardsTakenOut;
    }
}
