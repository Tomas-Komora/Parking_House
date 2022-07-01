package sk.stuba.fei.uim.vsa.pr2.response.dtoInput;

import lombok.Data;
import lombok.NoArgsConstructor;
import sk.stuba.fei.uim.vsa.pr2.response.CarParkFloorResponce;
import sk.stuba.fei.uim.vsa.pr2.response.dto.Dto;

import java.util.List;

@Data
@NoArgsConstructor
public class CarParkInputDto extends Dto {
    private String name;
    private String address;
    private int prices;
    private List<CarParkFloorInputDto> floors;
}
