package Model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DestinationCardHand implements iHand {
    private List<DestinationCard> cards = new ArrayList<>();

    public DestinationCard addCardById(int id) {
        try {
            Gson gson = new Gson();
            String jsonString = new String(Files.readAllBytes(Paths.get("json/DestinationCardsWithIds.json")));
            JsonObject obj = gson.fromJson(jsonString, JsonObject.class);
            JsonArray cards = (JsonArray)obj.get("cards");
            DestinationCard card = new DestinationCard();
            JsonObject jsonCard = (JsonObject)cards.get(id);
            card.setId(id);
            card.setCity1(Integer.parseInt(jsonCard.get("city1").toString()));
            card.setCity2(Integer.parseInt(jsonCard.get("city2").toString()));
            card.setPoints(Integer.parseInt(jsonCard.get("points").toString()));
            return card;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return new DestinationCard();
    }

    public List<DestinationCard> getCards() {
        return cards;
    }

    public void setCards(List<DestinationCard> cards) {
        this.cards = cards;
    }
}
