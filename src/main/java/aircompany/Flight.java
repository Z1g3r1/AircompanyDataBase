package aircompany;

public class Flight {
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