package sk.stuba.fei.uim.vsa.pr2.response.dtoInput;

import lombok.Data;
import lombok.NoArgsConstructor;
import sk.stuba.fei.uim.vsa.pr2.response.dto.Dto;

@Data
@NoArgsConstructor
public class ReservationInputDto extends Dto {
    private Long id;
    private String start;
    private String end;
    private int prices;
    private ParkingSpotInputDto spot;
    private CarInputDto car;
}
