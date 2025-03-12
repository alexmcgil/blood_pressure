package su.alexmcgil.blood_pressure.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import su.alexmcgil.blood_pressure.entity.Pressure;
import su.alexmcgil.blood_pressure.entity.User;
import su.alexmcgil.blood_pressure.repository.PressureRepository;
import su.alexmcgil.blood_pressure.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PressureRepository pressureRepository;

    @Test
    void testSaveAndRetrieveUser() {
        // Arrange
        User user = new User();
        user.setTelegramID(123456789L);
        user.setName("Integration Test User");

        // Act
        User savedUser = userRepository.save(user);
        Optional<User> retrievedUser = userRepository.findById(user.getTelegramID().toString());

        // Assert
        assertTrue(retrievedUser.isPresent());
        assertEquals(user.getTelegramID(), retrievedUser.get().getTelegramID());
        assertEquals(user.getName(), retrievedUser.get().getName());
    }

    @Test
    void testSaveUserWithPressures() {
        // Arrange
        User user = new User();
        user.setTelegramID(223456789L);
        user.setName("User With Pressures");

        Pressure pressure = new Pressure();
        pressure.setSystolic((short) 120);
        pressure.setDiastolic((short) 80);
        pressure.setPulse((short) 70);
        pressure.setTimePressure(LocalDateTime.now());

        Pressure savedPressure = pressureRepository.save(pressure);

        List<Pressure> pressures = new ArrayList<>();
        pressures.add(savedPressure);
        user.setUserPressures(pressures);

        // Act
        User savedUser = userRepository.save(user);
        Optional<User> retrievedUser = userRepository.getRecords(user.getTelegramID());

        // Assert
        assertTrue(retrievedUser.isPresent());
        assertFalse(retrievedUser.get().getUserPressures().isEmpty());
        assertEquals(savedPressure.getSystolic(), retrievedUser.get().getUserPressures().get(0).getSystolic());
    }
}



