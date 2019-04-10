import Database.Database
import java.util.ArrayList
import com.mongodb.MongoClient

class MongoDB : Database {

    private val host: String = "127.0.0.1"
    private val port: Int = 0

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
            return false
        }
    }

    override fun closeConnection(): Boolean {
        try {
            mongoClient!!.close()
            return true
        }
        catch (e : Exception) {
            return false
        }
    }

    override fun getCommandsLength(p0: String?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun verifyPassword(p0: String?, p1: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCommandsForGame(p0: String?): ArrayList<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addNewUser(p0: String?, p1: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getGames(): ArrayList<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateGame(p0: String?, p1: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addCommand(p0: String?, p1: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearCommandsForGame(p0: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}