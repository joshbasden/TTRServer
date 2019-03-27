package Request;

import Model.TrainCarCard;
import Model.TrainCarCardType;

public class ClaimGrayRequest implements iRequest {
    String username;
    int id;
    TrainCarCardType color;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setColor(TrainCarCardType color) {
        this.color = color;
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public TrainCarCardType getColor() {
        return color;
    }
}
