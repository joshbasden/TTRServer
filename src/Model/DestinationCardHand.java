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

    public void addCard(DestinationCard card) {
        cards.add(card);
    }

    public List<DestinationCard> getCards() {
        return cards;
    }

    public void setCards(List<DestinationCard> cards) {
        this.cards = cards;
    }
}
