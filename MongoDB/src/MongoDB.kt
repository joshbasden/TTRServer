import Database.Database
import Database.DatabaseException
import java.util.ArrayList
import com.mongodb.MongoClient
import com.mongodb.client.MongoDatabase

class MongoDB : Database {

    private val host: String = "127.0.0.1"
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
            throw DatabaseException(e.message, e)
        }
    }

    override fun closeConnection(success: Boolean): Boolean {
        try {
            mongoClient!!.close()
            return true
        }
        catch (e : Exception) {
            println("Couldn't close connection")
            throw DatabaseException(e.message, e)
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
            println("Collection exists")
            throw DatabaseException(e.message, e)
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

    override fun getUsers(): ArrayList<String> {
        return userDAO!!.getUsers()
    }

    override fun addCommand(gameName: String?, type: String?, command: String?): Boolean {
        return commandDAO!!.addCommand(gameName, type, command)
    }

    override fun clearCommandsForGame(gameName: String?): Boolean {
        return commandDAO!!.clearCommandsForGame(gameName)
    }
}

fun main(args: Array<String>) {
//    val mongoDB = MongoDB()
//    mongoDB.openConnection()
//    mongoDB.initializeSchemas()
//    mongoDB.closeConnection(true)
//    mongoDB.openConnection()
//    mongoDB.addCommand("weirdo", "laksdjf","blah")
//    mongoDB.closeConnection(true)
//    mongoDB.openConnection()
//    mongoDB.addCommand("Dallin", "hello","blah")
//    mongoDB.closeConnection(true)
//    mongoDB.openConnection()
//    mongoDB.addCommand("Dallin", "hello","b")
//    mongoDB.closeConnection(true)
//    mongoDB.openConnection()
//    mongoDB.addCommand("Dallin", "hello","bla")
//    mongoDB.closeConnection(true)
//    mongoDB.openConnection()
//    mongoDB.addCommand("Dallin","hi", "blahasdfasdfs")
//    mongoDB.closeConnection(true)
//    mongoDB.openConnection()
//    println()
//    println(mongoDB.getCommandsForGame("Dallin").toString())
//    println()
//    mongoDB.closeConnection(true)
//    mongoDB.openConnection()
//    mongoDB.clearCommandsForGame("Dallin")
//    mongoDB.closeConnection(true)
//    mongoDB.openConnection()
//    println()
//    println(mongoDB.getCommandsForGame("weirdo").toString())
//    println()
//    mongoDB.closeConnection(true)
//    mongoDB.openConnection()
//    mongoDB.updateGame("dallinajfdlajsd", "jlasdfjlaksfjdTED!")
//    mongoDB.closeConnection(true)
//    mongoDB.openConnection()
//    mongoDB.updateGame("dadlajsd", "TED!")
//    mongoDB.closeConnection(true)
//    mongoDB.openConnection()
//    println(mongoDB.getGames().toString())
//    mongoDB.closeConnection(true)
//    mongoDB.openConnection()
//    mongoDB.addNewUser("dallin", "boop")
//    mongoDB.closeConnection(true)
}