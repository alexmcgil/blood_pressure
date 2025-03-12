package su.alexmcgil.blood_pressure.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class StringParserTest {

    @ParameterizedTest
    @MethodSource("validPressureInputs")
    void testParseValidPressureString(String input, short expectedSystolic, short expectedDiastolic, short expectedPulse) {
        // Act
        short[] result = StringParser.parsePressureString(input);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(expectedSystolic, result[0]);
        assertEquals(expectedDiastolic, result[1]);
        assertEquals(expectedPulse, result[2]);
    }

    private static Stream<Arguments> validPressureInputs() {
        return Stream.of(
                Arguments.of("120/80/70", (short)120, (short)80, (short)70),
                Arguments.of("120 80 70", (short)120, (short)80, (short)70),
                Arguments.of("120, 80, 70", (short)120, (short)80, (short)70),
                Arguments.of("120;80;70", (short)120, (short)80, (short)70),
                Arguments.of("120-80-70", (short)120, (short)80, (short)70)
        );
    }

    @Test
    void testParseInvalidPressureString() {
        // Act & Assert
        assertThrows(NumberFormatException.class, () -> StringParser.parsePressureString("Invalid Input"));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> StringParser.parsePressureString("120/80"));
        assertThrows(NumberFormatException.class, () -> StringParser.parsePressureString("abc/def/ghi"));
    }
}



