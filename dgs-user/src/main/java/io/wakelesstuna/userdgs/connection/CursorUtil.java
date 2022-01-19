package io.wakelesstuna.userdgs.connection;

import com.netflix.graphql.dgs.DgsComponent;
import graphql.relay.ConnectionCursor;
import graphql.relay.DefaultConnectionCursor;
import graphql.relay.Edge;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

/**
 * This is a util class for handling cursors for pagination logic
 *
 * @author oscar.steen.forss
 */
@DgsComponent
public class CursorUtil {

    /**
     * Creates a {@link ConnectionCursor} from an UUID
     * @param id id to create cursor from
     * @return ConnectionCursor
     */
    public ConnectionCursor createCursorWith(UUID id) {
        return new DefaultConnectionCursor(
                Base64.getEncoder().encodeToString(id.toString().getBytes(StandardCharsets.UTF_8))
        );
    }

    /**
     * Decodes the cursor from a String to UUID
     * @param cursor String
     * @return UUID
     */
    public UUID decode(String cursor) {
        return UUID.fromString(new String(Base64.getDecoder().decode(cursor)));
    }

    /**
     * Get the cursor from the first element in a list of edges
     * @param edges list of objects
     * @param <T> Class of the objects
     * @return ConnectionCursor
     */
    public <T> ConnectionCursor getFirstCursorFrom(List<Edge<T>> edges) {
        return edges.isEmpty() ? null : edges.get(0).getCursor();
    }

    /**
     * Get the cursor from the last element in a list of edges
     * @param edges list of objects
     * @param <T> Class of the objects
     * @return ConnectionCursor
     */
    public <T> ConnectionCursor getLastCursorFrom(List<Edge<T>> edges) {
        return edges.isEmpty() ? null : edges.get(edges.size() - 1).getCursor();
    }
}
