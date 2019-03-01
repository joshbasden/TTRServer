package Command.ClientCommand;

import Model.DestinationCard;

import java.util.List;

public class ChooseDestinationCardsCommand implements iClientCommand {
    private List<DestinationCard> destinationCards;

    public List<DestinationCard> getDestinationCards() {
        return destinationCards;
    }

    public void setDestinationCards(List<DestinationCard> destinationCards) {
        this.destinationCards = destinationCards;
    }
}
