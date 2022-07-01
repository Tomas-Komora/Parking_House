package sk.stuba.fei.uim.vsa.pr2.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity(name = "CAR_TYPE")
public class CarType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String typeName;

    @OneToMany(mappedBy = "carType", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Car> cars;

    @OneToMany(mappedBy = "carType", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ParkingSpot> parkingSpots;

    public CarType() {
    }

    public CarType(String typeName) {
        this.typeName = typeName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public List<ParkingSpot> getParkingSpots() {
        return parkingSpots;
    }

    public void setParkingSpots(List<ParkingSpot> parkingSpots) {
        this.parkingSpots = parkingSpots;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarType carType = (CarType) o;
        return Objects.equals(id, carType.id) && Objects.equals(typeName, carType.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, typeName);
    }

    @Override
    public String toString() {
        return "CarType{" +
                "id=" + id +
                ", typeName='" + typeName + '\'' +
                '}';
    }
}
