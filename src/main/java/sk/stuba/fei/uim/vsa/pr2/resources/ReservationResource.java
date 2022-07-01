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
import sk.stuba.fei.uim.vsa.pr2.factories.CarParkFactory;
import sk.stuba.fei.uim.vsa.pr2.factories.CarParkFloorFactory;
import sk.stuba.fei.uim.vsa.pr2.factories.ParkingSpotFactory;
import sk.stuba.fei.uim.vsa.pr2.factories.ReservationFactory;
import sk.stuba.fei.uim.vsa.pr2.response.dto.CarParkDto;
import sk.stuba.fei.uim.vsa.pr2.response.dto.ParkingSpotDto;
import sk.stuba.fei.uim.vsa.pr2.response.dto.ReservationDto;
import sk.stuba.fei.uim.vsa.pr2.response.dto.UserDto;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/reservations")
public class ReservationResource {

    public static final String EMPTY_RESPONCE = "{}";
    public static final Logger LOGGER = Logger.getLogger(CarParkResources.class.getName());

    private final ObjectMapper json = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,false);;
    private final Service service = new Service();
    private final CarParkFactory factory = new CarParkFactory();
    private final CarParkFloorFactory floorFactory = new CarParkFloorFactory();
    private final ParkingSpotFactory spotFactory = new ParkingSpotFactory();
    private final ReservationFactory reservationFactory = new ReservationFactory();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllReservations(@QueryParam("user")Long userId, @QueryParam("spot")Long spotId, @QueryParam("date")String date, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth){
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
        if((spotId!=null && date == null) || (spotId==null && date != null)){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(EMPTY_RESPONCE)
                    .build();
        }
        List<Object> reservations = new ArrayList<>();
        if(userId!= null && spotId==null){
            User user = (User) service.getUser(userId);
            if (user == null){
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(EMPTY_RESPONCE)
                        .build();
            }
            List<Car> cars = user.getCarList();
            for(Car car : cars){
                for(Reservation reservation : car.getReservationList()){
                    reservations.add(reservation);
                }
            }
        }
        else if(userId == null && spotId==null){
            List<Object> tmp = service.getUsers();
            List<User> users = tmp.stream().map(element -> (User)element).collect(Collectors.toList());
            for(User user : users){
                for(Car car : user.getCarList()){
                    for(Reservation reservation : car.getReservationList()){
                        reservations.add(reservation);
                    }
                }
            }
        }
        else if(spotId!=null && date!=null && userId==null){
            ParkingSpot parkingSpot = (ParkingSpot) service.getParkingSpot(spotId);
            if (parkingSpot == null){
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(EMPTY_RESPONCE)
                        .build();
            }

            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Date newDate = null;
            try {
                newDate = dateFormatter.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //reservations.add(service.getReservations(spotId,newDate));
            reservations = service.getReservations(spotId, newDate);
            if (service.getReservations(spotId,newDate) == null){
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();
            }



        }
        else if((spotId==null || date==null)){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(EMPTY_RESPONCE)
                    .build();
        }

        else if(userId!=null && spotId!=null){
            ParkingSpot parkingSpot = (ParkingSpot) service.getParkingSpots(spotId);
            User user = (User) service.getUser(userId);
            if ((parkingSpot == null) || (user==null)){
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(EMPTY_RESPONCE)
                        .build();
            }
            List<Reservation> tmp = parkingSpot.getReservationList();
            for(Reservation reservation: tmp){
                if ((reservation.getCar().getUser()==user)){
                    reservations.add(reservation);
                }
            }
        }else{
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(EMPTY_RESPONCE)
                    .build();
        }

        List<Reservation> reservationList = reservations.stream().map(element -> (Reservation)element).collect(Collectors.toList());
        List<ReservationDto> reservationDtos = reservationList.stream().map(reservationFactory::transformToDto).collect(Collectors.toList());
        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(json.writeValueAsString(reservationDtos))
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
    public Response getReservationById(@PathParam("id") long id, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
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
        Reservation reservation = (Reservation) service.getReservationById(id);
        if (reservation == null)
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(EMPTY_RESPONCE)
                    .build();
        ReservationDto reservationDto = reservationFactory.transformToDto(reservation);
        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(json.writeValueAsString(reservationDto))
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
    public Response createReservation(String body, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
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
            ReservationDto dto = json.readValue(body, ReservationDto.class);
            ParkingSpot parkingSpot = (ParkingSpot) service.getParkingSpot(dto.getSpot());
            Car car = (Car) service.getCar(dto.getCar());
            if (parkingSpot == null || car== null){
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();
            }

            //kontrola či je spravny user
            User tmp = (User) service.getUser(idPass);
            Car tmp1 = (Car) service.getCar(dto.getCar());
            User tmp2 = tmp1.getUser();
            if(!tmp.equals(tmp2)){
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(EMPTY_RESPONCE)
                        .build();
            }
            Reservation reservation =(Reservation) service.createReservation(dto.getSpot(), dto.getCar());
            dto = reservationFactory.transformToDto(reservation);
            if (reservation == null)
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/end")
    public Response endReservation(String body, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth, @PathParam("id") long idRes) {
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
            Reservation r = (Reservation) service.getReservationById(idRes);
            User tmp = (User) service.getUser(idPass);
            Car tmp1 = (Car) service.getCar(r.getCar().getId());
            User tmp2 = tmp1.getUser();
            if(!tmp.equals(tmp2)){
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(EMPTY_RESPONCE)
                        .build();
            }
            Reservation reservation =(Reservation) service.endReservation(idRes);
            ReservationDto reservationDto = reservationFactory.transformToDto(reservation);
            if (reservation == null) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(EMPTY_RESPONCE)
                        .build();
            }
            return Response
                    .status(Response.Status.CREATED)
                    .entity(json.writeValueAsString(reservationDto))
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
