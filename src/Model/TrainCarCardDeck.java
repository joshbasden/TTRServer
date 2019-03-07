package Model;

import java.util.List;
import java.util.Random;

public class TrainCarCardDeck implements iDeck {
    private List<iCard> discardPile;
    private List<iCard> drawPile;
    private List<iCard> faceUpCards;

    public iCard draw() {
        int numRemaining = drawPile.size();
        Random rand = new Random();
        int randomCardIndex = rand.nextInt((numRemaining) + 1); //TODO: Check bounds on this
        TrainCarCard card = (TrainCarCard) drawPile.get(randomCardIndex);
        drawPile.remove(randomCardIndex);
        return card;
    }

    public List<iCard> getDiscardPile() {
        return discardPile;
    }

    public void setDiscardPile(List<iCard> discardPile) {
        this.discardPile = discardPile;
    }

    public List<iCard> getDrawPile() {
        return drawPile;
    }

    public void setDrawPile(List<iCard> drawPile) {
        this.drawPile = drawPile;
    }

    public List<iCard> getFaceUpCards() {
        return faceUpCards;
    }

    public void setFaceUpCards(List<iCard> faceUpCards) {
        this.faceUpCards = faceUpCards;
    }
}
