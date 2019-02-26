package Request;

import Model.Chat;

public class SendMessageRequest implements iRequest {
    private Chat data;

    public Chat getData() {
        return data;
    }

    public void setData(Chat data) {
        this.data = data;
    }
}
