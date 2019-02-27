package Command.ServerCommand;

import Request.SendMessageRequest;
import Request.iRequest;
import Result.SendMessageResult;
import Service.SendMessageService;

public class SendMessageCommand implements iServerCommand {
    private iRequest data;
    public SendMessageCommand(iRequest request) {
        data = request;
    }
    public SendMessageResult execute() {
        SendMessageService sendMessageService = new SendMessageService();
        return sendMessageService.sendMessage((SendMessageRequest)data);
    }

    public iRequest getData() {
        return data;
    }

    public void setData(iRequest data) {
        this.data = data;
    }
}
