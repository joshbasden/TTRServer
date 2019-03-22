package Command.ClientCommand;

import java.util.List;

public class UpdatePlayerStatsCommand implements iClientCommand {
    List<StatsChange> changes;
    String username;

    public List<StatsChange> getChanges() {
        return changes;
    }

    public void setChanges(List<StatsChange> changes) {
        this.changes = changes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
