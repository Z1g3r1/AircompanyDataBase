package aircompany;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class Main {
    public static void main(String[] args) {
            System.out.println("Начало работы Main!");
        AirportDB dB = new AirportDB();
        dB.createTables();
        System.out.println("Таблицы были созданы!");
//        dB.addFlight(new Flight("Airfloat", "SU123", "18 20", "20 50", 2005));
//        if (dB.existsFlight(1)) dB.addPassenger(new Passenger(true, (byte) 18, 'M', 1));
//        System.out.println(dB.getAllPassengers());
//        dB.removePassenger(7);
//        dB.removePassenger(5);
//        dB.removePassenger(3);
        System.out.println(dB.getAllPassengers());
//        System.out.println(dB.joinTables());
        System.out.println(dB.countPassengers());
    }
}