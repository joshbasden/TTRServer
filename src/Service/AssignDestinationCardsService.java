package Service;

import Model.Model;
import Request.AssignDestinationCardsRequest;
import Result.AssignDestinationCardsResult;

public class AssignDestinationCardsService {
    Model model = Model.getInstance();
    public AssignDestinationCardsResult assignDestinationCards(AssignDestinationCardsRequest req) {
        return model.assignDestinationCards(req.getPlayer(), req.getIds());
    }
}