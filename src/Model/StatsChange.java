package Model;

import Command.ClientCommand.iClientCommand;

public class StatsChange implements iClientCommand {
    StatsChangeType type;
    int amount;

    public StatsChangeType getType() {
        return type;
    }

    public void setType(StatsChangeType type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
