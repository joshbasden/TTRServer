package Model;

public class DestinationCard implements iCard {
    public DestinationCard() {}
    private int id;
    private int city1;
    private int city2;
    private int points;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCity1() {
        return city1;
    }

    public void setCity1(int city1) {
        this.city1 = city1;
    }

    public int getCity2() {
        return city2;
    }

    public void setCity2(int city2) {
        this.city2 = city2;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
