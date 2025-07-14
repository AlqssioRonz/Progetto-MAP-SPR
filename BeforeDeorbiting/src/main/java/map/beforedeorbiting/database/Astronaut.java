package map.beforedeorbiting.database;

/**
 * Rappresenta un astronauta con i suoi dati anagrafici e il totale delle ore
 * trascorse a bordo della Stazione Spaziale Internazionale (ISS).
 *
 * @author lorenzopeluso
 */
public class Astronaut {

    /**
     * Nome dell’astronauta.
     */
    private String name;

    /**
     * Cognome dell’astronauta.
     */
    private String surname;

    /**
     * Data di nascita dell’astronauta, in formato ISO-8601 (YYYY-MM-DD).
     */
    private String dateOfBirth;

    /**
     * Luogo di nascita: città e/o paese.
     */
    private String birthplace;

    /**
     * Numero totale di ore trascorse dall’astronauta a bordo della Stazione
     * Spaziale Internazionale.
     */
    private int hoursOnISS;

    /**
     * Costruisce un nuovo oggetto {@code Astronaut} con tutti i campi
     * inizializzati ai valori di default.
     */
    public Astronaut() {
        // costruttore di default
    }

    /**
     * Restituisce il nome dell’astronauta.
     *
     * @return il nome dell’astronauta
     */
    public String getName() {
        return name;
    }

    /**
     * Imposta il nome dell’astronauta.
     *
     * @param name il nome da assegnare
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Restituisce il cognome dell’astronauta.
     *
     * @return il cognome dell’astronauta
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Imposta il cognome dell’astronauta.
     *
     * @param surname il cognome da assegnare
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Restituisce la data di nascita dell’astronauta.
     *
     * @return la data di nascita in formato "YYYY-MM-DD"
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Imposta la data di nascita dell’astronauta.
     *
     * @param dateOfBirth la data di nascita da assegnare, formato "YYYY-MM-DD"
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Restituisce il luogo di nascita dell’astronauta.
     *
     * @return il luogo di nascita (città, paese)
     */
    public String getBirthplace() {
        return birthplace;
    }

    /**
     * Imposta il luogo di nascita dell’astronauta.
     *
     * @param birthplace il luogo di nascita da assegnare
     */
    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    /**
     * Restituisce il totale delle ore trascorse a bordo della Stazione Spaziale
     * Internazionale.
     *
     * @return numero di ore trascorse in ISS
     */
    public int getHoursOnISS() {
        return hoursOnISS;
    }

    /**
     * Imposta il totale delle ore trascorse a bordo della Stazione Spaziale
     * Internazionale.
     *
     * @param hoursOnISS il numero di ore da assegnare
     */
    public void setHoursOnISS(int hoursOnISS) {
        this.hoursOnISS = hoursOnISS;
    }
}
