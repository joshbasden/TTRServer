package Service;

import Model.Model;
import Request.ClaimGrayRequest;
import Result.ClaimGrayResult;

public class ClaimGrayService {
    Model model = Model.getInstance();

    public ClaimGrayResult claimGray(ClaimGrayRequest req){
        return model.claimGray(req);
    }
}
