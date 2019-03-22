package Service;

import Model.Model;
import Request.EndTurnRequest;
import Request.iRequest;
import Result.EndTurnResult;

public class EndTurnService {
    Model model = Model.getInstance();
    public EndTurnResult endTurn(EndTurnRequest request){
        return model.endTurn(request.getUsername());
    }
}
