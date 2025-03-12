package su.alexmcgil.blood_pressure.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PressureTest {

    @Test
    void testPressureCreation() {
        // Arrange
        Short systolic = 120;
        Short diastolic = 80;
        Short pulse = 70;
        LocalDateTime now = LocalDateTime.now();

        // Act
        Pressure pressure = new Pressure();
        pressure.setSystolic(systolic);
        pressure.setDiastolic(diastolic);
        pressure.setPulse(pulse);
        pressure.setTimePressure(now);

        // Assert
        assertEquals(systolic, pressure.getSystolic());
        assertEquals(diastolic, pressure.getDiastolic());
        assertEquals(pulse, pressure.getPulse());
        assertEquals(now, pressure.getTimePressure());
    }

    @Test
    void testToString() {
        // Arrange
        Pressure pressure = new Pressure();
        pressure.setSystolic((short) 120);
        pressure.setDiastolic((short) 80);
        pressure.setPulse((short) 70);
        LocalDateTime now = LocalDateTime.now();
        pressure.setTimePressure(now);

        // Act
        String result = pressure.toString();

        // Assert
        assertTrue(result.contains("120"));
        assertTrue(result.contains("80"));
        assertTrue(result.contains("70"));
    }
}