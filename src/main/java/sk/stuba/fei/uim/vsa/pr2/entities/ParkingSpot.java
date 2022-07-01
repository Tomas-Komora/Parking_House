package sk.stuba.fei.uim.vsa.pr2.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity(name = "PARKING_SPOT")
public class ParkingSpot implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long carParkId;
    @Column(nullable = false)
    private String carParkFloorName;
    @Column(nullable = false)
    private String spotName;
    private Boolean reserved;

    public Boolean getReserved() {
        return reserved;
    }

    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }

    @OneToMany(mappedBy = "parkingSpot",cascade = CascadeType.PERSIST)
    private List<Reservation> reservationList;

    public void setCarParkId(Long carParkId) {
        this.carParkId = carParkId;
    }

    @ManyToOne
    private CarParkFloor carParkFloor;

    @ManyToOne
    private CarType carType;

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }

    public ParkingSpot() {
    }

    public ParkingSpot(Long carParkId, String carParkFloorName, String spotName) {
        this.carParkId = carParkId;
        this.carParkFloorName = carParkFloorName;
        this.spotName = spotName;
        //this.reserved = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCarParkId() {
        return carParkId;
    }

    public void setCarParkFloorName(String carParkFloorName) {
        this.carParkFloorName = carParkFloorName;
    }

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    public CarParkFloor getCarParkFloor() {
        return carParkFloor;
    }

    public void setCarParkFloor(CarParkFloor carParkFloor) {
        this.carParkFloor = carParkFloor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSpot that = (ParkingSpot) o;
        return Objects.equals(id, that.id) && Objects.equals(carParkId, that.carParkId) && Objects.equals(carParkFloorName, that.carParkFloorName) && Objects.equals(spotName, that.spotName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, carParkId, carParkFloorName, spotName);
    }

    @Override
    public String toString() {
        return "ParkingSpot{" +
                "id=" + id +
                ", carParkId=" + carParkId +
                ", carParkFloorName='" + carParkFloorName + '\'' +
                ", spotName='" + spotName + '\'' +
                ", carType=" + carType +
                ", reserved=" + reserved +

                '}';
    }
}
