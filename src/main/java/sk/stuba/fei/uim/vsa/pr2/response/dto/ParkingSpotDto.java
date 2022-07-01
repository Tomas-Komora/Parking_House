package sk.stuba.fei.uim.vsa.pr2.response.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sk.stuba.fei.uim.vsa.pr2.entities.CarType;
import sk.stuba.fei.uim.vsa.pr2.response.CarTypeResponse;
import sk.stuba.fei.uim.vsa.pr2.response.ReservationResponse;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpotDto extends Dto{
    private Long id;
    private String identifier;
    private String carParkFloor;
    private Long carPark;
    private CarTypeResponse type;
    private Boolean free;
    private List<ReservationResponse> reservations;
}
