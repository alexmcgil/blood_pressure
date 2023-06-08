package su.alexmcgil.blood_pressure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Pressure {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    Short systolic;
    Short diastolic;
    Short pulse;
    LocalDateTime timePressure;
    @ManyToOne(cascade = CascadeType.ALL)
    User user;

    @Override
    public String toString() {
        return "Pressure{" +
                "systolic=" + systolic +
                ", diastolic=" + diastolic +
                ", pulse=" + pulse +
                ", timePressure=" + timePressure +
                '}';
    }
}
