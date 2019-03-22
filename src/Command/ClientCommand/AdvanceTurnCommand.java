package Command.ClientCommand;

public class AdvanceTurnCommand implements iClientCommand {
    String username;
    Boolean lastTurn;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getLastTurn() {
        return lastTurn;
    }

    public void setLastTurn(Boolean lastTurn) {
        this.lastTurn = lastTurn;
    }
}
