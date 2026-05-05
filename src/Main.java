import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class Main {
    public static void main(String[] args) {
        AirportDB dB = new AirportDB();
//        dB.createTables();
//        dB.addFlight(new Flight("Airfloat", "SU123", "18 20", "20 50", 2005));
//        if (dB.existsFlight(1)) dB.addPassenger(new Passenger(true, (byte) 18, 'M', 1));
//        System.out.println(dB.getAllPassengers());
//        dB.removePassenger(7);
//        dB.removePassenger(5);
//        dB.removePassenger(3);
        System.out.println(dB.getAllPassengers());
//        System.out.println(dB.joinTables());
//        System.out.println(dB.countPassengers());
    }
}
class AirportDB {
    private final String url = "jdbc:h2:file:D:\\H2";
    private final String user = "Timofey";
    private final String password = "admin12345";
    class FlightPassengerCount {
        int flight_id;
        int count;

        public FlightPassengerCount(int flight_id, int count) {
            this.flight_id = flight_id;
            this.count = count;
        }

        @Override
        public String toString() {
            return "FlightPassengerCount{" +
                    "flight_id=" + flight_id +
                    ", count=" + count +
                    '}';
        }
    }
    class PassengerFlightInfo {
        int passengerId;
        String company_name;
        String airplane;

        public PassengerFlightInfo(int passengerId, String company_name, String airplane) {
            this.passengerId = passengerId;
            this.company_name = company_name;
            this.airplane = airplane;
        }
        @Override
        public String toString() {
            return "PassengerFlightInfo{" +
                    "passengerId=" + passengerId +
                    ", company_name='" + company_name + '\'' +
                    ", airplane='" + airplane + '\'' +
                    '}';
        }
    }
    private FlightPassengerCount extractPFCount(ResultSet rs) throws SQLException {
        int flight_id = rs.getInt("flight_id");
        int count = rs.getInt("count");
        return new FlightPassengerCount(flight_id, count);
    }
    public List<FlightPassengerCount> countPassengers() {
        List<FlightPassengerCount> result = new ArrayList<>();
        String sql = "SELECT flight_id, COUNT(*) as count FROM passengers GROUP BY flight_id";
        try(Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                result.add(extractPFCount(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    public void createTables() {
        String passengers = """
            CREATE TABLE IF NOT EXISTS passengers (
            id INT PRIMARY KEY AUTO_INCREMENT,
            has_ticket BOOLEAN,
            age INT,
            gender CHAR(1),
            flight_id INT,
            FOREIGN KEY (flight_id) REFERENCES flights(flight_id)
            )""";
        String flights  = """
            CREATE TABLE IF NOT EXISTS flights (
            flight_id INT PRIMARY KEY AUTO_INCREMENT,
            company_name VARCHAR(255),
            airplane VARCHAR(255),
            time_up VARCHAR(255),
            time_down VARCHAR(255),
            airplane_year INT
            )
            """;
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            stmt.execute(flights);
            stmt.execute(passengers);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private PassengerFlightInfo extractPassengerFlight(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String company_name = rs.getString("company_name");
        String airplane = rs.getString("airplane");
        return new PassengerFlightInfo(id, company_name, airplane);
    }
    public List<PassengerFlightInfo> joinTables() {
        List<PassengerFlightInfo> result = new ArrayList<>();
        String sql = "SELECT passengers.id, flights.company_name, flights.airplane FROM passengers JOIN flights ON flights.flight_id = passengers.flight_id";
        try(Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                result.add(extractPassengerFlight(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    private Passenger extractPassenger(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        boolean has_ticket = rs.getBoolean("has_ticket");
        int age = rs.getInt("age");
        String g = rs.getString("gender");
        char gender = g.charAt(0);
        int flight_id = rs.getInt("flight_id");
        return new Passenger(id, has_ticket, (byte)age, gender, flight_id);
    }
    private Flight extractFlight(ResultSet rs) throws SQLException {
        int flight_id = rs.getInt("flight_id");
        String company_name = rs.getString("company_name");
        String airplane = rs.getString("airplane");
        String time_up = rs.getString("time_up");
        String time_down = rs.getString("time_down");
        int airplane_year = rs.getInt("airplane_year");
        return new Flight(flight_id, company_name, airplane, time_up, time_down, airplane_year);
    }
    public List<Passenger> getAllPassengers() {
        List<Passenger> passengers = new ArrayList<>();
        String sql = "SELECT * FROM passengers";
        try(Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                passengers.add(extractPassenger(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return passengers;
    }
    public List<Flight> getAllFlight() {
        List<Flight> flight = new ArrayList<>();
        String sql = "SELECT * FROM flights";
        try(Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                flight.add(extractFlight(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return flight;
    }
    public void addFlight(Flight flight) {
        String sql = "INSERT INTO flights(company_name, airplane, time_up, time_down, airplane_year) VALUES (?, ?, ?, ?, ?)";

        try(Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, flight.getCompany_name());
            pstmt.setString(2, flight.getAirplane());
            pstmt.setString(3, flight.getTime_up());
            pstmt.setString(4, flight.getTime_down());
            pstmt.setInt(5, flight.getAirplane_year());
            pstmt.executeUpdate();
            System.out.println("Новый рейс был успешно добавлен!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void addPassenger(Passenger passenger){
        String sql = "INSERT INTO passengers(has_ticket, age, gender, flight_id) VALUES (?, ?, ?, ?)";

        try(Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String gender = "" +passenger.getGender();
            pstmt.setBoolean(1, passenger.isHas_ticket());
            pstmt.setByte(2, passenger.getAge());
            pstmt.setString(3, gender);
            pstmt.setInt(4, passenger.getFlight_id());
            if (existsFlight(passenger.getFlight_id())){
                pstmt.executeUpdate();
                System.out.println("Новый пассажир был добавлен!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void removePassenger(int id) {
        String sql = "DELETE FROM passengers WHERE id = ?";
        try(Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                System.out.println("Был удален пассажир с id=" + id);
            }
            else {
                System.out.println("Пассажира с таким id не найдено!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void removeFlight(int flight_id) {
        String sql = "DELETE FROM flight WHERE flight_id = ?";
        try(Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, flight_id);
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                System.out.println("Был удален рейс с flight_id=" + flight_id);
            }
            else {
                System.out.println("Рейс с таким flight_id не найдено!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean existsFlight(int flight_id) {
        String sql = "SELECT 1 FROM flights WHERE flight_id = ?";

        try(Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, flight_id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
class Passenger {
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
}
class Flight {
    int flight_id;
    String company_name;
    String airplane;
    String time_up;
    String time_down;
    int airplane_year;

    public int getFlight_id() {
        return flight_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getAirplane() {
        return airplane;
    }

    public String getTime_up() {
        return time_up;
    }

    public String getTime_down() {
        return time_down;
    }

    public int getAirplane_year() {
        return airplane_year;
    }

    public Flight(){}
    public Flight(int flight_id, String company_name, String airplane, String time_up, String time_down, int airplane_year) {
        this.flight_id = flight_id;
        this.company_name = company_name;
        this.airplane = airplane;
        this.time_up = time_up;
        this.time_down = time_down;
        this.airplane_year = airplane_year;
    }
    public Flight(String company_name, String airplane, String time_up, String time_down, int airplane_year) {
        this.company_name = company_name;
        this.airplane = airplane;
        this.time_up = time_up;
        this.time_down = time_down;
        this.airplane_year = airplane_year;
    }
}