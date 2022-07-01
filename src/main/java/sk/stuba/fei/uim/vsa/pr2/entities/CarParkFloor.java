package sk.stuba.fei.uim.vsa.pr2.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity(name = "CAR_PARK_FLOOR")
public class CarParkFloor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(nullable = false)
    private Long carParkId;

    public Long getId() {
        return id;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    @Column(nullable = false)
    private String floorName;

    public List<ParkingSpot> getParkingSpotList() {
        return parkingSpotList;
    }

    public void setParkingSpotList(List<ParkingSpot> parkingSpotList) {
        this.parkingSpotList = parkingSpotList;
    }

    @OneToMany(mappedBy = "carParkFloor", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ParkingSpot> parkingSpotList;

    @ManyToOne
    private CarPark carPark;

    public Long getCarParkId() {
        return carParkId;
    }

    public void setCarParkId(Long carParkId) {
        this.carParkId = carParkId;
    }

    public CarPark getCarPark() {
        return carPark;
    }

    public void setCarPark(CarPark carPark) {
        this.carPark = carPark;
    }

    public CarParkFloor() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CarParkFloor(Long carParkId, String floorName) {
        this.carParkId = carParkId;
        this.floorName = floorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarParkFloor that = (CarParkFloor) o;
        return Objects.equals(id, that.id) &&  Objects.equals(floorName, that.floorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id , floorName);
    }

    @Override
    public String toString() {
        return "CarParkFloor{" +
                "id=" + id +
                ", carParkId="+carPark.getId() +
                ", floorName='" + floorName + '\'' +
                '}';
    }
}
