package server.subscribers;

import java.util.Calendar;
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

    public Date getBirthday() {
        return birthday;
    }



    public int getAge() {
        Calendar birthDate = Calendar.getInstance();
        birthDate.setTime(birthday);

        Calendar currentDate = Calendar.getInstance();

        int age = currentDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        if (currentDate.get(Calendar.MONTH) < birthDate.get(Calendar.MONTH) ||
                (currentDate.get(Calendar.MONTH) == birthDate.get(Calendar.MONTH) &&
                        currentDate.get(Calendar.DAY_OF_MONTH) < birthDate.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }
        return age;
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
