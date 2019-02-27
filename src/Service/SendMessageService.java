package Service;

import Model.Model;
import Request.SendMessageRequest;
import Result.SendMessageResult;

public class SendMessageService {
    Model model = Model.getInstance();
    public SendMessageResult sendMessage(SendMessageRequest req) {
        SendMessageResult res = new SendMessageResult();
        if (model.sendMessage(req.getData())) {
            res.setSuccess(true);
            return res;
        }
        res.setErrorMessage("Message was not able to be sent");
        res.setSuccess(false);
        return res;
    }
}