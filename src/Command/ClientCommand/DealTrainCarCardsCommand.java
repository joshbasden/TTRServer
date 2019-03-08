package Command.ClientCommand;

import Model.TrainCarCard;
import Model.TrainCarCardHand;

import java.util.List;

public class DealTrainCarCardsCommand implements iClientCommand {
    private TrainCarCardHand hand;

    public TrainCarCardHand getHand() {
        return hand;
    }

    public void setHand(TrainCarCardHand hand) {
        this.hand = hand;
    }
}
