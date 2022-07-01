package sk.stuba.fei.uim.vsa.pr2.resources;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.Service;
import sk.stuba.fei.uim.vsa.pr2.entities.*;
import sk.stuba.fei.uim.vsa.pr2.factories.CarParkFactory;
import sk.stuba.fei.uim.vsa.pr2.factories.CarParkFloorFactory;
import sk.stuba.fei.uim.vsa.pr2.factories.ParkingSpotFactory;
import sk.stuba.fei.uim.vsa.pr2.response.CarParkFloorResponce;
import sk.stuba.fei.uim.vsa.pr2.response.dto.CarParkDto;
import sk.stuba.fei.uim.vsa.pr2.response.dto.CarParkFloorDto;
import sk.stuba.fei.uim.vsa.pr2.response.dto.ParkingSpotDto;
import sk.stuba.fei.uim.vsa.pr2.response.dtoInput.CarParkFloorInputDto;
import sk.stuba.fei.uim.vsa.pr2.response.dtoInput.CarParkInputDto;
import sk.stuba.fei.uim.vsa.pr2.response.dtoInput.ParkingSpotInputDto;

import java.util.ArrayList;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/carparks")
public class CarParkResources {
    public static final String EMPTY_RESPONCE = "{}";

    public static final Logger LOGGER = Logger.getLogger(CarParkResources.class.getName());

    private final ObjectMapper json = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,false);
    private final Service service = new Service();
    private final CarParkFactory factory = new CarParkFactory();
    private final CarParkFloorFactory floorFactory = new CarParkFloorFactory();
    private final ParkingSpotFactory spotFactory = new ParkingSpotFactory();
   // private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,false);



    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCarParks(@QueryParam("name") String name, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
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
        List<Object> carParks = new ArrayList<>();
        if (name == null)
            carParks = service.getCarParks();
        else {
            CarPark carPark = (CarPark) service.getCarPark(name);
            if (carPark == null) {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(EMPTY_RESPONCE)
                        .build();
            }
            carParks.add(service.getCarPark(name));
        }

        List<CarPark> cp = carParks.stream().map(element -> (CarPark) element).collect(Collectors.toList());
        List<CarParkDto> carParkDtos = cp.stream().map(factory::transformToDto).collect(Collectors.toList());
        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(json.writeValueAsString(carParkDtos))
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
    public Response getCarParkById(@PathParam("id") long id, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
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


        CarPark carPark = (CarPark) service.getCarPark(id);
        if (carPark == null)
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(EMPTY_RESPONCE)
                    .build();
        CarParkDto carParkDto = factory.transformToDto(carPark);
        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(json.writeValueAsString(carParkDto))
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
    public Response createCarPark(String body, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
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

        try {
            CarParkInputDto dto = json.readValue(body, CarParkInputDto.class);
            CarPark tmp = (CarPark) service.getCarPark(dto.getName());
            if (tmp != null)
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();
            CarPark cp = (CarPark) service.createCarPark(dto.getName(), dto.getAddress(), dto.getPrices());
            if (cp == null)
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();
            // List<CarParkFloor> floors = new ArrayList<>();
            // List<ParkingSpot> spots = new ArrayList<>();
            /*if (dto.getFloors() != null) {
                for (CarParkFloorResponce carParkFloor : dto.getFloors()) {
                    floors.add((CarParkFloor) service.createCarParkFloor(cp.getId(), carParkFloor.getIdentifier()));
                    if(carParkFloor.getSpots()!=null){
                        for(ParkingSpotResponse parkingSpotResponse : carParkFloor.getSpots()) {
                            spots.add((ParkingSpot) service.createParkingSpot(dto.getId(), carParkFloor.getIdentifier(), parkingSpotResponse.getIdentifier()));
                        }
                        List<ParkingSpotResponse> parkingSpotDtos = spots.stream().map(spotFactory::transformToResponce).collect(Collectors.toList());
                        carParkFloor.setSpots(parkingSpotDtos);
                    }
                }
            }
            cp.setList_car_park_floor(floors); */
            int tmpError = 0;
            CarType carType = null;
            for(CarParkFloorInputDto carParkFloorInputDto : dto.getFloors()){
                CarParkFloor carParkFloor = (CarParkFloor) service.createCarParkFloor(cp.getId(), carParkFloorInputDto.getIdentifier());
                if(carParkFloor == null)
                    tmpError=1;
                else {
                    cp.getList_car_park_floor().add(carParkFloor);
                    if(carParkFloorInputDto.getSpots()== null)
                        carParkFloorInputDto.setSpots(new ArrayList<>());
                    else {
                        for (ParkingSpotInputDto parkingSpotInputDto : carParkFloorInputDto.getSpots()) {
                            if (parkingSpotInputDto.getType() == null) {
                                CarPark carPark = (CarPark) service.deleteCarPark(cp.getId());
                                return Response
                                                   .status(Response.Status.BAD_REQUEST)
                                                   .entity(EMPTY_RESPONCE)
                                                   .build();
                                //ParkingSpot parkingSpot = (ParkingSpot) service.createParkingSpot(cp.getId(), carParkFloorInputDto.getIdentifier(), parkingSpotInputDto.getIdentifier());
                                //if(parkingSpot==null)
                                //    tmpError=1;
                            } else {

                                if(parkingSpotInputDto.getType().getId()==null)
                                    carType = (CarType) service.createCarType(parkingSpotInputDto.getType().getName());
                                else{
                                    carType = (CarType) service.getCarType(parkingSpotInputDto.getType().getId());
                                }
                                ParkingSpot parkingSpot = null;

                                if(carType==null){
                                    CarPark carPark = (CarPark) service.deleteCarPark(cp.getId());
                                    return Response
                                            .status(Response.Status.BAD_REQUEST)
                                            .entity(EMPTY_RESPONCE)
                                            .build();
                                }else {
                                     parkingSpot = (ParkingSpot) service.createParkingSpot(cp.getId(), carParkFloorInputDto.getIdentifier(), parkingSpotInputDto.getIdentifier(), carType.getId());
                                    parkingSpot.setReservationList(new ArrayList<>());
                                }
                                if (carType == null || parkingSpot == null) {
                                    tmpError = 1;
                                }
                            }
                        }
                    }
                }
            }

            if (tmpError==1){
                CarPark carPark = (CarPark) service.deleteCarPark(cp.getId());
                CarType carType12 = (CarType) service.deleteCarType(carType.getId());
                return  Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();
            }
            //CarPark tmpCarPark =(CarPark) service.getCarPark(cp.getId());
            CarParkDto tmpCarParkDto = factory.transformToDto((CarPark) service.getCarPark(cp.getId()));
            return Response
                    .status(Response.Status.CREATED)
                    .entity(json.writeValueAsString(tmpCarParkDto))
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
    public Response deleteCarPark(@PathParam("id") long id, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
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

        CarPark carPark = (CarPark) service.getCarPark(id);
        if (carPark == null)
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(EMPTY_RESPONCE)
                    .build();
        CarPark deleteCarPark = (CarPark) service.deleteCarPark(id);
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

    @GET
    @Path("/{id}/floors")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFloors(@PathParam("id") long id, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
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

        CarPark carPark = (CarPark) service.getCarPark(id);
        if (carPark == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(EMPTY_RESPONCE)
                    .build();
        }
        List<Object> carParkFloors = service.getCarParkFloors(id);
        List<CarParkFloor> cpf = carParkFloors.stream().map(element -> (CarParkFloor) element).collect(Collectors.toList());
        List<CarParkFloorDto> carParkFloorDtos = cpf.stream().map(floorFactory::transformToDto).collect(Collectors.toList());

        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(json.writeValueAsString(carParkFloorDtos))
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
    @Path("/{id}/floors/{identifier}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCarParkFloor(@PathParam("id") long id, @PathParam("identifier") String identifier, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
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


        CarPark carPark = (CarPark) service.getCarPark(id);
        if (carPark == null)
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(EMPTY_RESPONCE)
                    .build();

        CarParkFloor carParkFloor = null;

        List<CarParkFloor> floors = carPark.getList_car_park_floor();
        for (CarParkFloor carParkFloor1 : floors) {
            if (carParkFloor1.getFloorName().equals(identifier)) {
                carParkFloor = carParkFloor1;
            }
        }

        if (carParkFloor == null)
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(EMPTY_RESPONCE)
                    .build();

        CarParkFloor deleteCarParkFloor = (CarParkFloor) service.deleteCarParkFloor(carParkFloor.getId());

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
    @Path("/{id}/floors")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCarParkFloor(@PathParam("id") long id, String body, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
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


        try {
            CarParkFloorInputDto floordto = json.readValue(body, CarParkFloorInputDto.class);
            CarPark tmp = (CarPark) service.getCarPark(id);
            if (tmp == null)
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();
            CarParkFloor cpf = (CarParkFloor) service.createCarParkFloor(id, floordto.getIdentifier());
            if (cpf == null)
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();

            int tmpError = 0;
            if(floordto.getSpots()==null){
                floordto.setSpots(new ArrayList<>());
            }else {
                for (ParkingSpotInputDto parkingSpotInputDto : floordto.getSpots()) {
                    if (parkingSpotInputDto.getType() == null) {
                        CarParkFloor floor = (CarParkFloor) service.deleteCarParkFloor(cpf.getId());
                        return Response
                               .status(Response.Status.BAD_REQUEST)
                                .entity(EMPTY_RESPONCE)
                               .build();
                        //ParkingSpot parkingSpot = (ParkingSpot) service.createParkingSpot(id, floordto.getIdentifier(), parkingSpotInputDto.getIdentifier());

                    } else {
                        CarType carType = null;
                        if(parkingSpotInputDto.getType().getId()==null)
                            carType = (CarType) service.createCarType(parkingSpotInputDto.getType().getName());
                        else{
                            carType= (CarType) service.getCarType(parkingSpotInputDto.getType().getId());
                        }
                        if(carType==null){
                            CarParkFloor tmpE = (CarParkFloor) service.deleteCarParkFloor(cpf.getId());
                            return Response
                                    .status(Response.Status.BAD_REQUEST)
                                    .entity(EMPTY_RESPONCE)
                                    .build();

                        }
                        ParkingSpot parkingSpot = (ParkingSpot) service.createParkingSpot(id, floordto.getIdentifier(), parkingSpotInputDto.getIdentifier(), carType.getId());
                        if (carType == null || parkingSpot == null) {
                            tmpError = 1;
                        }
                    }
                }
            }


            if(tmpError==1){
                CarParkFloor tmpE = (CarParkFloor) service.deleteCarParkFloor(cpf.getId());
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();
            }
            CarParkFloorDto returnedDto = floorFactory.transformToDto((CarParkFloor) service.getCarParkFloor(cpf.getId()));
            return Response
                    .status(Response.Status.CREATED)
                    .entity(json.writeValueAsString(returnedDto))
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
    @Path("/{id}/floors/{identifier}/spots")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getParkingSpot(@PathParam("id") long id, @PathParam("identifier") String identifier, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
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

        try {
            CarPark tmp = (CarPark) service.getCarPark(id);
            if (tmp == null)
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(EMPTY_RESPONCE)
                        .build();

            List<CarParkFloor> floors = tmp.getList_car_park_floor();
            CarParkFloor tmp1 = null;
            for (CarParkFloor carParkFloor : floors) {
                if (carParkFloor.getFloorName().equals(identifier))
                    tmp1 = carParkFloor;
            }
            if (tmp1 == null) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();
            }


            List<Object> spots = service.getParkingSpots(id, identifier);
            List<ParkingSpot> ps = spots.stream().map(element -> (ParkingSpot) element).collect(Collectors.toList());
            List<ParkingSpotDto> parkingSpotDtos = ps.stream().map(spotFactory::transformToDto).collect(Collectors.toList());
            return Response
                    .status(Response.Status.OK)
                    .entity(json.writeValueAsString(parkingSpotDtos))
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
    @Path("{id}/floors/{identifier}/spots")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createParkingSpot(@PathParam("id") long id, @PathParam("identifier") String identifier, String body, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
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

        try {

            ParkingSpotInputDto spotDto = json.readValue(body, ParkingSpotInputDto.class);
            CarPark tmp = (CarPark) service.getCarPark(id);
            if (tmp == null)
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();
            ParkingSpot ps = null;
            int tmpError = 0;
            if(spotDto.getType()==null) {
                return Response
                                                            .status(Response.Status.BAD_REQUEST)
                                                            .entity(EMPTY_RESPONCE)
                                                            .build();
                //ps = (ParkingSpot) service.createParkingSpot(id, identifier, spotDto.getIdentifier());
            }else{
                CarType carType = null;
                if(spotDto.getType().getId()==null) {
                    CarType tmp1 = (CarType) service.getCarType(spotDto.getType().getName());
                    if(tmp1!=null){
                        return Response
                                .status(Response.Status.BAD_REQUEST)
                                .entity(EMPTY_RESPONCE)
                                .build();
                    }
                    carType = (CarType) service.createCarType(spotDto.getType().getName());
                    if(carType == null){
                        return Response
                                .status(Response.Status.BAD_REQUEST)
                                .entity(EMPTY_RESPONCE)
                                .build();
                    }
                }else {
                    carType = (CarType) service.getCarType(spotDto.getType().getId());
                    if(carType == null){
                        return Response
                                .status(Response.Status.BAD_REQUEST)
                                .entity(EMPTY_RESPONCE)
                                .build();
                    }
                }
                ps = (ParkingSpot) service.createParkingSpot(id,identifier,spotDto.getIdentifier(), carType.getId());
                if(ps == null){
                    tmpError = 1;
                }

            }
            if(tmpError == 1){
                ParkingSpot spot = (ParkingSpot) service.deleteParkingSpot(ps.getId());
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();
            }
            if (ps == null)
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();
            ParkingSpotDto parkingSpotDto = spotFactory.transformToDto((ParkingSpot) service.getParkingSpot(ps.getId()));
            return Response
                    .status(Response.Status.CREATED)
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


    @GET
    @Path("/{id}/spots")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSpots(@PathParam("id") long id, @QueryParam("free") Boolean free, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
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

        if (free == null) {
            CarPark carPark = (CarPark) service.getCarPark(id);
            if (carPark == null)
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(EMPTY_RESPONCE)
                        .build();

            List<ParkingSpot> parkingSpots = new ArrayList<>();
            List<Object> carParkFloors = service.getCarParkFloors(id);
            List<CarParkFloor> cpf = carParkFloors.stream().map(element -> (CarParkFloor) element).collect(Collectors.toList());
            for (CarParkFloor c : cpf) {
                for (ParkingSpot ps : c.getParkingSpotList()) {
                    parkingSpots.add(ps);
                }
            }
            List<ParkingSpotDto> parkingSpotDtos = parkingSpots.stream().map(spotFactory::transformToDto).collect(Collectors.toList());

            try {
                return Response
                        .status(Response.Status.OK)
                        .entity(json.writeValueAsString(parkingSpotDtos))
                        .build();
            } catch (JsonProcessingException e) {
                LOGGER.log(Level.SEVERE, null, e);
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();
            }
        }
        if (free == true) {
            CarPark carPark = (CarPark) service.getCarPark(id);
            if (carPark == null)
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();

            List<ParkingSpot> parkingSpots = new ArrayList<>();
            Map<String, List<Object>> map = service.getAvailableParkingSpots(carPark.getName());

            for (Map.Entry<String, List<Object>> entry : map.entrySet()) {
                for (Object ps : entry.getValue()) {
                    parkingSpots.add((ParkingSpot) ps);
                }
            }

            List<ParkingSpotDto> parkingSpotDtos = parkingSpots.stream().map(spotFactory::transformToDto).collect(Collectors.toList());

            try {
                return Response
                        .status(Response.Status.OK)
                        .entity(json.writeValueAsString(parkingSpotDtos))
                        .build();
            } catch (JsonProcessingException e) {
                LOGGER.log(Level.SEVERE, null, e);
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();
            }

        }
        if (free == false) {
            CarPark carPark = (CarPark) service.getCarPark(id);
            if (carPark == null)
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();

            List<ParkingSpot> parkingSpots = new ArrayList<>();
            Map<String, List<Object>> map = service.getOccupiedParkingSpots(carPark.getName());

            for (Map.Entry<String, List<Object>> entry : map.entrySet()) {
                for (Object ps : entry.getValue()) {
                    parkingSpots.add((ParkingSpot) ps);
                }
            }

            List<ParkingSpotDto> parkingSpotDtos = parkingSpots.stream().map(spotFactory::transformToDto).collect(Collectors.toList());

            try {
                return Response
                        .status(Response.Status.OK)
                        .entity(json.writeValueAsString(parkingSpotDtos))
                        .build();
            } catch (JsonProcessingException e) {
                LOGGER.log(Level.SEVERE, null, e);
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();
            }
        }
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(EMPTY_RESPONCE)
                .build();
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