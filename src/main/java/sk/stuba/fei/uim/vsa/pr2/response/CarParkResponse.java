package sk.stuba.fei.uim.vsa.pr2.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sk.stuba.fei.uim.vsa.pr2.entities.CarParkFloor;
import sk.stuba.fei.uim.vsa.pr2.response.dto.CarParkFloorDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarParkResponse {
    private Long id;
    private String name;
    private String address;
    private int prices;
    private List<CarParkFloorResponce> floors;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPrices() {
        return prices;
    }

    public void setPrices(int prices) {
        this.prices = prices;
    }
}
