package sk.stuba.fei.uim.vsa.pr2.entities;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity(name = "CAR_PARK")
public class CarPark implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String address;
    private int price;

    @OneToMany(mappedBy = "carPark", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CarParkFloor> list_car_park_floor;


    public CarPark() {
    }

    public CarPark(String name, String address, int price) {
        this.name = name;
        this.address = address;
        this.price = price;
        this.list_car_park_floor = new ArrayList<>();
    }

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<CarParkFloor> getList_car_park_floor() {
        return list_car_park_floor;
    }

    public void setList_car_park_floor(List<CarParkFloor> list_car_park_floor) {
        this.list_car_park_floor = list_car_park_floor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarPark carPark = (CarPark) o;
        return id == carPark.id && price == carPark.price && Objects.equals(name, carPark.name) && Objects.equals(address, carPark.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, price);
    }

    @Override
    public String toString() {
        return "CarPark{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", price=" + price +
                '}';
    }

}
