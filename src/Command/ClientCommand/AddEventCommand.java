package Command.ClientCommand;

import Model.Event;

public class AddEventCommand implements iClientCommand {
    private Event event;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
