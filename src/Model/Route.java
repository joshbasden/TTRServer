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
    private Double lat1;
    private Double lon1;
    private Double lat2;
    private Double lon2;
    private int companionId;
  
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

    public Double getLat1() {
        return lat1;
    }

    public void setLat1(Double lat1) {
        this.lat1 = lat1;
    }

    public Double getLon1() {
        return lon1;
    }

    public void setLon1(Double lon1) {
        this.lon1 = lon1;
    }

    public Double getLat2() {
        return lat2;
    }

    public void setLat2(Double lat2) {
        this.lat2 = lat2;
    }

    public Double getLon2() {
        return lon2;
    }

    public void setLon2(Double lon2) {
        this.lon2 = lon2;
    }

    public int getCompanionId() {
        return companionId;
    }

    public void setCompanionId(int companionId) {
        this.companionId = companionId;
    }
}
