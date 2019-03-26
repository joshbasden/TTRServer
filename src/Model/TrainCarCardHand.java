package Model;

import java.util.ArrayList;
import java.util.List;

public class TrainCarCardHand implements iHand {
    private List<TrainCarCard> cards = new ArrayList<>();

    public TrainCarCardType getTypeOfMaxCount() {
        int maxSoFar = 0;
        TrainCarCardType maxTypeSoFar = null;
        for (TrainCarCardType type: TrainCarCardType.values()) {
            if (type.toString().equals("LOCOMOTIVE")) {
                continue;
            }
            if (getCount(type) > maxSoFar) {
                maxSoFar = getCount(type);
                maxTypeSoFar = type;
            }
        }
        return maxTypeSoFar;
    }

    public int getMaxCount() { // Of card types besides rainbow/wild/locomotive
        int maxSoFar = 0;
        for (TrainCarCardType type: TrainCarCardType.values()) {
            if (type.toString().equals("LOCOMOTIVE")) {
                continue;
            }
            if (getCount(type) > maxSoFar) {
                maxSoFar = getCount(type);
            }
        }
        return maxSoFar;
    }

    public int getCount(TrainCarCardType type) {
        int count = 0;
        for (TrainCarCard card: cards) {
            if (card.getType() == type) {
                count += 1;
            }
        }
        return count;
    }

    public void removeCards(TrainCarCardType type, int numCards) {
        TrainCarCard card = new TrainCarCard();
        card.setType(type);
        for (int i = 0; i < numCards; ++i) {
            cards.remove(card);
        }
    }

    public List<TrainCarCard> getCards() {
        return cards;
    }

    public void setCards(List<TrainCarCard> cards) {
        this.cards = cards;
    }
    public void addCard(iCard card){
        cards.add((TrainCarCard)card);
    }
}
