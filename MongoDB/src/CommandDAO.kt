import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import Database.DatabaseException
import org.bson.Document
import java.util.ArrayList

class CommandDAO(val mongoClient: MongoClient) {

    fun getCommandsLength(gameName: String?): Int {
        try {
            println("Trying to get commands for game $gameName")
            val db: MongoDatabase = mongoClient.getDatabase("TTR")
            val collection: MongoCollection<Document> = db.getCollection("Commands")
            val doc = Document("gameName", gameName)

            val commands = collection.find(doc).into(ArrayList<Document>())

            return commands.size
        }
        catch(e: Exception) {
            println("Couldn't get these commands")
            throw DatabaseException(e.message, e)
        }
    }

    fun getCommandsForGame(gameName: String?): ArrayList<String>? {
        try {
            println("Trying to get commands for game $gameName")
            val db: MongoDatabase = mongoClient.getDatabase("TTR")
            val collection: MongoCollection<Document> = db.getCollection("Commands")
            val doc = Document("gameName", gameName)

            val commands = collection.find(doc).into(ArrayList<Document>())
            val returner = ArrayList<String>()

            for (c in commands) {
                returner.add(c["command"].toString())
            }

            return returner
        }
        catch(e: Exception) {
            println("Couldn't get these commands")
            throw DatabaseException(e.message, e)
        }
    }

    fun addCommand(gameName: String?, command: String?): Boolean {
        try {
            println("Adding command: $command")
            val db: MongoDatabase = mongoClient.getDatabase("TTR")
            val collection: MongoCollection<Document> = db.getCollection("Commands")
            val doc = Document("gameName", gameName)
            doc.append("command", command)

            collection.insertOne(doc)

            return true
        }
        catch (e: Exception) {
            println("Adding command failed")
            throw DatabaseException(e.message, e)
        }
    }

    fun clearCommandsForGame(gameName: String?): Boolean {
        try {
            val db = mongoClient.getDatabase("TTR")
            val col = db.getCollection("Commands")

            val doc = Document("gameName", gameName)
            col.deleteMany(doc)

            return true
        }
        catch (e: Exception) {
            println("Clearing for game $gameName didn't work")
            throw DatabaseException(e.message, e)
        }
    }
}