package aircompany;

import java.util.Objects;

public class Passenger {
    private int id;
    private boolean has_ticket;
    private byte age;
    private char gender;
    private int flight_id;

    @Override
    public String toString() {
        return "Passenger{" +
                "id=" + id +
                ", has_ticket=" + has_ticket +
                ", age=" + age +
                ", gender=" + gender +
                ", flight_id=" + flight_id +
                '}';
    }

    public Passenger(int id, boolean has_ticket, byte age, char gender, int flight_id) {
        this.id = id;
        this.has_ticket = has_ticket;
        this.age = age;
        this.gender = gender;
        this.flight_id = flight_id;
    }
    public Passenger(boolean has_ticket, byte age, char gender, int flight_id) {
        this.has_ticket = has_ticket;
        this.age = age;
        this.gender = gender;
        this.flight_id = flight_id;
    }
    public int getId() {
        return id;
    }

    public boolean isHas_ticket() {
        return has_ticket;
    }

    public byte getAge() {
        return age;
    }

    public char getGender() {
        return gender;
    }

    public int getFlight_id() {
        return flight_id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Passenger passenger = (Passenger) o;
        return id == passenger.id && has_ticket == passenger.has_ticket && age == passenger.age && gender == passenger.gender && flight_id == passenger.flight_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, has_ticket, age, gender, flight_id);
    }
}