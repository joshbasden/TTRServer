package Service;

import Model.Model;
import Request.DrawDestinationCardsRequest;
import Result.DrawDestinationCardsResult;

public class DrawDestinationCardsService {
    Model model = Model.getInstance();
    public DrawDestinationCardsResult drawDestinationCards(DrawDestinationCardsRequest req) {
        return model.drawDestinationCards(req.getUsername());
    }
}