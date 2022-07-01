package sk.stuba.fei.uim.vsa.pr2.response.dtoInput;

import lombok.Data;
import lombok.NoArgsConstructor;
import sk.stuba.fei.uim.vsa.pr2.response.CarTypeResponse;
import sk.stuba.fei.uim.vsa.pr2.response.ReservationResponse;
import sk.stuba.fei.uim.vsa.pr2.response.dto.Dto;

import java.util.List;
@Data
@NoArgsConstructor
public class CarInputDto extends Dto {
    private Long id;
    private String brand;
    private String model;
    private String vrp;
    private String colour;
    private CarTypeInputDto type;
    private UserInputDto owner;
    private List<ReservationInputDto> reservations;
}
