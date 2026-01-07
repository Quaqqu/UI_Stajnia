package stadnina;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "ratings")
public class Rating implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // NAPRAWA BŁĘDU: "value" to słowo zastrzeżone w SQL.
    // Zmieniamy nazwę kolumny w bazie na "score", ale w Javie zostaje "value".
    @Column(name = "score")
    private int value; // 0-5

    private LocalDate date;
    private String description;

    @ManyToOne
    @JoinColumn(name = "horse_id")
    private Horse horse;

    public Rating() {}

    public Rating(int value, String description, Horse horse) {
        this.value = value;
        this.date = LocalDate.now();
        this.description = description;
        this.horse = horse;
    }

    public int getValue() { return value; }
    public String getDescription() { return description; }
    public void setHorse(Horse horse) { this.horse = horse; }
}