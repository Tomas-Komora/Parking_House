package sk.stuba.fei.uim.vsa.pr2.response.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import sk.stuba.fei.uim.vsa.pr2.entities.CarParkFloor;
import sk.stuba.fei.uim.vsa.pr2.response.CarParkFloorResponce;
import sk.stuba.fei.uim.vsa.pr2.response.dto.Dto;

import java.util.List;

@Data
@NoArgsConstructor
public class CarParkDto extends Dto {

    private Long id;
    private String name;
    private String address;
    private int prices;
    private List<CarParkFloorResponce> floors;
}
