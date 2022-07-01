package sk.stuba.fei.uim.vsa.pr2.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sk.stuba.fei.uim.vsa.pr2.entities.Car;
import sk.stuba.fei.uim.vsa.pr2.entities.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.entities.Reservation;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
public class ReservationResponse {
    private Long id;
    private String start;
    private String end;
    private int prices;
    private Long spot;
    private Long car;

    public ReservationResponse(Reservation reservation) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.id = reservation.getId();
        //this.start = reservation.getStartDate();
        this.start = format.format(reservation.getStartDate());
        //this.end = reservation.getEndDate();
        if (reservation.getEndDate()!= null)
            this.end = format.format(reservation.getEndDate());
        this.prices = reservation.getCena();
        if(reservation.getParkingSpot()!=null)
            this.spot = reservation.getParkingSpot().getId();
        if(reservation.getCar()!=null)
            this.car = reservation.getCar().getId();
    }

}
