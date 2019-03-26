package Model;

public class Route {
    private int id;
    private City city1;
    private City city2;
    private int numTracks;
    private int points;
    private RouteColor color;
    private TrainCarCardType claimedType;
    private String owner = "";
  
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public City getCity1() {
        return city1;
    }

    public void setCity1(City city1) {
        this.city1 = city1;
    }

    public City getCity2() {
        return city2;
    }

    public void setCity2(City city2) {
        this.city2 = city2;
    }

    public int getNumTracks() {
        return numTracks;
    }

    public void setNumTracks(int numTracks) {
        this.numTracks = numTracks;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public RouteColor getColor() {
        return color;
    }

    public void setColor(RouteColor color) {
        this.color = color;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public TrainCarCardType getClaimedType() {
        return claimedType;
    }

    public void setClaimedType(TrainCarCardType claimedType) {
        this.claimedType = claimedType;
    }
}
