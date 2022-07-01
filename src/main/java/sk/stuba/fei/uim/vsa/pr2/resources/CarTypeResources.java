package sk.stuba.fei.uim.vsa.pr2.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.Service;
import sk.stuba.fei.uim.vsa.pr2.entities.CarType;
import sk.stuba.fei.uim.vsa.pr2.entities.User;
import sk.stuba.fei.uim.vsa.pr2.factories.*;
import sk.stuba.fei.uim.vsa.pr2.response.dto.CarTypeDto;
import sk.stuba.fei.uim.vsa.pr2.response.dto.UserDto;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/cartypes")
public class CarTypeResources {

    public static final String EMPTY_RESPONCE = "{}";

    public static final Logger LOGGER = Logger.getLogger(CarParkResources.class.getName());

    private final ObjectMapper json = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,false);;
    private final Service service = new Service();
    private final CarParkFactory factory = new CarParkFactory();
    private final CarParkFloorFactory floorFactory = new CarParkFloorFactory();
    private final ParkingSpotFactory spotFactory = new ParkingSpotFactory();
    private final CarTypeFactory carTypeFactory = new CarTypeFactory();
    private final UserFactory userFactory = new UserFactory();


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("name") String name, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
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


        List<Object> carTypes = new ArrayList<>();
        if(name == null)
            carTypes = service.getCarTypes();
        else {
            CarType carType = (CarType) service.getCarType(name);
            if (carType == null)
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(EMPTY_RESPONCE)
                        .build();
            carTypes.add(carType);

        }
        List<CarType> carTypes1 = carTypes.stream().map(element -> (CarType)element).collect(Collectors.toList());
        List<CarTypeDto> carTypeDtos = carTypes1.stream().map(carTypeFactory::transformToDto).collect(Collectors.toList());
        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(json.writeValueAsString(carTypeDtos))
                    .build();
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, null, e);
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(EMPTY_RESPONCE)
                    .build();
        }
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCarTypeById(@PathParam("id") long id, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
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


        CarType carType = (CarType) service.getCarType(id);
        if(carType == null)
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(EMPTY_RESPONCE)
                    .build();
        CarTypeDto carTypeDto = carTypeFactory.transformToDto(carType);
        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(json.writeValueAsString(carTypeDto))
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
    public Response deleteCarType(@PathParam("id") long id, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
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


        CarType carType = (CarType) service.getCarType(id);
        if(carType == null)
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(EMPTY_RESPONCE)
                    .build();
        CarType carType1 = (CarType) service.deleteCarType(id);
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCarType(String body, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
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


        try {
            CarTypeDto dto = json.readValue(body, CarTypeDto.class);
            CarType tmp = (CarType) service.getCarType(dto.getName());
            if (tmp != null)
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();
            CarType carType =(CarType) service.createCarType(dto.getName());
            dto = carTypeFactory.transformToDto(carType);
            return Response
                    .status(Response.Status.CREATED)
                    .entity(json.writeValueAsString(dto))
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
