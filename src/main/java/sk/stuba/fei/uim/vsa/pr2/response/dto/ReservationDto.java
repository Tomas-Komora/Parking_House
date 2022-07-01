package sk.stuba.fei.uim.vsa.pr2.response.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sk.stuba.fei.uim.vsa.pr2.entities.Car;
import sk.stuba.fei.uim.vsa.pr2.entities.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.response.CarResponse;
import sk.stuba.fei.uim.vsa.pr2.response.ParkingSpotResponse;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto extends Dto{
    private Long id;
    private String start;
    private String end;
    private int prices;
    private Long spot;
    private Long car;

}
