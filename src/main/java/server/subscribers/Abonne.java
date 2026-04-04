package server.subscribers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Abonne {
    private final int number;
    private final String name;
    private final Date birthday;

    private Date bannedUntil;

    public Abonne(int number, String name, Date birthday) {
        this.number = number;
        this.name = name;
        this.birthday = birthday;
        this.bannedUntil = null;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    /**
     * Banni l'abonné pendant 1 mois.
     * Si déjà banni, repousse la date de fin de bannissement d'1 mois supplémentaire.
     */
    public void ban() {
        Calendar cal = Calendar.getInstance();
        if (isBanned()) {
            cal.setTime(bannedUntil);
        }
        cal.add(Calendar.MONTH, 1);
        this.bannedUntil = cal.getTime();
    }

    /**
     * @return true si l'abonné est actuellement banni
     */
    public boolean isBanned() {
        return bannedUntil != null && new Date().before(bannedUntil);
    }

    /**
     * @return message de bannissement lisible
     */
    public String getBannedMessage() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return "Erreur : l'abonné " + name + " est banni de la médiathèque jusqu'au "
                + sdf.format(bannedUntil);
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
