package su.alexmcgil.blood_pressure.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserCreation() {
        // Arrange
        Long telegramId = 123456789L;
        String name = "Тестовый пользователь";

        // Act
        User user = new User();
        user.setTelegramID(telegramId);
        user.setName(name);

        // Assert
        assertEquals(telegramId, user.getTelegramID());
        assertEquals(name, user.getName());
        assertNotNull(user.getUserPressures());
        assertFalse(user.isUserWantChangeName());
    }

    @Test
    void testAddPressure() {
        // Arrange
        User user = new User();
        Pressure pressure = new Pressure();
        pressure.setSystolic((short) 120);
        pressure.setDiastolic((short) 80);
        pressure.setPulse((short) 70);
        pressure.setTimePressure(LocalDateTime.now());

        // Act
        List<Pressure> pressures = new ArrayList<>();
        pressures.add(pressure);
        user.setUserPressures(pressures);

        // Assert
        assertEquals(1, user.getUserPressures().size());
        assertEquals(pressure, user.getUserPressures().get(0));
    }

    @Test
    void testToString() {
        // Arrange
        User user = new User();
        user.setTelegramID(123456789L);
        user.setName("Тестовый пользователь");

        // Act
        String result = user.toString();

        // Assert
        assertTrue(result.contains("123456789"));
        assertTrue(result.contains("Тестовый пользователь"));
    }
}