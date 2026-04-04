package server.subscribers;

import java.util.Calendar;
import java.util.Date;

public class Abonne {
    private final int number;
    private final String name;
    private final Date birthday;

    public Abonne(int number, String name, Date birthday) {
        this.number = number;
        this.name = name;
        this.birthday = birthday;
    }

    public int getNumber() {
        return number;
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

}
