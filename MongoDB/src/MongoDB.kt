import Database.Database
import java.util.ArrayList
import com.mongodb.MongoClient
import com.mongodb.client.MongoDatabase

class MongoDB : Database {

    private val host: String = "localhost"
    private val port: Int = 27017

    private var userDAO : UserDAO? = null
    private var gameDAO : GameDAO? = null
    private var commandDAO : CommandDAO? = null

    private var mongoClient : MongoClient? = null

    override fun openConnection(): Boolean {
        try {
            mongoClient = MongoClient(host, port)
            userDAO = UserDAO(mongoClient!!)
            gameDAO = GameDAO(mongoClient!!)
            commandDAO = CommandDAO(mongoClient!!)
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

    override fun getCommandsLength(gameName: String?): Int {
        return commandDAO!!.getCommandsLength(gameName)
    }

    override fun verifyPassword(username: String?, password: String?): Boolean {
        return userDAO!!.verifyPassword(username, password)
    }

    override fun getCommandsForGame(gameName: String?): ArrayList<String>? {
        return commandDAO!!.getCommandsForGame(gameName)
    }

    override fun addNewUser(username: String?, password: String?): Boolean {
        return userDAO!!.addNewUser(username, password)
    }

    override fun getGames(): ArrayList<String>? {
        return gameDAO!!.getGames()
    }

    override fun updateGame(gameName: String?, game: String?): Boolean {
        return gameDAO!!.updateGame(gameName, game)
    }

    override fun addCommand(gameName: String?, command: String?): Boolean {
        return commandDAO!!.addCommand(gameName, command)
    }

    override fun clearCommandsForGame(gameName: String?): Boolean {
        return commandDAO!!.clearCommandsForGame(gameName)
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