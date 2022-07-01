package sk.stuba.fei.uim.vsa.pr2.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sk.stuba.fei.uim.vsa.pr2.entities.CarType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarTypeResponse {
    private Long id;
    private String name;

    public CarTypeResponse(CarType carType){
        this.id = carType.getId();
        this.name = carType.getTypeName();
    }
}
