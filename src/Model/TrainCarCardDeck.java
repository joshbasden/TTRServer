package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TrainCarCardDeck implements iDeck {
    private List<TrainCarCard> discardPile = new ArrayList<>();
    private List<TrainCarCard> drawPile = new ArrayList<>();
    private List<TrainCarCard> faceUpCards = new ArrayList<>();

    public TrainCarCard draw() {
        if (drawPile.size() <= 5){
            addDiscardToTrainDeck();
        }
        int numRemaining = drawPile.size();
//        Random rand = new Random();
//        int randomCardIndex = rand.nextInt(numRemaining);
        TrainCarCard card = (TrainCarCard) drawPile.get(0);
        drawPile.remove(0);

//        if (drawPile.size() <= 5){
//            addDiscardToTrainDeck();
//        }

        return card;
    }

    public void addDiscardToTrainDeck(){
        for (TrainCarCard c: discardPile){
            addCard((TrainCarCard) c);
        }
        Collections.shuffle(drawPile);

        //clear discardPile
        discardPile.clear();
    }

    public void addCardToFaceUp(TrainCarCard card){
        faceUpCards.add(card);
    }

    public void addCard(TrainCarCard card) {
        drawPile.add(card);
    }

    public ArrayList<TrainCarCard> drawFaceUp(int ind) {
        //make arraylist of TrainCarCard to send back
        ArrayList<TrainCarCard> cards = new ArrayList<TrainCarCard>();

        TrainCarCard faceUpCard = faceUpCards.get(ind);
        TrainCarCard drawCard = draw();
        faceUpCards.set(ind, drawCard);

        //first index is the chosen face up card
        //second index is drawpile card that replaces it
        cards.add(drawCard);
        cards.add(faceUpCard);

        return cards;
    }

    public void addCardToDiscardPile(TrainCarCard card){
        discardPile.add(card);
    }

    public void clearAllFaceUp(){
        faceUpCards.clear();
    }

    public void addToDiscardPile(TrainCarCardType type, int numTracks) {
        for (int i = 0; i < numTracks; ++i) {
            TrainCarCard card = new TrainCarCard();
            card.setType(type);
            discardPile.add(card);
        }
    }

    public int getTrainDeckSize(){
        return drawPile.size();
    }

    public List<TrainCarCard> getDiscardPile() {
        return discardPile;
    }

    public void setDiscardPile(List<TrainCarCard> discardPile) {
        this.discardPile = discardPile;
    }

    public List<TrainCarCard> getDrawPile() {
        return drawPile;
    }

    public void setDrawPile(List<TrainCarCard> drawPile) {
        this.drawPile = drawPile;
    }

    public List<TrainCarCard> getFaceUpCards() {
        return faceUpCards;
    }

    public void setFaceUpCards(List<TrainCarCard> faceUpCards) {
        this.faceUpCards = faceUpCards;
    }
}
