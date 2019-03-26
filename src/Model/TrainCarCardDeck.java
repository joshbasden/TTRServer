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

    public ArrayList<iCard> drawFaceUp(int ind) {
        //make arraylist of icard to send back
        ArrayList<iCard> cards = new ArrayList<iCard>();

        iCard faceUpCard = faceUpCards.get(ind);
        iCard drawCard = draw();
        faceUpCards.set(ind, drawCard);

        //first index is the chosen face up card
        //second index is drawpile card that replaces it
        cards.add(drawCard);
        cards.add(faceUpCard);

        return cards;
    }

    public void addToDiscardPile(TrainCarCardType type, int numTracks) {
        for (int i = 0; i < numTracks; ++i) {
            TrainCarCard card = new TrainCarCard();
            card.setType(type);
            discardPile.add(card);
        }
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
