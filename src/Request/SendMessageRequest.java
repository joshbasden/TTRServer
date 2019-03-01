package Request;

import Model.Event;

public class SendMessageRequest implements iRequest {
    private Event data;

    public Event getData() {
        return data;
    }

    public void setData(Event data) {
        this.data = data;
    }
}
