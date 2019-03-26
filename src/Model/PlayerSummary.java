package Model;

public class PlayerSummary {
    private String username;
    private int ptsFromClaimedRoutes;
    private int ptsFromDestinations;
    private int ptsReducedFromDestinations;
    private int ptsFromMostClaimedRoutes;
    private int totalPoints;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPtsFromClaimedRoutes() {
        return ptsFromClaimedRoutes;
    }

    public void setPtsFromClaimedRoutes(int ptsFromClaimedRoutes) {
        this.ptsFromClaimedRoutes = ptsFromClaimedRoutes;
    }

    public int getPtsFromDestinations() {
        return ptsFromDestinations;
    }

    public void setPtsFromDestinations(int ptsFromDestinations) {
        this.ptsFromDestinations = ptsFromDestinations;
    }

    public int getPtsReducedFromDestinations() {
        return ptsReducedFromDestinations;
    }

    public void setPtsReducedFromDestinations(int ptsReducedFromDestinations) {
        this.ptsReducedFromDestinations = ptsReducedFromDestinations;
    }

    public int getPtsFromMostClaimedRoutes() {
        return ptsFromMostClaimedRoutes;
    }

    public void setPtsFromMostClaimedRoutes(int ptsFromMostClaimedRoutes) {
        this.ptsFromMostClaimedRoutes = ptsFromMostClaimedRoutes;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }
}
