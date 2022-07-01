package sk.stuba.fei.uim.vsa.pr2.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.Service;
import sk.stuba.fei.uim.vsa.pr2.entities.*;
import sk.stuba.fei.uim.vsa.pr2.factories.*;
import sk.stuba.fei.uim.vsa.pr2.response.CarParkFloorResponce;
import sk.stuba.fei.uim.vsa.pr2.response.CarResponse;
import sk.stuba.fei.uim.vsa.pr2.response.dto.CarDto;
import sk.stuba.fei.uim.vsa.pr2.response.dto.CarParkDto;
import sk.stuba.fei.uim.vsa.pr2.response.dto.CarTypeDto;
import sk.stuba.fei.uim.vsa.pr2.response.dto.UserDto;
import sk.stuba.fei.uim.vsa.pr2.response.dtoInput.CarInputDto;
import sk.stuba.fei.uim.vsa.pr2.response.dtoInput.UserInputDto;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/users")
public class UserResources {
    public static final String EMPTY_RESPONCE = "{}";

    public static final Logger LOGGER = Logger.getLogger(CarParkResources.class.getName());

    private final ObjectMapper json = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,false);;
    private final Service service = new Service();
    private final CarParkFactory factory = new CarParkFactory();
    private final CarParkFloorFactory floorFactory = new CarParkFloorFactory();
    private final ParkingSpotFactory spotFactory = new ParkingSpotFactory();
    private final CarFactory carFactory = new CarFactory();
    private final UserFactory userFactory = new UserFactory();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("email") String email, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
        String tmpEmail = null;
        Long idPass = null;
        try {
            tmpEmail = getEmail(auth);
            String password = getId(auth);

            idPass = Long.parseLong(password);
        } catch (Exception e){
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(EMPTY_RESPONCE)
                    .build();
        }
        User tmpUser = (User)service.getUser(tmpEmail);
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
        List<Object> usersTmp = new ArrayList<>();
        if(email == null){
            usersTmp = service.getUsers();
        }else {
            User user = (User) service.getUser(email);
            if (user == null){
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(EMPTY_RESPONCE)
                        .build();
            }
            usersTmp.add(user);
        }
        List<User> users = usersTmp.stream().map(element -> (User)element).collect(Collectors.toList());
        List<UserDto> usersDtos = users.stream().map(userFactory::transformToDto).collect(Collectors.toList());
        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(json.writeValueAsString(usersDtos))
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
    public Response getUserById(@PathParam("id") long id, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
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
        User user = (User) service.getUser(id);
        if(user == null)
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(EMPTY_RESPONCE)
                    .build();
        UserDto userDto = userFactory.transformToDto(user);
        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(json.writeValueAsString(userDto))
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
    public Response deleteUser(@PathParam("id") long id, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {

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
        User user = (User) service.getUser(id);
        if(user == null)
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(EMPTY_RESPONCE)
                    .build();
        User user1 = (User) service.deleteUser(id);
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

   /* @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(String body) {
        try {
            UserDto dto = json.readValue(body, UserDto.class);
            User tmp = (User) service.getUser(dto.getEmail());
            if (tmp != null)
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();
            User user =(User) service.createUser(dto.getFirstName(),dto.getLastName(),dto.getEmail());
            Long userId = user.getId();
            if (user == null)
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();

            List<Car> cars = new ArrayList<>();
            if(dto.getCars()!= null){
                for (CarResponse carResponse : dto.getCars()){
                    cars.add( (Car) service.createCar(dto.getId(), carResponse.getBrand(),carResponse.getModel(),carResponse.getColour(),carResponse.getVrp()));
                    carResponse.setType((CarTypeDto) service.createCarType(carResponse.getType().getName()));
                }
            }
            user.setCarList(cars);


            UserDto dto1 = userFactory.transformToDto((User) service.getUser(userId));
            return Response
                    .status(Response.Status.CREATED)
                    .entity(json.writeValueAsString(dto1))
                    .build();
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, null, e);
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(EMPTY_RESPONCE)
                    .build();
        }
    }
*/


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(String body, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
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
            UserInputDto dto = json.readValue(body, UserInputDto.class);
            User tmp = (User) service.getUser(dto.getEmail());
            if (tmp != null)
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();
            User user =(User) service.createUser(dto.getFirstName(),dto.getLastName(),dto.getEmail());
            Long userId = user.getId();
            if (user == null)
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();

            int tmpError = 0;
            if(dto.getCars()!= null){
                for (CarInputDto carInputDto : dto.getCars()){

                    if(carInputDto.getOwner()==null){
                        User user1 = (User) service.deleteUser(userId);
                        return Response
                                .status(Response.Status.BAD_REQUEST)
                                .entity(EMPTY_RESPONCE)
                                .build();
                    }


                    if(carInputDto.getType()==null){
                        User user1 = (User) service.deleteUser(user.getId());
                        return Response
                                .status(Response.Status.BAD_REQUEST)
                                .entity(EMPTY_RESPONCE)
                                .build();
                        //Car car = (Car) service.createCar(user.getId(),carInputDto.getBrand(), carInputDto.getModel(), carInputDto.getColour(), carInputDto.getVrp());
                        //if (car == null)
                         //   tmpError=1;
                    }else{
                        CarType carType = null;
                        if(carInputDto.getType().getId()==null) {
                            CarType tmp2 = (CarType) service.getCarType(carInputDto.getType().getName());
                            if(tmp2!=null){
                                User tmp3 = (User) service.deleteUser(user.getId());
                                return Response
                                        .status(Response.Status.BAD_REQUEST)
                                        .entity(EMPTY_RESPONCE)
                                        .build();
                            }
                            carType = (CarType) service.createCarType(carInputDto.getType().getName());
                        }else {
                            carType = (CarType) service.getCarType(carInputDto.getType().getId());
                            if (carType==null){
                                return Response
                                        .status(Response.Status.BAD_REQUEST)
                                        .entity(EMPTY_RESPONCE)
                                        .build();
                            }
                        }
                        Car car = (Car) service.createCar(user.getId(),carInputDto.getBrand(), carInputDto.getModel(), carInputDto.getColour(), carInputDto.getVrp(),carType.getId());
                        if (car == null || carType == null)
                            tmpError=1;
                    }
                }
            }else {
                dto.setCars(new ArrayList<>());
            }

            if (tmpError == 1) {
                User tmperror = (User) service.deleteUser(user.getId());
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();
            }
            UserDto dto1 = userFactory.transformToDto((User) service.getUser(userId));
            return Response
                    .status(Response.Status.CREATED)
                    .entity(json.writeValueAsString(dto1))
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
