package sk.stuba.fei.uim.vsa.pr2;

import sk.stuba.fei.uim.vsa.pr2.entities.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

public class Service extends sk.stuba.fei.uim.vsa.pr2.AbstractCarParkService {

    @Override
    public Object createCarPark(String name, String address, Integer pricePerHour) {
        if(name == null)
            return null;
        if(pricePerHour == null)
            return null;
        CarPark carPark1 = (CarPark) getCarPark(name);
        if (carPark1 != null)
            return null;
        CarPark carPark = new CarPark(name,address,pricePerHour);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(carPark);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return carPark;

    }

    @Override
    public Object getCarPark(Long carParkId) {
        if(carParkId == null)
            return null;
        EntityManager em = emf.createEntityManager();
        return em.find(CarPark.class, carParkId);
    }

    @Override
    public Object getCarPark(String carParkName) {
        if(carParkName == null)
            return null;
        EntityManager em = emf.createEntityManager();
        Query q1 = em.createQuery("SELECT cp FROM CAR_PARK cp WHERE cp.name= :name",CarPark.class);
        q1.setParameter("name", carParkName);
        if(q1.getResultList().isEmpty())
            return null;
        else
            return q1.getSingleResult();

    }

    @Override
    public List<Object> getCarParks() {
        EntityManager em = emf.createEntityManager();
        Query q1 = em.createQuery("SELECT cp FROM CAR_PARK cp ",CarPark.class);
        List<Object> list = new ArrayList<>();
        if(q1.getResultList().isEmpty())
            return list;
        else
            return q1.getResultList();
    }

    @Override
    public Object updateCarPark(Object carPark) {
        if (carPark == null){
            return null;
        }
        EntityManager em = emf.createEntityManager();
        CarPark carPark1 = em.find(CarPark.class, ((CarPark)carPark).getId());
        if (carPark1 == null)
            return null;
        if(!carPark1.getName().equals(((CarPark) carPark).getName())){
            Object carParkTmp = getCarPark(((CarPark) carPark).getName());
            if (carParkTmp != null)
                return null;
        }
        em.getTransaction().begin();
        try {
            em.merge(carPark);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

       return carPark;
    }

    @Override
    public Object deleteCarPark(Long carParkId) {
        if(carParkId == null)
            return null;
        EntityManager em = emf.createEntityManager();
        CarPark carPark = em.find(CarPark.class, carParkId);
        if(carPark==null)
            return null;
        em.getTransaction().begin();
        for(CarParkFloor carParkFloor : carPark.getList_car_park_floor()){
            for(ParkingSpot parkingSpot : carParkFloor.getParkingSpotList()){
                for(Reservation reservation : parkingSpot.getReservationList()){
                    reservation.setParkingSpot(null);
                    if(reservation.getEndDate() == null){
                        reservation.setEndDate(new Date());
                        int hours = (int)(reservation.getEndDate().getTime() - reservation.getStartDate().getTime())/1000*60*60;
                        hours++;
                        reservation.setCena(carPark.getPrice()*hours);
                    }
                    em.merge(reservation);
                }
            }
        }
        try {
            em.remove(carPark);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

        return carPark;
    }

    @Override
    public Object createCarParkFloor(Long carParkId, String floorIdentifier) {
        if(carParkId == null)
            return null;
        if(floorIdentifier == null)
            return null;

        EntityManager em = emf.createEntityManager();

        CarPark cp = em.find(CarPark.class, carParkId);
        if(cp == null)
            return null;

        CarParkFloor carParkFloor = new CarParkFloor(carParkId, floorIdentifier);

        List<CarParkFloor> carParkFloorList = cp.getList_car_park_floor();
        for(CarParkFloor carParkFloor1 : carParkFloorList){
            if (carParkFloor1.getFloorName().equals(floorIdentifier)){
                //System.out.println("Už je poschodie z daným menom");
                return null;
            }
        }

        carParkFloor.setCarPark(cp);
        cp.getList_car_park_floor().add(carParkFloor);
        em.getTransaction().begin();
        try {
            em.persist(carParkFloor);
            em.merge(cp);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return carParkFloor;

    }

    @Override
    public Object getCarParkFloor(Long carParkFloorId) {
        if(carParkFloorId == null)
            return null;
        EntityManager em = emf.createEntityManager();
        return em.find(CarParkFloor.class, carParkFloorId);
    }

    public Object getCarParkFloorByIdentifier(Long carParkId, String identifier) {
        if(carParkId == null)
            return null;
        EntityManager em = emf.createEntityManager();
        CarPark carPark = (CarPark) getCarPark(carParkId);
        if (carPark == null)
            return null;
        List<CarParkFloor> floors = carPark.getList_car_park_floor();
        for (CarParkFloor floor : floors){
            if (floor.getFloorName().equals(identifier))
                return floor;
        }
        return null;
    }


    @Override
    public List<Object> getCarParkFloors(Long carParkId) {
        List<Object> ocpf= new ArrayList<>();
        if(carParkId == null)
            return ocpf;
        EntityManager em = emf.createEntityManager();
        CarPark carPark = em.find(CarPark.class, carParkId);
        if (carPark==null)
            return ocpf;
        List<CarParkFloor> cpf = carPark.getList_car_park_floor();
        for (CarParkFloor carParkFloor : cpf){
            ocpf.add((Object)carParkFloor);
        }
        return ocpf;
    }

    @Override
    public Object updateCarParkFloor(Object carParkFloor) {
        if (carParkFloor == null)
            return null;
        EntityManager em = emf.createEntityManager();
        CarParkFloor carParkFloor1 = em.find(CarParkFloor.class, ((CarParkFloor)carParkFloor).getId());
        if (((CarParkFloor) carParkFloor).getCarPark() == null)
            return null;
        Object tmp = em.find(CarPark.class, ((CarParkFloor) carParkFloor).getCarPark().getId());
        if(tmp == null)
            return null;
        if (carParkFloor1 == null)
            return null;

        if (!carParkFloor1.getFloorName().equals(((CarParkFloor) carParkFloor).getFloorName())){
            Object tmpFloor = getCarParkFloor(((CarParkFloor) carParkFloor).getId(),((CarParkFloor) carParkFloor).getFloorName());
                if (tmpFloor != null){
                    return null;
                }
        }
        em.getTransaction().begin();
        try {
            em.merge(carParkFloor);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

        return carParkFloor;
    }



    @Override
    public Object deleteCarParkFloor(Long carParkFloorId) {
        if(carParkFloorId == null)
            return null;
        EntityManager em = emf.createEntityManager();
        CarParkFloor carParkFloor = em.find(CarParkFloor.class, carParkFloorId);
        if (carParkFloor == null){
            //System.out.println("neexistuje");
            return null;
        }
        em.getTransaction().begin();
        CarPark carPark = carParkFloor.getCarPark();
        for(ParkingSpot parkingSpot : carParkFloor.getParkingSpotList()){
            for(Reservation reservation : parkingSpot.getReservationList()){
                reservation.setParkingSpot(null);
                if(reservation.getEndDate()==null){
                    reservation.setEndDate(new Date());
                    int hours = (int)(reservation.getEndDate().getTime() - reservation.getStartDate().getTime())/1000*60*60;
                    hours++;
                    reservation.setCena(carPark.getPrice()*hours);

                }
                em.merge(reservation);
            }
        }

        try {
            em.remove(carParkFloor);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

        return carParkFloor;
    }

    @Override
    public Object deleteCarParkFloorByObject(CarParkFloor carParkFloor) {

        EntityManager em = emf.createEntityManager();
        if (carParkFloor == null){
            //System.out.println("neexistuje");
            return null;
        }
        em.getTransaction().begin();
        CarPark carPark = carParkFloor.getCarPark();
        for(ParkingSpot parkingSpot : carParkFloor.getParkingSpotList()){
            for(Reservation reservation : parkingSpot.getReservationList()){
                reservation.setParkingSpot(null);
                if(reservation.getEndDate()==null){
                    reservation.setEndDate(new Date());
                    int hours = (int)(reservation.getEndDate().getTime() - reservation.getStartDate().getTime())/1000*60*60;
                    hours++;
                    reservation.setCena(carPark.getPrice()*hours);

                }
                em.merge(reservation);
            }
        }

        try {
            em.remove(carParkFloor);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

        return carParkFloor;
    }



    @Override
    public Object createParkingSpot(Long carParkId, String floorIdentifier, String spotIdentifier) {
        if(carParkId == null)
            return null;
        if(floorIdentifier == null)
            return null;
        if(spotIdentifier == null)
            return null;


        EntityManager em = emf.createEntityManager();
        CarType ct = (CarType) getCarType("benzin");
        if(ct == null)
            createCarType("benzin");

        ParkingSpot parkingSpot = new ParkingSpot(carParkId, floorIdentifier, spotIdentifier);
        //parkingSpot.setReserved(false);
        CarPark cp = em.find(CarPark.class, carParkId);
        if(cp == null)
            return null;

        List<Object> carParkFloorList = new ArrayList<>();
        List<CarParkFloor> newlist = new ArrayList<>();
        carParkFloorList = getCarParkFloors(carParkId);
        for( Object cpfl : carParkFloorList){
            newlist.add((CarParkFloor) cpfl);
        }
        CarParkFloor carParkFloor = null;
        for(CarParkFloor cpf: newlist){
            if (cpf.getFloorName().equals(floorIdentifier)){
                carParkFloor =cpf;
            }
        }


        if (carParkFloor == null){
            //System.out.println("nenašiel poschodie");
            return null;
        }

        List<ParkingSpot> list = carParkFloor.getParkingSpotList();
        for(ParkingSpot ps : list){
            if (ps.getSpotName().equals(spotIdentifier)){
                //System.out.println("už je s takym menom");
                return null;
            }
        }
        CarType carType = (CarType) getCarType("benzin");


        parkingSpot.setCarType(carType);
        carType.getParkingSpots().add(parkingSpot);
        parkingSpot.setCarParkFloor(carParkFloor);
        carParkFloor.getParkingSpotList().add(parkingSpot);
        parkingSpot.setReserved(true);

        em.getTransaction().begin();
        try {
            em.persist(parkingSpot);
            em.merge(carParkFloor);
            em.merge(carType);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return parkingSpot;
    }

    @Override
    public Object getParkingSpot(Long parkingSpotId) {
        if(parkingSpotId == null)
            return null;
        EntityManager em = emf.createEntityManager();
        return em.find(ParkingSpot.class, parkingSpotId);
    }

    @Override
    public List<Object> getParkingSpots(Long carParkId, String floorIdentifier) {
        List<Object> ocpf= new ArrayList<>();
        if(carParkId == null)
            return ocpf;
        EntityManager em = emf.createEntityManager();
        CarPark carPark = em.find(CarPark.class, carParkId);

        if (carPark==null)
            return ocpf;

        List<Object> carParkFloorList = new ArrayList<>();
        List<CarParkFloor> newlist = new ArrayList<>();
        carParkFloorList = getCarParkFloors(carParkId);
        for( Object cpfl : carParkFloorList){
            newlist.add((CarParkFloor) cpfl);
        }
        CarParkFloor carParkFloor = null;
        for(CarParkFloor cpf: newlist){
            if (cpf.getFloorName().equals(floorIdentifier)){
                carParkFloor =cpf;
            }
        }
        if (carParkFloor == null){
            //System.out.println("nenašiel poschodie");
            return ocpf;
        }
        List<ParkingSpot> cpfl = carParkFloor.getParkingSpotList();
        for (ParkingSpot parkingSpot : cpfl){
            ocpf.add((Object)parkingSpot);
        }
        return ocpf;
    }

    @Override
    public Map<String, List<Object>> getParkingSpots(Long carParkId) {
        Map<String, List<Object>> map = new HashMap<>();
        if (carParkId == null){
            return map;
        }

        EntityManager em = emf.createEntityManager();
        CarPark carPark = (CarPark)getCarPark(carParkId);
        if (carPark == null){
            //System.out.println("neexistuje carpark");
            return map;
        }
        List<CarParkFloor> carParkFloorList = carPark.getList_car_park_floor();
        List<Object> parkingSpotList = new ArrayList<>();
        String carParkFloorName;
        for (CarParkFloor cpf : carParkFloorList){
            carParkFloorName = cpf.getFloorName();
            parkingSpotList = Collections.singletonList( getParkingSpots(carParkId, carParkFloorName));
            map.put(carParkFloorName,parkingSpotList);
        }

        return map;
    }

    @Override
    public Map<String, List<Object>> getAvailableParkingSpots(String carParkName) {
        CarPark carPark = (CarPark) getCarPark(carParkName);
        Boolean tmp = true;
        Map<String, List<Object>> finalMap = new HashMap<>();
        if (carPark == null)
            return finalMap;
        for(CarParkFloor carParkFloor : carPark.getList_car_park_floor()){
            List<Object> tmpList = new ArrayList<>();
            for(ParkingSpot parkingSpot : carParkFloor.getParkingSpotList()){
                for(Reservation reservation : parkingSpot.getReservationList()){
                    if (reservation.getEndDate()==null){
                        tmp = false;
                    }
                }
                if(tmp){
                    tmpList.add((Object) parkingSpot);
                }
                tmp= true;
            }
            //System.out.println(tmpList);
            finalMap.put(carParkFloor.getFloorName(), tmpList);
            tmp = true;
        }

        return finalMap;
    }

    @Override
    public Map<String, List<Object>> getOccupiedParkingSpots(String carParkName) {
        CarPark carPark = (CarPark) getCarPark(carParkName);
        Boolean tmp = true;
        Map<String, List<Object>> finalMap = new HashMap<>();
        if (carPark == null)
            return finalMap;
        for(CarParkFloor carParkFloor : carPark.getList_car_park_floor()){
            List<Object> tmpList = new ArrayList<>();
            for(ParkingSpot parkingSpot : carParkFloor.getParkingSpotList()){
                for(Reservation reservation : parkingSpot.getReservationList()){
                    if (reservation.getEndDate()==null){
                        tmp = false;
                    }
                }
                if(!tmp){
                    tmpList.add((Object) parkingSpot);
                }
                tmp= true;
            }
            //System.out.println(tmpList);
            finalMap.put(carParkFloor.getFloorName(), tmpList);
            tmp = true;
        }

        return finalMap;
    }

    @Override
    public Object updateParkingSpot(Object parkingSpot) {
        EntityManager em = emf.createEntityManager();
        if (parkingSpot == null)
            return null;
        ParkingSpot parkingSpot1 = em.find(ParkingSpot.class, ((ParkingSpot)parkingSpot).getId());
        if (parkingSpot1 == null)
            return null;
        Object tmp = em.find(CarParkFloor.class, ((ParkingSpot) parkingSpot).getCarParkFloor().getId());
        if(tmp == null){
            return null;
        }
        if(((ParkingSpot) parkingSpot).getCarParkFloor() == null)
            return null;
        em.getTransaction().begin();
        try {
            em.merge(parkingSpot);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

        return parkingSpot;
    }

    @Override
    public Object deleteParkingSpot(Long parkingSpotId) {
        if(parkingSpotId == null)
            return null;
        EntityManager em = emf.createEntityManager();
        ParkingSpot parkingSpot = em.find(ParkingSpot.class, parkingSpotId);
        if (parkingSpot == null){
            //System.out.println("neexistuje");
            return null;
        }
        CarParkFloor carParkFloor = parkingSpot.getCarParkFloor();
        CarPark carPark = carParkFloor.getCarPark();
        //Query q1 = em.createQuery("DELETE FROM PARKING_SPOT ps where ps.id = :id", ParkingSpot.class);
        em.getTransaction().begin();
        for(Reservation reservation : parkingSpot.getReservationList()){
            reservation.setParkingSpot(null);
            if(reservation.getEndDate() == null){
                reservation.setEndDate(new Date());
                int hours = (int)(reservation.getEndDate().getTime() - reservation.getStartDate().getTime())/1000*60*60;
                hours++;
                reservation.setCena(carPark.getPrice()*hours);
            }
            em.merge(reservation);
        }
        try {
            em.remove(parkingSpot);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

        return parkingSpot;
    }

    @Override
    public Object createCar(Long userId, String brand, String model, String colour, String vehicleRegistrationPlate) {
        Car tmpCar = (Car) getCar(vehicleRegistrationPlate);
        if (tmpCar!= null){
            //System.out.println("zle ecv");
            return null;
        }
        if(userId == null){
            //System.out.println("neni user");
            return null;
        }
        if (vehicleRegistrationPlate== null){
            //System.out.println("chyba ecv");
            return null;
        }
        EntityManager em = emf.createEntityManager();
        CarType ct = (CarType) getCarType("benzin");
        if(ct == null)
            ct = (CarType) createCarType("benzin");
        User user = em.find(User.class, userId);
        if (user == null){
            //System.out.println("neexistuje user");
            return null;
        }
        Car car = new Car(brand, model, colour, vehicleRegistrationPlate);
        CarType carType =(CarType) getCarType("benzin");
        car.setCarType(carType);
        car.setUser(user);
        user.getCarList().add(car);
        carType.getCars().add(car);
        em.getTransaction().begin();
        try {

            em.persist(car);
            em.merge(user);
            em.merge(carType);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return car;


    }

    @Override
    public Object getCar(Long carId) {
        if(carId == null)
            return null;
        EntityManager em = emf.createEntityManager();
        return em.find(Car.class, carId);
    }

    @Override
    public Object getCar(String vehicleRegistrationPlate) {
        if(vehicleRegistrationPlate == null)
            return null;
        EntityManager em = emf.createEntityManager();
        Query q1 = em.createQuery("SELECT c FROM CAR c WHERE c.ecv= :ecv",Car.class);
        q1.setParameter("ecv", vehicleRegistrationPlate);
        if(q1.getResultList().isEmpty())
            return null;
        else
            return q1.getSingleResult();
    }

    @Override
    public List<Object> getCars(Long userId) {
        List<Object> oc = new ArrayList<>();
        if (userId == null)
            return oc;
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class, userId);

        if (user==null)
            return oc;
        List<Car> cpf = user.getCarList();
        for (Car carParkFloor : cpf){
            oc.add((Object)carParkFloor);
        }
        return oc;
    }

    @Override
    public Object updateCar(Object car) {
        if (car == null)
            return null;
        EntityManager em = emf.createEntityManager();
        Car car1 = em.find(Car.class, ((Car)car).getId());
        if (car1 == null)
            return null;
        if(((Car) car).getUser()== null)
            return null;
        Object tmp = em.find(User.class, ((Car) car).getUser().getId());
        if(tmp == null)
            return null;
        Object carType = em.find(CarType.class, ((Car) car).getCarType().getId());
        if (carType==null)
            return null;
        if(!((Car) car).getEcv().equals(car1.getEcv())){
            Object catTmp = getCar(((Car) car).getEcv());
            if(catTmp!= null){
                return null;
            }
        }

        em.getTransaction().begin();
        try {
            em.merge(car);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

        return car;
    }

    @Override
    public Object deleteCar(Long carId) {
        if(carId == null)
            return null;
        EntityManager em = emf.createEntityManager();
        Car car = em.find(Car.class, carId);
        if(car==null)
            return null;

        em.getTransaction().begin();

        for(Reservation reservation : car.getReservationList()){
            reservation.setCar(null);
            CarPark carPark = (CarPark) reservation.getParkingSpot().getCarParkFloor().getCarPark();
            //reservation.setCena(reservation.getParkingSpot().getCarParkFloor().getCarPark().getPrice());
            if(reservation.getEndDate()== null){
                reservation.setEndDate(new Date());
                int hours = (int)(reservation.getEndDate().getTime() - reservation.getStartDate().getTime())/1000*60*60;
                hours++;
                reservation.setCena(carPark.getPrice()*hours);

            }
            em.merge(reservation);
        }

        try {
            em.remove(car);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return car;
    }

    @Override
    public Object createUser(String firstname, String lastname, String email) {
        if(email == null)
            return null;
        Object user1 = getUser(email);
        if (user1!= null){
            //System.out.println("zly mail");
            return null;
        }
        User user = new User(firstname,lastname,email);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

        return user;
    }

    @Override
    public Object getUser(Long userId) {
        if(userId == null)
            return null;
        EntityManager em = emf.createEntityManager();
        return em.find(User.class, userId);
    }

    @Override
    public Object getUser(String email) {
        if(email == null)
            return null;
        EntityManager em = emf.createEntityManager();
        Query q1 = em.createQuery("SELECT cp FROM USER cp WHERE cp.email= :email",User.class);
        q1.setParameter("email", email);
        if(q1.getResultList().isEmpty())
            return null;
        else
            return q1.getSingleResult();
    }

    @Override
    public List<Object> getUsers() {
        EntityManager em = emf.createEntityManager();
        Query q1 = em.createQuery("SELECT u FROM USER u ",User.class);
        List<Object> list = new ArrayList<>();
        if(q1.getResultList().isEmpty())
            return list;
        else
            return q1.getResultList();
    }

    @Override
    public Object updateUser(Object user) {
        if (user == null)
            return null;
        EntityManager em = emf.createEntityManager();
        User user1 = em.find(User.class, ((User)user).getId());
        if (user1 == null)
            return null;
        if(((User) user).getEmail()==null)
            return null;
        if(!((User) user).getEmail().equals(user1.getEmail())){
            Object tmpUser = getUser(((User) user).getEmail());
            if(tmpUser!=null)
                return null;
        }
        em.getTransaction().begin();
        try {
            em.merge(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

        return user;
    }

    @Override
    public Object deleteUser(Long userId) {
        if(userId == null)
            return null;
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class, userId);
        if(user==null)
            return null;
        em.getTransaction().begin();
        for(Car car : user.getCarList()){
            for(Reservation reservation : car.getReservationList()){
                reservation.setCar(null);
                CarPark carPark = reservation.getParkingSpot().getCarParkFloor().getCarPark();
                if(reservation.getEndDate() == null){
                    reservation.setEndDate(new Date());
                    int hours = (int)(reservation.getEndDate().getTime() - reservation.getStartDate().getTime())/1000*60*60;
                    hours++;
                    reservation.setCena(carPark.getPrice()*hours);
                }
                em.merge(reservation);
            }
        }

        try {
            em.remove(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

        return user;
    }

    @Override
    public Object createReservation(Long parkingSpotId, Long cardId) {
        EntityManager em = emf.createEntityManager();
        ParkingSpot parkingSpot = em.find(ParkingSpot.class, parkingSpotId);
        Car car = em.find(Car.class, cardId);
        if (parkingSpot == null)
            return null;
        if(car == null)
            return null;
        if(!car.getCarType().equals(parkingSpot.getCarType()))
            return null;
        for(Reservation reservation : car.getReservationList()){
            if (reservation.getEndDate()==null)
                return null;
        }
        Date date = new Date(System.currentTimeMillis());
        for (Reservation reservation : parkingSpot.getReservationList()){
            if(reservation.getEndDate()==null)
                return null;
        }
        Reservation reservation = new Reservation(car, parkingSpot);
        em.getTransaction().begin();
        reservation.setCar(car);
        car.getReservationList().add(reservation);
        reservation.setParkingSpot(parkingSpot);
        parkingSpot.getReservationList().add(reservation);
        parkingSpot.setReserved(false);
        //Date date =  new Date(System.currentTimeMillis());
        reservation.setStartDate(date);
        try {
            em.persist(reservation);
            em.merge(parkingSpot);
            em.merge(car);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

        return reservation;
    }

    @Override
    public Object endReservation(Long reservationId) {
        if(reservationId == null)
            return null;
        EntityManager em = emf.createEntityManager();
        Reservation reservation = (Reservation) em.find(Reservation.class, reservationId);
        if(reservation == null)
            return null;
        if(reservation.getEndDate()!= null)
            return null;
        Date endDate = new Date(System.currentTimeMillis());
        Date startDate = reservation.getStartDate();

        ParkingSpot parkingSpot = reservation.getParkingSpot();
        CarParkFloor carParkFloor = parkingSpot.getCarParkFloor();
        CarPark carPark = carParkFloor.getCarPark();
        int cena = carPark.getPrice();
        reservation.setEndDate(endDate);
        long hours = (reservation.getEndDate().getTime() - reservation.getStartDate().getTime())/(1000*60*60);        hours++;
        //System.out.println(hours);
        int parkingPrice =(int) hours * cena;
        //parkingSpot.setReserved(false);
       // reservation.setParkingSpot(null);
        //reservation.setCar(null);
        parkingSpot.setReserved(true);
        reservation.setCena(parkingPrice);
        em.getTransaction().begin();
        try {
            //em.merge(reservation);
            em.persist(reservation);
            em.merge(parkingSpot);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return reservation;
    }

    @Override
    public List<Object> getReservations(Long parkingSpotId, Date date) {
        List<Object> error = new ArrayList<>();
        if(parkingSpotId == null)
            return error;
        if(date == null)
            return error;
        EntityManager em = emf.createEntityManager();
        ParkingSpot parkingSpot = em.find(ParkingSpot.class, parkingSpotId);
        if(parkingSpot == null)
            return error;
        List<Reservation> reservationList = parkingSpot.getReservationList();
        List<Object> tmpReservations = new ArrayList<>();
        for( Reservation reservation : reservationList){
            if (isSameDay(date, reservation.getStartDate())){
                tmpReservations.add((Object) reservation);
            }
        }
        return tmpReservations;
    }
    public static boolean isSameDay(Date date1, Date date2) {
        Calendar searchDate = Calendar.getInstance();
        searchDate.setTime(date1);
        Calendar searchDate2 = Calendar.getInstance();
        searchDate2.setTime(date2);
        if(searchDate.get(Calendar.YEAR) == searchDate2.get(Calendar.YEAR) && searchDate.get(Calendar.MONTH) == searchDate2.get(Calendar.MONTH) && searchDate.get(Calendar.DAY_OF_MONTH) == searchDate2.get(Calendar.DAY_OF_MONTH))
            return true;
        return false;
    }

    @Override
    public Object getReservationById(Long id){
        if(id == null)
            return null;
        EntityManager em = emf.createEntityManager();
        return em.find(Reservation.class, id);
    }

    @Override
    public List<Object> getMyReservations(Long userId) {
        List<Object> finalList = new ArrayList<>();
        if (userId == null)
            return finalList;
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class, userId);
        if(user == null)
            return finalList;
        List<Car> carList = user.getCarList();


        for (Car car : carList){
            List<Reservation> list = car.getReservationList();
            for (Reservation reservation : list){
                if(reservation.getEndDate() == null)
                    finalList.add((Object) reservation);
            }
        }
        return finalList;
    }

    @Override
    public Object updateReservation(Object reservation) {
        if (reservation == null)
            return null;
        EntityManager em = emf.createEntityManager();
        Reservation reservation1 = em.find(Reservation.class, ((Reservation)reservation).getId());
        if (reservation1 == null)
            return null;
        if(((Reservation) reservation).getCar()==null)
            return null;
        if(((Reservation) reservation).getParkingSpot()==null)
            return null;
        Object tmp = em.find(ParkingSpot.class, ((Reservation) reservation).getParkingSpot().getId());
        if(tmp == null){
            return null;
        }
        Object tmp1 = em.find(Car.class, ((Reservation) reservation).getCar().getId());
        if(tmp1 == null)
            return null;

        em.getTransaction().begin();
        try {
            em.merge(reservation);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return reservation;

    }

    @Override
    public Object createCarType(String name) {
        if(name == null)
            return null;
        CarType carType1 = (CarType) getCarType(name);
        if (carType1 != null)
            return null;
        //CarPark carPark = new CarPark(name,address,pricePerHour);
        CarType carType = new CarType(name);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(carType);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return carType;
    }

    @Override
    public List<Object> getCarTypes() {
        EntityManager em = emf.createEntityManager();
        Query q1 = em.createQuery("SELECT ct FROM CAR_TYPE ct ",CarType.class);
        List<Object> list = new ArrayList<>();
        if(q1.getResultList().isEmpty())
            return list;
        else
            return q1.getResultList();
    }

    @Override
    public Object getCarType(Long carTypeId) {
        if(carTypeId == null)
            return null;
        EntityManager em = emf.createEntityManager();
        return em.find(CarType.class, carTypeId);
    }

    @Override
    public Object getCarType(String name) {
        if(name == null)
            return null;
        EntityManager em = emf.createEntityManager();
        Query q1 = em.createQuery("SELECT ct FROM CAR_TYPE ct WHERE ct.typeName= :name",CarType.class);
        q1.setParameter("name", name);
        if(q1.getResultList().isEmpty())
            return null;
        else
            return q1.getSingleResult();
    }

    @Override
    public Object deleteCarType(Long carTypeId) {
        if(carTypeId == 1L)
            System.out.println("nemozes vymazať default");
        if (carTypeId == null)
            return null;
        EntityManager em = emf.createEntityManager();
        CarType carType = em.find(CarType.class, carTypeId);
        if(carType == null)
            return null;

        em.getTransaction().begin();


        for(ParkingSpot parkingSpot : carType.getParkingSpots()){
            for(Reservation reservation : parkingSpot.getReservationList()){
                if(reservation.getEndDate() == null){
                    return null;
                }else {
                    reservation.setParkingSpot(null);
                }
                em.merge(reservation);
            }
        }

        for(Car cars : carType.getCars()){
            for(Reservation reservation : cars.getReservationList()){
                if(reservation.getEndDate() == null){
                    return null;
                }else {
                    reservation.setCar(null);
                }
                em.merge(reservation);
            }
        }

        try {
            em.remove(carType);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

        return carType;

    }

    @Override
    public Object createCar(Long userId, String brand, String model, String colour, String vehicleRegistrationPlate, Long carTypeId) {
        Car tmpCar = (Car) getCar(vehicleRegistrationPlate);
        if (tmpCar!= null){
            //System.out.println("zle ecv");
            return null;
        }
        if(userId == null){
            //System.out.println("neni user");
            return null;
        }
        if (vehicleRegistrationPlate== null){
            //System.out.println("chyba ecv");
            return null;
        }
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class, userId);
        if (user == null){
            //System.out.println("neexistuje user");
            return null;
        }
        Car car = new Car(brand, model, colour, vehicleRegistrationPlate);
        CarType carType =em.find(CarType.class, carTypeId);
        if (carType == null)
            return null;
        car.setCarType(carType);
        car.setUser(user);
        user.getCarList().add(car);
        carType.getCars().add(car);
        em.getTransaction().begin();
        try {

            em.persist(car);
            em.merge(user);
            em.merge(carType);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return car;
    }

    @Override
    public Object createParkingSpot(Long carParkId, String floorIdentifier, String spotIdentifier, Long carTypeId) {
        if(carParkId == null)
            return null;
        if(floorIdentifier == null)
            return null;
        if(spotIdentifier == null)
            return null;
        EntityManager em = emf.createEntityManager();
        CarPark cp = em.find(CarPark.class, carParkId);
        if(cp == null)
            return null;
        CarType carType = em.find(CarType.class, carTypeId);
        if(carType == null)
            return null;
        ParkingSpot parkingSpot = new ParkingSpot(carParkId, floorIdentifier, spotIdentifier);
        parkingSpot.setReserved(true);


        List<Object> carParkFloorList = new ArrayList<>();
        List<CarParkFloor> newlist = new ArrayList<>();
        carParkFloorList = getCarParkFloors(carParkId);
        for( Object cpfl : carParkFloorList){
            newlist.add((CarParkFloor) cpfl);
        }
        CarParkFloor carParkFloor = null;
        for(CarParkFloor cpf: newlist){
            if (cpf.getFloorName().equals(floorIdentifier)){
                carParkFloor =cpf;
            }
        }


        if (carParkFloor == null){
            //System.out.println("nenašiel poschodie");
            return null;
        }

        List<ParkingSpot> list = carParkFloor.getParkingSpotList();
        for(ParkingSpot ps : list){
            if (ps.getSpotName().equals(spotIdentifier)){
                //System.out.println("už je s takym menom");
                return null;
            }
        }



        parkingSpot.setCarType(carType);
        carType.getParkingSpots().add(parkingSpot);
        parkingSpot.setCarParkFloor(carParkFloor);
        carParkFloor.getParkingSpotList().add(parkingSpot);

        em.getTransaction().begin();
        try {
            em.persist(parkingSpot);
            em.merge(carParkFloor);
            em.merge(carType);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return parkingSpot;
    }
}
