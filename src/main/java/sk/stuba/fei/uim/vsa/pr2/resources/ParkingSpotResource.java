package sk.stuba.fei.uim.vsa.pr2.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.Service;
import sk.stuba.fei.uim.vsa.pr2.entities.CarPark;
import sk.stuba.fei.uim.vsa.pr2.entities.CarParkFloor;
import sk.stuba.fei.uim.vsa.pr2.entities.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.entities.User;
import sk.stuba.fei.uim.vsa.pr2.factories.CarParkFactory;
import sk.stuba.fei.uim.vsa.pr2.factories.CarParkFloorFactory;
import sk.stuba.fei.uim.vsa.pr2.factories.ParkingSpotFactory;
import sk.stuba.fei.uim.vsa.pr2.response.dto.CarParkDto;
import sk.stuba.fei.uim.vsa.pr2.response.dto.CarParkFloorDto;
import sk.stuba.fei.uim.vsa.pr2.response.dto.ParkingSpotDto;

import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/parkingspots")
public class ParkingSpotResource {

    public static final String EMPTY_RESPONCE = "{}";
    public static final Logger LOGGER = Logger.getLogger(CarParkResources.class.getName());

    private final ObjectMapper json = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,false);;
    private final Service service = new Service();
    private final CarParkFactory factory = new CarParkFactory();
    private final CarParkFloorFactory floorFactory = new CarParkFloorFactory();
    private final ParkingSpotFactory spotFactory = new ParkingSpotFactory();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getParkingSpot(@PathParam("id") long id, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
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
        User tmpUser = (User)service.getUser(email);
        User tmpUser1 = (User) service.getUser(idPass);
        if (tmpUser == null || tmpUser1 == null){
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(EMPTY_RESPONCE)
                    .build();
        }
        if(!tmpUser.equals(tmpUser1)){
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(EMPTY_RESPONCE)
                    .build();
        }

        ParkingSpot parkingSpot = (ParkingSpot) service.getParkingSpot(id);
        if (parkingSpot == null)
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(EMPTY_RESPONCE)
                    .build();;
        ParkingSpotDto parkingSpotDto = spotFactory.transformToDto(parkingSpot);
        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(json.writeValueAsString(parkingSpotDto))
                    .build();
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, null, e);
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(EMPTY_RESPONCE)
                    .build();
        }
    }


    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteParkingSpot(@PathParam("id") long id, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
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
        User tmpUser = (User)service.getUser(email);
        User tmpUser1 = (User) service.getUser(idPass);
        if (tmpUser == null || tmpUser1 == null){
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(EMPTY_RESPONCE)
                    .build();
        }
        if(!tmpUser.equals(tmpUser1)){
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(EMPTY_RESPONCE)
                    .build();
        }

        ParkingSpot parkingSpot = (ParkingSpot) service.getParkingSpot(id);
        if(parkingSpot == null)
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(EMPTY_RESPONCE)
                    .build();
        ParkingSpot deleteParkingSpot = (ParkingSpot) service.deleteParkingSpot(id);
        ParkingSpotDto spotDto = spotFactory.transformToDto(deleteParkingSpot);
        try {
            return Response
                    .status(Response.Status.NO_CONTENT)
                    .entity(json.writeValueAsString(""))
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
