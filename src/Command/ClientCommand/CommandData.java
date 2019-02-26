package Command.ClientCommand;

public class CommandData implements iClientCommand {
    private ClientCommandType type;
    private String data;

    public ClientCommandType getType() {
        return type;
    }

    public void setType(ClientCommandType type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
