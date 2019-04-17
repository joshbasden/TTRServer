import Database.DatabaseException
import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document
import java.util.ArrayList

class UserDAO(val mongoClient: MongoClient) {

    fun verifyPassword(username: String?, password: String?): Boolean {
        try {
            println("Trying to verify password for $username")
            val db = mongoClient.getDatabase("TTR")
            val collection = db.getCollection("Users")

            val doc = Document("username", username)

            val thisDoc = collection.find(doc).into(ArrayList<Document>())

            return thisDoc[0]["password"] == password
        }
        catch (e: Exception) {
            println("Verifying password didn't work")
            throw DatabaseException(e.message, e)
        }
    }

    fun addNewUser(username: String?, password: String?): Boolean {
        try {
            println("Adding user: $username")
            val db: MongoDatabase = mongoClient.getDatabase("TTR")
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
            throw DatabaseException(e.message, e)
        }
    }

    fun getUsers() : ArrayList<String> {
        try {
            println("Getting the users")
            val db: MongoDatabase = mongoClient.getDatabase("TTR")
            val collection: MongoCollection<Document> = db.getCollection("Users")

            val collector = collection.find().into(ArrayList<Document>())
            val returner = ArrayList<String>()

            for (c in collector) {
                returner.add(c["username"].toString())
                returner.add(c["password"].toString())
            }

            return returner
        } catch (e: Exception) {
            println("Getting games failed")
            throw DatabaseException(e.message, e)
        }
    }
}