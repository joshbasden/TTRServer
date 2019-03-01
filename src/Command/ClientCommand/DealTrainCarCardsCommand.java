package Command.ClientCommand;

import Model.TrainCarCard;

import java.util.List;

public class DealTrainCarCardsCommand implements iClientCommand {
    private List<TrainCarCard> trainCarCards;

    public List<TrainCarCard> getTrainCarCards() {
        return trainCarCards;
    }

    public void setTrainCarCards(List<TrainCarCard> trainCarCards) {
        this.trainCarCards = trainCarCards;
    }
}
