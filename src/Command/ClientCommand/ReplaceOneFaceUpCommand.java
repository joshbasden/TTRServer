package Command.ClientCommand;

public class ReplaceOneFaceUpCommand implements iClientCommand {
    int index;
    TrainCarCard card;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public TrainCarCard getCard() {
        return card;
    }

    public void setCard(TrainCarCard card) {
        this.card = card;
    }
}
