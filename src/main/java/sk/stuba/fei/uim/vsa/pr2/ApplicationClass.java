package sk.stuba.fei.uim.vsa.pr2;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import sk.stuba.fei.uim.vsa.pr2.entities.CarType;
import sk.stuba.fei.uim.vsa.pr2.resources.*;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class ApplicationClass extends Application {

    static final Set<Class<?>> appClasses = new HashSet<>();

    static {
        appClasses.add(CarParkResources.class);
        appClasses.add(CarParkFloorResources.class);
        appClasses.add(ParkingSpotResource.class);
        appClasses.add(CarResources.class);
        appClasses.add(UserResources.class);
        appClasses.add(CarTypeResources.class);
        appClasses.add(ReservationResource.class);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return appClasses;
    }
}
