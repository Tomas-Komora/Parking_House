package sk.stuba.fei.uim.vsa.pr2.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sk.stuba.fei.uim.vsa.pr2.entities.*;
import sk.stuba.fei.uim.vsa.pr2.factories.CarTypeFactory;
import sk.stuba.fei.uim.vsa.pr2.response.dto.CarTypeDto;
import sk.stuba.fei.uim.vsa.pr2.response.dto.ReservationDto;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarResponse {
    private Long id;
    private String brand;
    private String model;
    private String vrp;
    private String colour;
    private CarTypeDto type;
    private Long owner;
    private List<ReservationResponse> reservations;

    public CarResponse(Car car) {
        this.id = car.getId();
        this.brand = car.getBrand();
        this.model = car.getModel();
        this.vrp = car.getEcv();
        this.colour = car.getColor();
        this.type = new CarTypeFactory().transformToDto(car.getCarType());
        this.owner = car.getUser().getId();

        List<Reservation> tmp = car.getReservationList();
        List<ReservationResponse> finalList = new ArrayList<>();
        for (Reservation cpf:tmp) {
            finalList.add(new ReservationResponse(cpf));
        }
        this.reservations = finalList;
    }
}
