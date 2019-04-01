package Service;

import Model.Model;
import Request.ClaimGrayRequest;
import Request.ClaimRouteRequest;
import Result.ClaimGrayResult;
import Result.ClaimRouteResult;

public class ClaimGrayService {
    Model model = Model.getInstance();
    public ClaimGrayResult claimGray(ClaimGrayRequest req){
        ClaimRouteRequest temporaryRequest = new ClaimRouteRequest();
        temporaryRequest.setId(req.getId());
        temporaryRequest.setUsername(req.getUsername());
        ClaimRouteResult temporaryResult = model.claimRoute(temporaryRequest, req.getColor());
        ClaimGrayResult claimGrayResult = new ClaimGrayResult();
        claimGrayResult.setErrorMessage(temporaryResult.getErrorMessage());
        claimGrayResult.setSuccess(temporaryResult.getSuccess());
        claimGrayResult.setId(req.getId());
        claimGrayResult.setColor(req.getColor());
        return claimGrayResult;
    }
}
