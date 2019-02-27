package Service;

import Model.Model;
import Request.AssignDestCardsRequest;
import Result.AssignDestCardsResult;

public class AssignDestCardsService {
    Model model = Model.getInstance();
    public AssignDestCardsResult assignDestCards(AssignDestCardsRequest req) {
        return model.assignDestCards(req.getPlayer(), req.getIds());
    }
}