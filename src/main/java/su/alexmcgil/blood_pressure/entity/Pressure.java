package su.alexmcgil.blood_pressure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
