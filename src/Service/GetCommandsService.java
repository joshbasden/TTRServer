package Service;

import Model.Model;
import Request.GetCommandsRequest;
import Result.GetCommandsResult;

/**
 * Created by jbasden on 1/29/19.
 */

public class GetCommandsService {
    Model model = Model.getInstance();
    public GetCommandsResult getCommands(GetCommandsRequest req) {
        return model.getCommands(req.getUserName());
    }
}
