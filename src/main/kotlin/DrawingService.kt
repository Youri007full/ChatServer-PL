import com.example.models.Player
import com.example.models.Room
import java.util.concurrent.ConcurrentHashMap

class DrawingService {

    val rooms = ConcurrentHashMap<String, Room>()
    val players = ConcurrentHashMap<String, Player>()

    fun playerJoined(player: Player) {
        players[player.clientId] = player
    }

    fun getRoomWithClientId(clientId: String): Room? {
        val filteredRooms = rooms.filterValues { room ->
            room.players.find { player ->
                player.clientId == clientId
            } != null
        }
        return if (filteredRooms.isEmpty()) {
            null
        } else {
            filteredRooms.values.toList()[0]
        }
    }

}