package Command.ClientCommand;

/**
 * Created by jbasden on 1/29/19.
 */

public class RemoveGameCommand implements iClientCommand {
    private String gameName;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

}
