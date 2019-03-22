package Command.ClientCommand;

import Model.TrainCarCard;
import Model.iCard;

public class ReplaceOneFaceUpCommand implements iClientCommand {
    int index;
    iCard card;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public iCard getCard() {
        return card;
    }

    public void setCard(iCard card) {
        this.card = card;
    }
}
