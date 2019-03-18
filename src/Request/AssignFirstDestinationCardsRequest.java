package Request;

import java.util.List;

public class AssignFirstDestinationCardsRequest implements iRequest {
    private String player;
    private List<Integer> chosen;
    private List<Integer> notChosen;

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public List<Integer> getChosen() {
        return chosen;
    }

    public void setChosen(List<Integer> chosen) {
        this.chosen = chosen;
    }

    public List<Integer> getNotChosen() {
        return notChosen;
    }

    public void setNotChosen(List<Integer> notChosen) {
        this.notChosen = notChosen;
    }
}
