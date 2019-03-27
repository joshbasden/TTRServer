package Command.ClientCommand;

import Model.TrainCarCard;
import Model.iCard;

import java.util.ArrayList;

public class ReplaceAllFaceUpCommand implements iClientCommand {
    ArrayList<iCard> faceUpCards;

    public ArrayList<iCard> getFaceUpCards() {
        return faceUpCards;
    }

    public void setFaceUpCards(ArrayList<iCard> faceUpCards) {
        this.faceUpCards = faceUpCards;
    }
}
