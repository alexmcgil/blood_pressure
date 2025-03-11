package su.alexmcgil.blood_pressure.it;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import su.alexmcgil.blood_pressure.entity.Pressure;
import su.alexmcgil.blood_pressure.entity.User;
import su.alexmcgil.blood_pressure.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Short.parseShort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class PressureRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    private final Long TEST_CHAT_ID = 123L;

    @Test
    void testSavePressureRecord() {
        // Given
        User user = new User();
        user.setTelegramID(TEST_CHAT_ID);
        user.setName("testuser");

        Pressure pressure = new Pressure();
        pressure.setSystolic((short)120);
        pressure.setDiastolic((short)80);
        pressure.setPulse((short)70);
        pressure.setTimePressure(LocalDateTime.now());

        user.setUserPressures(new ArrayList<>());
        user.getUserPressures().add(pressure);

        // When
        User savedUser = userRepository.save(user);

        // Then
        Optional<User> foundUser = userRepository.getRecords(TEST_CHAT_ID);
        assertTrue(foundUser.isPresent());
        assertEquals(1, foundUser.get().getUserPressures().size());

        Pressure savedPressure = foundUser.get().getUserPressures().getFirst();
        assertEquals(parseShort("120"), savedPressure.getSystolic());
        assertEquals(parseShort("80"), savedPressure.getDiastolic());
        assertEquals(parseShort("70"), savedPressure.getPulse());
    }

    @Test
    void testGetUserRecords() {
        // Given
        User user = new User();
        user.setTelegramID(TEST_CHAT_ID);
        user.setName("testuser");

        List<Pressure> pressures = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        pressures.add(createPressure((short)120, (short)80, (short)70, now));
        pressures.add(createPressure((short)130, (short)85, (short)75, now.minusDays(1)));

        user.setUserPressures(pressures);
        userRepository.save(user);

        // When
        Optional<User> result = userRepository.getRecords(TEST_CHAT_ID);

        // Then
        assertTrue(result.isPresent());
        assertEquals(2, result.get().getUserPressures().size());
        assertTrue(result.get().getUserPressures().stream()
                .anyMatch(p -> p.getSystolic() == 120 && p.getDiastolic() == 80));
    }

    private Pressure createPressure(short systolic, short diastolic, short pulse, LocalDateTime time) {
        Pressure pressure = new Pressure();
        pressure.setSystolic(systolic);
        pressure.setDiastolic(diastolic);
        pressure.setPulse(pulse);
        pressure.setTimePressure(time);
        return pressure;
    }
}