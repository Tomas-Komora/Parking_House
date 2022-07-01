package sk.stuba.fei.uim.vsa.pr2.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import sk.stuba.fei.uim.vsa.pr2.entities.CarParkFloor;
import sk.stuba.fei.uim.vsa.pr2.entities.CarType;
import sk.stuba.fei.uim.vsa.pr2.entities.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.entities.Reservation;
import sk.stuba.fei.uim.vsa.pr2.factories.CarTypeFactory;
import sk.stuba.fei.uim.vsa.pr2.response.dto.CarTypeDto;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ParkingSpotResponse {
    private Long id;
    private String identifier;
    private String carParkFloor;
    private Long carPark;
    private CarTypeDto type;
    private Boolean free;
    private List<ReservationResponse> reservations;



    public ParkingSpotResponse(ParkingSpot parkingSpot) {
        this.id = parkingSpot.getId();
        this.identifier = parkingSpot.getSpotName();
        this.carParkFloor = parkingSpot.getCarParkFloor().getFloorName();
        this.carPark = parkingSpot.getCarParkId();
        this.type = new CarTypeFactory().transformToDto(parkingSpot.getCarType());
        this.free = parkingSpot.getReserved();

        List<Reservation> tmp = parkingSpot.getReservationList();
        List<ReservationResponse> finalList = new ArrayList<>();
        for (Reservation reservation:tmp) {
            finalList.add(new ReservationResponse(reservation));
        }

        this.reservations = finalList;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getCarParkFloor() {
        return carParkFloor;
    }

    public void setCarParkFloor(String carParkFloor) {
        this.carParkFloor = carParkFloor;
    }

    public Long getCarPark() {
        return carPark;
    }

    public void setCarPark(Long carPark) {
        this.carPark = carPark;
    }


    public Boolean getFree() {
        return free;
    }

    public void setFree(Boolean free) {
        this.free = free;
    }
}
