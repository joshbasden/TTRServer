package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TrainCarCardDeck implements iDeck {
    private List<iCard> discardPile = new ArrayList<>();
    private List<iCard> drawPile = new ArrayList<>();
    private List<iCard> faceUpCards = new ArrayList<>();

    public iCard draw() {
        int numRemaining = drawPile.size();
        Random rand = new Random();
        int randomCardIndex = rand.nextInt(numRemaining);
        TrainCarCard card = (TrainCarCard) drawPile.get(randomCardIndex);
        drawPile.remove(randomCardIndex);
        return card;
    }

    public void addCard(TrainCarCard card) {
        drawPile.add(card);
    }

    public void addToDiscardPile(RouteColor color, int numTracks) {
        //TODO: Implement
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
