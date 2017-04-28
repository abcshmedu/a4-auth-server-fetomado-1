package edu.hm.shareit.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.hm.shareit.models.Book;
import edu.hm.shareit.models.Copy;
import edu.hm.shareit.services.CopyService;
import edu.hm.shareit.services.ServiceResult;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CopyResourceTest extends JerseyTest {

    @Mock
    private CopyService serviceMock;
    private Book book = new Book("Die Kaenguru-Chroniken", "Marc-Uwe Kling", "978-3-548-37623-3");
    private Copy[] copies = {
            new Copy("Test User", book),
            new Copy("Egon", book),
    };

    @Override
    protected Application configure() {
        MockitoAnnotations.initMocks(this);
        return new ResourceConfig().register(new CopyResource(serviceMock));
    }

    @Test
    public void testCreateCopy() {
        when(serviceMock.addCopy(any(String.class), any(String.class))).thenReturn(ServiceResult.OK);
        ObjectNode root = new ObjectMapper().createObjectNode();
        root.put("medium", "978-3-548-37623-3");
        root.put("owner", "Test User");
        Entity<ObjectNode> json = Entity.entity(root, MediaType.APPLICATION_JSON);
        Response resp = target("copys").request().post(json);
        assertEquals(200, resp.getStatus());
        assertEquals("{\"code\":200,\"detail\":\"\"}", resp.readEntity(String.class));
    }

    @Test
    public void testGetCopys() {
        when(serviceMock.getCopys()).thenReturn(copies);
        Response resp = target("copys").request().get();
        assertEquals(200, resp.getStatus());
        assertEquals("[{\"medium\":{\"title\":\"Die Kaenguru-Chroniken\",\"author\":\"Marc-Uwe Kling\",\"isbn\":\"978-3-548-37623-3\"},\"owner\":\"Test User\",\"id\":1}," +
                "{\"medium\":{\"title\":\"Die Kaenguru-Chroniken\",\"author\":\"Marc-Uwe Kling\",\"isbn\":\"978-3-548-37623-3\"},\"owner\":\"Egon\",\"id\":2}]",
                resp.readEntity(String.class));
    }

    @Test
    public void testGetCopy() {
    }

    @Test
    public void testUpdateCopy() {
    }

}
