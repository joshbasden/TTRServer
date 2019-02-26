package Request;

import java.util.List;

public class AssignDestCardsRequest implements iRequest {
    private String player;
    private List<Integer> ids;

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}