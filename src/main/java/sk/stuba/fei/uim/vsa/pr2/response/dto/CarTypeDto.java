package sk.stuba.fei.uim.vsa.pr2.response.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sk.stuba.fei.uim.vsa.pr2.entities.CarType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarTypeDto  extends Dto{
    private Long id;
    private String name;

    public CarTypeDto(CarType carType) {
        this.id = carType.getId();
        this.name = carType.getTypeName();
    }
}
