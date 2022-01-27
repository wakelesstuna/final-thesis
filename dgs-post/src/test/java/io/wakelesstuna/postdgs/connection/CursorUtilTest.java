package io.wakelesstuna.postdgs.connection;

import graphql.relay.ConnectionCursor;
import graphql.relay.DefaultEdge;
import graphql.relay.Edge;
import io.wakelesstuna.post.generated.types.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is test class for {@link CursorUtil}
 *
 * @author oscar.steen.forss
 */
@ExtendWith(MockitoExtension.class)
class CursorUtilTest {

    CursorUtil cursorUtil;
    List<Edge<Post>> edges;

    UUID uuidOne = UUID.fromString("0226fdea-c1ff-47b3-afd5-f47d9cc2327e");
    UUID uuidTwo = UUID.fromString("473eea09-69f7-40bf-bea0-f3380e926ecd");

    @BeforeEach
    void setUp() {
        cursorUtil = new CursorUtil();
        edges = Arrays.asList(
                new DefaultEdge<>(Post.newBuilder().id(uuidOne).build(), cursorUtil.createCursorWith(uuidOne)),
                new DefaultEdge<>(Post.newBuilder().id(uuidTwo).build(), cursorUtil.createCursorWith(uuidTwo))
        );
    }

    @Test
    void shouldCreateAConnectionCursorFromUUID() {
        String expected = Base64.getEncoder().encodeToString(uuidOne.toString().getBytes(StandardCharsets.UTF_8));
        ConnectionCursor cursor = cursorUtil.createCursorWith(uuidOne);

        assertEquals(expected,cursor.getValue());
    }

    @Test
    void shouldDecodeConnectionCursorIntoUUID() {
        UUID expected = UUID.fromString("0226fdea-c1ff-47b3-afd5-f47d9cc2327e");
        String cursor = Base64.getEncoder().encodeToString(uuidOne.toString().getBytes(StandardCharsets.UTF_8));
        UUID decodedValue = cursorUtil.decode(cursor);

        assertEquals(expected, decodedValue);
    }

    @Test
    void shouldReturnNullWhenListIsEmpty() {
        ConnectionCursor cursor = cursorUtil.getFirstCursorFrom(new ArrayList<>());

        assertNull(cursor);
    }

    @Test
    void shouldReturnCursorFromFirstElement() {
        ConnectionCursor cursor = cursorUtil.getFirstCursorFrom(edges);
        String expectedCursor = Base64.getEncoder().encodeToString(uuidOne.toString().getBytes(StandardCharsets.UTF_8));

        assertNotNull(cursor);
        assertEquals(expectedCursor, cursor.getValue());
    }

    @Test
    void shouldReturnCursorFromLastElement() {
        ConnectionCursor cursor = cursorUtil.getLastCursorFrom(edges);
        String expectedCursor = Base64.getEncoder().encodeToString(uuidTwo.toString().getBytes(StandardCharsets.UTF_8));

        assertNotNull(cursor);
        assertEquals(expectedCursor, cursor.getValue());
    }
}