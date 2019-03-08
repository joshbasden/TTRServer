package Request;

import Model.Event;

public class SendMessageRequest implements iRequest {
    private Event event;

    public Event getData() {
        return event;
    }

    public void setData(Event data) {
        this.event = data;
    }
}
