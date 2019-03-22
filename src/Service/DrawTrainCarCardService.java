package Service;

import Model.Model;
import Request.DrawTrainCarCardRequest;
import Result.DrawTrainCarCardResult;

public class DrawTrainCarCardService {
    Model model = Model.getInstance();

    public DrawTrainCarCardResult getTopTrainCarCard(DrawTrainCarCardRequest req){
        return model.takeTopTrainCarCard(req.getUsername());
    }
}
