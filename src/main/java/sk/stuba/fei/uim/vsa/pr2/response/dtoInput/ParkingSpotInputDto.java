package sk.stuba.fei.uim.vsa.pr2.response.dtoInput;

import lombok.Data;
import lombok.NoArgsConstructor;
import sk.stuba.fei.uim.vsa.pr2.response.CarTypeResponse;
import sk.stuba.fei.uim.vsa.pr2.response.ReservationResponse;
import sk.stuba.fei.uim.vsa.pr2.response.dto.Dto;

import java.util.List;

@Data
@NoArgsConstructor
public class ParkingSpotInputDto extends Dto {
    private String identifier;
    private CarTypeInputDto type;
    private Boolean free;
    private List<ReservationInputDto> reservations;
}
