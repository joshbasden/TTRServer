package Service;

import Model.Model;
import Request.AssignFirstDestinationCardsRequest;
import Result.AssignFirstDestinationCardsResult;

public class AssignFirstDestinationCardsService {
    Model model = Model.getInstance();
    public AssignFirstDestinationCardsResult assignFirstDestinationCards(AssignFirstDestinationCardsRequest req) {
        return model.assignFirstDestinationCards(req);
    }

}