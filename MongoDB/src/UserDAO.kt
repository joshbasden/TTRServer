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
            return false
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
            return false
        }
    }


}