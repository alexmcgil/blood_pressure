package su.alexmcgil.blood_pressure.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import su.alexmcgil.blood_pressure.entity.Pressure;
import su.alexmcgil.blood_pressure.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveUser() {
        // Arrange
        User user = new User();
        user.setTelegramID(123456789L);
        user.setName("Тестовый пользователь");

        // Act
        User savedUser = userRepository.save(user);

        // Assert
        assertEquals(user.getTelegramID(), savedUser.getTelegramID());
        assertEquals(user.getName(), savedUser.getName());
    }

    @Test
    void testFindById() {
        // Arrange
        User user = new User();
        user.setTelegramID(123456789L);
        user.setName("Тестовый пользователь");
        entityManager.persist(user);
        entityManager.flush();

        // Act
        Optional<User> foundUser = userRepository.findById(user.getTelegramID().toString());

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(user.getName(), foundUser.get().getName());
    }

    @Test
    void testGetRecords() {
        // Arrange
        User user = new User();
        user.setTelegramID(123456789L);
        user.setName("Тестовый пользователь");

        Pressure pressure = new Pressure();
        pressure.setSystolic((short) 120);
        pressure.setDiastolic((short) 80);
        pressure.setPulse((short) 70);
        pressure.setTimePressure(LocalDateTime.now());
        entityManager.persist(pressure);

        List<Pressure> pressures = new ArrayList<>();
        pressures.add(pressure);
        user.setUserPressures(pressures);

        entityManager.persist(user);
        entityManager.flush();

        // Act
        Optional<User> foundUser = userRepository.getRecords(user.getTelegramID());

        // Assert
        assertTrue(foundUser.isPresent());
        assertFalse(foundUser.get().getUserPressures().isEmpty());
    }
}



