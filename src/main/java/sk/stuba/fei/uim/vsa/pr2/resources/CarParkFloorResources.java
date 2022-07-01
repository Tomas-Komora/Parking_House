package sk.stuba.fei.uim.vsa.pr2.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.Service;
import sk.stuba.fei.uim.vsa.pr2.entities.CarParkFloor;
import sk.stuba.fei.uim.vsa.pr2.entities.User;
import sk.stuba.fei.uim.vsa.pr2.factories.CarParkFloorFactory;
import sk.stuba.fei.uim.vsa.pr2.response.dto.CarParkFloorDto;

import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/carparkfloors")
public class CarParkFloorResources {
    public static final String EMPTY_RESPONCE = "{}";

    public static final Logger LOGGER = Logger.getLogger(CarParkFloorResources.class.getName());

    private final ObjectMapper json = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,false);
    private final Service service = new Service();
    private final CarParkFloorFactory factory = new CarParkFloorFactory();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCarParkFloorById(@PathParam("id") long id, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
        String email = null;
        Long idPass = null;
        try {
            email = getEmail(auth);
            String password = getId(auth);

            idPass = Long.parseLong(password);
        } catch (Exception e){
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(EMPTY_RESPONCE)
                    .build();
        }
        User user = (User)service.getUser(email);
        User user1 = (User) service.getUser(idPass);
        if (user == null || user1 == null){
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(EMPTY_RESPONCE)
                    .build();
        }
        if(!user.equals(user1)){
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(EMPTY_RESPONCE)
                    .build();
        }


        CarParkFloor carParkFloor = (CarParkFloor) service.getCarParkFloor(id);
        if(carParkFloor == null)
            return Response
                    .status(Response.Status.NO_CONTENT)
                    .entity(EMPTY_RESPONCE)
                    .build();
        CarParkFloorDto carParkFloorDto = factory.transformToDto(carParkFloor);
        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(json.writeValueAsString(carParkFloorDto))
                    .build();
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, null, e);
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(EMPTY_RESPONCE)
                    .build();
        }
    }

    private String getEmail(String authHeader){
        String base64Encode = authHeader.substring("Basic ".length());
        String decoded = new String(Base64.getDecoder().decode(base64Encode));
        return decoded.split(":")[0];
    }

    private String getId(String authHeader){
        String base64Encode = authHeader.substring("Basic ".length());
        String decoded = new String(Base64.getDecoder().decode(base64Encode));
        return decoded.split(":")[1];
    }
}
