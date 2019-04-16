import Database.DatabaseException
import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.UpdateOptions
import org.bson.Document
import java.util.ArrayList

class GameDAO(val mongoClient: MongoClient) {

    fun getGames(): ArrayList<String>? {
        try {
            println("Getting the games")
            val db: MongoDatabase = mongoClient.getDatabase("TTR")
            val collection: MongoCollection<Document> = db.getCollection("Games")

            val collector = collection.find().into(ArrayList<Document>())
            val returner = ArrayList<String>()

            for (c in collector) {
                returner.add(c["game"].toString())
            }

            return returner
        } catch (e: Exception) {
            println("Getting games failed")
            throw DatabaseException(e.message, e)
        }
    }

    fun updateGame(gameName: String?, game: String?): Boolean {
        try {
            println("Updating the game: $gameName")
            val db: MongoDatabase = mongoClient.getDatabase("TTR")
            val collection: MongoCollection<Document> = db.getCollection("Games")

            val filter = Document("gameName", gameName)
            val doc = Document("game", game)
            val setter = Document("\$set", doc)
            val options = UpdateOptions()
            options.upsert(true)
            println(setter.toString())

            collection.updateOne(filter, setter, options)

            return true
        }
        catch (e: Exception) {
            println("Updating the game failed")
            throw DatabaseException(e.message, e)
        }
    }
}