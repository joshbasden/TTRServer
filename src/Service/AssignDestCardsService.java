package Service;

import Model.Model;
import Request.AssignDestinationCardsRequest;
import Result.AssignDestinationCardsResult;

public class AssignDestCardsService {
    Model model = Model.getInstance();
    public AssignDestinationCardsResult assignDestCards(AssignDestinationCardsRequest req) {
        return model.assignDestCards(req.getPlayer(), req.getIds());
    }
}