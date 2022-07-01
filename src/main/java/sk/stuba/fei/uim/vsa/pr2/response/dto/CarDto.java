package sk.stuba.fei.uim.vsa.pr2.response.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import sk.stuba.fei.uim.vsa.pr2.entities.CarType;
import sk.stuba.fei.uim.vsa.pr2.entities.User;
import sk.stuba.fei.uim.vsa.pr2.response.CarTypeResponse;
import sk.stuba.fei.uim.vsa.pr2.response.ReservationResponse;
import sk.stuba.fei.uim.vsa.pr2.response.UserResponse;

import java.util.List;

@Data
@NoArgsConstructor
public class CarDto extends Dto{
    private Long id;
    private String brand;
    private String model;
    private String vrp;
    private String colour;
    private CarTypeResponse type;
    private Long owner;
    private List<ReservationResponse> reservations;
}
