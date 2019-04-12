import Database.Database
import java.util.ArrayList
import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.UpdateOptions
import org.bson.Document

class MongoDB : Database {

    private val host: String = "localhost"
    private val port: Int = 27017

    private val userDAO : UserDAO? = null
    private val gameDAO : GameDAO? = null
    private val commandDAO : CommandDAO? = null

    private var mongoClient : MongoClient? = null

    override fun openConnection(): Boolean {
        try {
            mongoClient = MongoClient(host, port)
            return true
        }
        catch (e : Exception) {
            println("Couldn't open connection")
            return false
        }
    }

    override fun closeConnection(): Boolean {
        try {
            mongoClient!!.close()
            return true
        }
        catch (e : Exception) {
            println("Couldn't close connection")
            return false
        }
    }

    override fun getCommandsLength(gameName: String?): Int {
        try {
            println("Trying to get commands for game $gameName")
            val db: MongoDatabase = mongoClient!!.getDatabase("TTR")
            val collection: MongoCollection<Document> = db.getCollection("Commands")
            val doc = Document("gameName", gameName)

            val commands = collection.find(doc).into(ArrayList<Document>())

            return commands.size
        }
        catch(e: Exception) {
            println("Couldn't get these commands")
            return -1
        }
    }

    override fun verifyPassword(username: String?, password: String?): Boolean {
        try {
            println("Trying to verify password for $username")
            val db = mongoClient!!.getDatabase("TTR")
            val collection = db.getCollection("Users")

            val doc = Document("username", username)

            val thisDoc = collection.find(doc).into(ArrayList<Document>())

            return thisDoc[0]["password"] == password
        }
        catch (e: Exception) {
            println("Verifying password didn't work")
            return false
        }
    }

    override fun getCommandsForGame(gameName: String?): ArrayList<String>? {
        try {
            println("Trying to get commands for game $gameName")
            val db: MongoDatabase = mongoClient!!.getDatabase("TTR")
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
            return null
        }
    }

    override fun addNewUser(username: String?, password: String?): Boolean {
        try {
            println("Adding user: $username")
            val db: MongoDatabase = mongoClient!!.getDatabase("TTR")
            val collection: MongoCollection<Document> = db.getCollection("Users")
            val doc = Document("username", username)
            doc.append("password", password)

            println("Trying to find if the user is here")
            val myDoc = collection.find(doc).first()

            if (myDoc == null) {
                collection.insertOne(doc)
                return true
            }
            else {
                return false
            }
        }
        catch (e: Exception) {
            println("Adding new user failed")
            return false
        }
    }

    override fun getGames(): ArrayList<String>? {
        try {
            println("Getting the games")
            val db: MongoDatabase = mongoClient!!.getDatabase("TTR")
            val collection: MongoCollection<Document> = db.getCollection("Games")

            val collector = collection.find().into(ArrayList<Document>())
            val returner = ArrayList<String>()

            for (c in collector) {
                returner.add(c["game"].toString())
            }

            return returner
        }
        catch (e: Exception) {
            println("Getting games failed")
            return null
        }
    }

    override fun updateGame(gameName: String?, game: String?): Boolean {
        try {
            println("Updating the game: $gameName")
            val db: MongoDatabase = mongoClient!!.getDatabase("TTR")
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
            return false
        }
    }

    override fun addCommand(gameName: String?, command: String?): Boolean {
        try {
            println("Adding command: $command")
            val db: MongoDatabase = mongoClient!!.getDatabase("TTR")
            val collection: MongoCollection<Document> = db.getCollection("Commands")
            val doc = Document("gameName", gameName)
            doc.append("command", command)

            collection.insertOne(doc)

            return true
        }
        catch (e: Exception) {
            println("Adding command failed")
            return false
        }
    }

    override fun clearCommandsForGame(gameName: String?): Boolean {
        try {
            val db = mongoClient!!.getDatabase("TTR")
            val col = db.getCollection("Commands")

            val doc = Document("gameName", gameName)
            col.deleteMany(doc)

            return true
        }
        catch (e: Exception) {
            println("Clearing for game $gameName didn't work")

            return false
        }
    }

    override fun initializeSchemas(): Boolean {
        try {
            val db: MongoDatabase = mongoClient!!.getDatabase("TTR")
            db.createCollection("Users")
            db.createCollection("Games")
            db.createCollection("Commands")

            return true
        }
        catch (e : Exception) {
            println("A problem in initializing the schemas!")
            return false
        }
    }
}

fun main(args: Array<String>) {
    val mongoDB = MongoDB()
    mongoDB.openConnection()
    mongoDB.initializeSchemas()
    mongoDB.closeConnection()
    mongoDB.openConnection()
    mongoDB.addCommand("weirdo", "blah")
    mongoDB.closeConnection()
    mongoDB.openConnection()
    mongoDB.addCommand("Dallin", "blah")
    mongoDB.closeConnection()
    mongoDB.openConnection()
    mongoDB.addCommand("Dallin", "b")
    mongoDB.closeConnection()
    mongoDB.openConnection()
    mongoDB.addCommand("Dallin", "bla")
    mongoDB.closeConnection()
    mongoDB.openConnection()
    mongoDB.addCommand("Dallin", "blahasdfasdfs")
    mongoDB.closeConnection()
    mongoDB.openConnection()
    println()
    println(mongoDB.getCommandsForGame("Dallin").toString())
    println()
    mongoDB.closeConnection()
    mongoDB.openConnection()
    mongoDB.clearCommandsForGame("Dallin")
    mongoDB.closeConnection()
    mongoDB.openConnection()
    println()
    println(mongoDB.getCommandsForGame("weirdo").toString())
    println()
    mongoDB.closeConnection()
    mongoDB.openConnection()
    mongoDB.updateGame("dallinajfdlajsd", "jlasdfjlaksfjdTED!")
    mongoDB.closeConnection()
    mongoDB.openConnection()
    mongoDB.updateGame("dadlajsd", "TED!")
    mongoDB.closeConnection()
    mongoDB.openConnection()
    println(mongoDB.getGames().toString())
    mongoDB.closeConnection()
    mongoDB.openConnection()
    mongoDB.addNewUser("dallin", "boop")
    mongoDB.closeConnection()
}