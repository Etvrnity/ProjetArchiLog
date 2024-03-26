package server.subscribers;

import java.util.Date;

public class Subscriber {
    private int number;
    private String name;
    private Date birthday;

    public Subscriber(int number, String name, Date birthday) {
        this.number = number;
        this.name = name;
        this.birthday = birthday;
    }

    public int getNumber(){
        return number;
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "number=" + number +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
