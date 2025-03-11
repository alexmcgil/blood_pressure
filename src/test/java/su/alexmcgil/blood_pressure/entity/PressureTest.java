package su.alexmcgil.blood_pressure.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class PressureTest {

    @Test
    void testPressureCreation() {
        // Arrange
        Long id = 1L;
        Short systolic = 120;
        Short diastolic = 80;
        Short pulse = 70;
        LocalDateTime timePressure = LocalDateTime.now();

        // Act
        Pressure pressure = new Pressure();
        pressure.setId(id);
        pressure.setSystolic(systolic);
        pressure.setDiastolic(diastolic);
        pressure.setPulse(pulse);
        pressure.setTimePressure(timePressure);

        // Assert
        assertEquals(id, pressure.getId());
        assertEquals(systolic, pressure.getSystolic());
        assertEquals(diastolic, pressure.getDiastolic());
        assertEquals(pulse, pressure.getPulse());
        assertEquals(timePressure, pressure.getTimePressure());
    }

    @Test
    void testPressureValidation() {
        // Arrange
        Pressure pressure = new Pressure();
        
        // Act & Assert
        Short invalidValue = -1;
        
        pressure.setSystolic(invalidValue);
        assertNull(pressure.getSystolic());
        
        pressure.setDiastolic(invalidValue);
        assertNull(pressure.getDiastolic());
        
        pressure.setPulse(invalidValue);
        assertNull(pressure.getPulse());
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
        assertTrue(result.contains("systolic=120"));
        assertTrue(result.contains("diastolic=80"));
        assertTrue(result.contains("pulse=70"));
        assertTrue(result.contains("timePressure=" + now));
    }
} 