package no.auke.drone.dto;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

/**
 * @author thaihuynh
 */
@Path("/ui")
@Component
@Produces(MediaType.APPLICATION_JSON)
public class AukeResource {

    @GET
    @Path("/{filename}")
    public JsonNode getJson(@PathParam(value = "filename") String filename) throws IOException {
        JsonFactory factory = new JsonFactory();
        factory.configure(Feature.AUTO_CLOSE_SOURCE, false).configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
                .configure(Feature.ALLOW_SINGLE_QUOTES, true);

        InputStream fileStream = getClass().getResourceAsStream("/ui/" + filename + ".json");
        JsonParser parser = factory.createJsonParser(fileStream);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(parser);
        return node;
    }

}
