package sk.stuba.fei.uim.vsa.pr2.response.dtoInput;

import lombok.Data;
import lombok.NoArgsConstructor;
import sk.stuba.fei.uim.vsa.pr2.response.ParkingSpotResponse;
import sk.stuba.fei.uim.vsa.pr2.response.dto.Dto;

import java.util.List;
@Data
@NoArgsConstructor
public class CarParkFloorInputDto extends Dto {
    private Long id;
    private String identifier;
    private Long carPark;
    private List<ParkingSpotInputDto> spots;
}
