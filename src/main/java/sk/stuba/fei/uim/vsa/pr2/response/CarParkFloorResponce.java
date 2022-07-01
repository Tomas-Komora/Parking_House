package sk.stuba.fei.uim.vsa.pr2.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sk.stuba.fei.uim.vsa.pr2.entities.CarPark;
import sk.stuba.fei.uim.vsa.pr2.entities.CarParkFloor;
import sk.stuba.fei.uim.vsa.pr2.entities.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.response.dto.CarParkDto;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CarParkFloorResponce {
    private Long id;
    private String identifier;
    private Long carPark;
    private List<ParkingSpotResponse> spots;


    public CarParkFloorResponce(CarParkFloor carParkFloor) {
        this.id = carParkFloor.getId();
        this.identifier = carParkFloor.getFloorName();
        this.carPark = carParkFloor.getCarPark().getId();
        List<ParkingSpot> tmp = carParkFloor.getParkingSpotList();
        List<ParkingSpotResponse> finalList = new ArrayList<>();
        for (ParkingSpot cpf:tmp) {
            finalList.add(new ParkingSpotResponse(cpf));
        }
        this.spots = finalList;
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

    public Long getCarPark() {
        return carPark;
    }

    public void setCarPark(Long carPark) {
        this.carPark = carPark;
    }

    public List<ParkingSpotResponse> getSpots() {
        return spots;
    }

    public void setSpots(List<ParkingSpotResponse> spots) {
        this.spots = spots;
    }
}
