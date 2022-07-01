package sk.stuba.fei.uim.vsa.pr2.entities;

import sk.stuba.fei.uim.vsa.pr2.Service;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity(name = "RESERVATION")
public class Reservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return this.id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;


    @ManyToOne
    private ParkingSpot parkingSpot;
    @ManyToOne
    private Car car;

    private int cena;


    public void setId(Long id) {
        this.id = id;
    }

    public int getCena() {
        return cena;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }

    public Reservation() {
    }

    public Reservation(Car car, ParkingSpot parkingSpot) {
        this.car = car;
        this.parkingSpot = parkingSpot;
    }

    public Reservation(Long car, Long parkingSpot) {
        this.car = (Car) new Service().getCar(car);
        this.parkingSpot = (ParkingSpot) new Service().getParkingSpot(parkingSpot);
    }



    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public void setParkingSpotId(Long parkingSpot) {
        this.parkingSpot.setId(parkingSpot);
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void setCarId(Long car) {
        this.car.setId(car);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, endDate);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", idCar=" + car.getId() +
                ", idParkingSpot=" + parkingSpot.getId() +
                ", cena=" + cena +
                '}';
    }
}
