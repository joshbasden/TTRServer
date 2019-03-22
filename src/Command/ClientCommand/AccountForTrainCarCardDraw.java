package Command.ClientCommand;

public class AccountForTrainCarCardDraw implements iClientCommand {
    int deckSize;

    public int getDeckSize() {
        return deckSize;
    }

    public void setDeckSize(int deckSize) {
        this.deckSize = deckSize;
    }
}
