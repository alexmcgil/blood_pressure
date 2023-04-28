package su.alexmcgil.blood_pressure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    Long telegramID;
    String name;
    @OneToMany
    @OrderBy("timePressure ASC")
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    List<Pressure> userPressures = new ArrayList<>();
    boolean userWantChangeName = false;

    @Override
    public String toString() {
        return "User{" +
                "telegramID=" + telegramID +
                ", name='" + name + '\'' +
                ", userPressures=" + userPressures +
                '}';
    }
}
