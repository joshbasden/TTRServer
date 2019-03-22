package Command.ClientCommand;

public class StatsChange implements iClientCommand {
    StatsChangeType type;
    int ammount;

    public StatsChangeType getType() {
        return type;
    }

    public void setType(StatsChangeType type) {
        this.type = type;
    }

    public int getAmmount() {
        return ammount;
    }

    public void setAmmount(int ammount) {
        this.ammount = ammount;
    }
}
