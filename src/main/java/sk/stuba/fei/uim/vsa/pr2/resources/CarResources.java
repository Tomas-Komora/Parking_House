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
import sk.stuba.fei.uim.vsa.pr2.factories.CarFactory;
import sk.stuba.fei.uim.vsa.pr2.factories.CarParkFactory;
import sk.stuba.fei.uim.vsa.pr2.factories.CarParkFloorFactory;
import sk.stuba.fei.uim.vsa.pr2.factories.ParkingSpotFactory;
import sk.stuba.fei.uim.vsa.pr2.response.CarParkFloorResponce;
import sk.stuba.fei.uim.vsa.pr2.response.dto.CarDto;
import sk.stuba.fei.uim.vsa.pr2.response.dto.CarParkDto;
import sk.stuba.fei.uim.vsa.pr2.response.dtoInput.CarInputDto;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/cars")
public class CarResources {
    public static final String EMPTY_RESPONCE = "{}";

    public static final Logger LOGGER = Logger.getLogger(CarParkResources.class.getName());

    private final ObjectMapper json = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,false);;
    private final Service service = new Service();
    private final CarParkFactory factory = new CarParkFactory();
    private final CarParkFloorFactory floorFactory = new CarParkFloorFactory();
    private final ParkingSpotFactory spotFactory = new ParkingSpotFactory();
    private final CarFactory carFactory = new CarFactory();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("vrp")String vrp, @QueryParam("user")Long userID, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
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

        List<Object> user = service.getUsers();
        List<Car> cars = new ArrayList<>();
        if(vrp == null && userID == null){
            List<User> users = user.stream().map(element -> (User)element).collect(Collectors.toList());
            for (User u : users){
                for (Car car : u.getCarList()){
                    cars.add(car);
                }
            }
        }
        if(vrp != null && userID==null){
            Car car = (Car) service.getCar(vrp);
            if(car==null){
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(EMPTY_RESPONCE)
                        .build();
            }
            cars.add((Car) service.getCar(vrp));
        }
        if(vrp == null && userID !=null){
            User user1 = (User) service.getUser(userID);
            if(user1==null){
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(EMPTY_RESPONCE)
                        .build();
            }
            cars = user1.getCarList();
        }
        if(vrp != null && userID != null){
            User user1 = (User) service.getUser(userID);
            if(user1==null){
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(EMPTY_RESPONCE)
                        .build();
            }
            Car car = null;
            for(Car tmp : user1.getCarList()){
                if (tmp.getEcv().equals(vrp))
                    car = tmp;
            }
            if (car == null){
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(EMPTY_RESPONCE)
                        .build();
            }
            cars.add(car);
        }



        List<CarDto> carDtos = cars.stream().map(carFactory::transformToDto).collect(Collectors.toList());
        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(json.writeValueAsString(carDtos))
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
    public Response getCarById(@PathParam("id") long id, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
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

        Car car = (Car) service.getCar(id);
        if(car == null)
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(EMPTY_RESPONCE)
                    .build();
        CarDto carDto = carFactory.transformToDto((Car) service.getCar(car.getId()));

        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(json.writeValueAsString(carDto))
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
    public Response deleteCar(@PathParam("id") long id, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
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

        Car car = (Car) service.getCar(id);
        if(car == null)
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(EMPTY_RESPONCE)
                    .build();
        Car deleteCar = (Car) service.deleteCar(id);
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
    public Response createCar(String body, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
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
            CarInputDto dto = json.readValue(body, CarInputDto.class);
            Car tmp = (Car) service.getCar(dto.getVrp());
            if (tmp != null)
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();
            User user = null;
            if(dto.getOwner()==null){
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();
            }

            if(dto.getOwner().getId()==null) {
                User user1 = (User) service.getUser(dto.getOwner().getEmail());
                if(user1!=null){
                    return Response
                            .status(Response.Status.BAD_REQUEST)
                            .entity(EMPTY_RESPONCE)
                            .build();
                }
                user = (User) service.createUser(dto.getOwner().getFirstName(), dto.getOwner().getLastName(), dto.getOwner().getEmail());
            }else{
                user = (User) service.getUser(dto.getOwner().getId());
            }
            if(user == null){
                User user1 = (User) service.deleteUser(user.getId());
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();
            }
            Car car = null;
            if(dto.getType()!= null) {
                CarType carType = null;
                if(dto.getType().getId()==null) {
                    CarType carType1 = (CarType) service.getCarType(dto.getType().getName());
                    if(carType1!= null){
                        User user1 = (User) service.deleteUser(user.getId());
                        return Response
                                .status(Response.Status.BAD_REQUEST)
                                .entity(EMPTY_RESPONCE)
                                .build();
                    }
                    carType = (CarType) service.createCarType(dto.getType().getName());
                }else {
                    carType =(CarType) service.getCarType(dto.getType().getId());
                    if(carType == null){
                        User user1 = (User) service.deleteUser(user.getId());
                        return Response
                                .status(Response.Status.BAD_REQUEST)
                                .entity(EMPTY_RESPONCE)
                                .build();
                    }
                }
                if(carType == null){
                    CarType carType1 = (CarType) service.deleteCarType(carType.getId());
                    User user1 = (User) service.deleteUser(user.getId());
                    return Response
                            .status(Response.Status.BAD_REQUEST)
                            .entity(EMPTY_RESPONCE)
                            .build();
                }
                Car car1 = (Car) service.getCar(dto.getVrp());
                if(car1!=null){
                    User user1 = (User) service.deleteUser(user.getId());
                    return Response
                            .status(Response.Status.BAD_REQUEST)
                            .entity(EMPTY_RESPONCE)
                            .build();
                }
                car = (Car) service.createCar(user.getId(), dto.getBrand(), dto.getModel(), dto.getColour(), dto.getVrp(), carType.getId());

            }else {
                User user1 = (User) service.deleteUser(user.getId());
                return Response
                                .status(Response.Status.BAD_REQUEST)
                                .entity(EMPTY_RESPONCE)
                                .build();
                 //car = (Car) service.createCar(user.getId(), dto.getBrand(), dto.getModel(), dto.getColour(), dto.getVrp());
            }
            if(car.getReservationList()==null)
                car.setReservationList(new ArrayList<>());
            if(car == null) {
                Car car1 = (Car) service.deleteCar(car.getId());
                User user1 = (User) service.deleteUser(user.getId());
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();
            }


            CarDto output = carFactory.transformToDto((Car) service.getCar(car.getId()));
            return Response
                    .status(Response.Status.CREATED)
                    .entity(json.writeValueAsString(output))
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
