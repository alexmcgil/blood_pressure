package su.alexmcgil.blood_pressure.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import su.alexmcgil.blood_pressure.entity.Pressure;
import su.alexmcgil.blood_pressure.entity.User;
import su.alexmcgil.blood_pressure.repository.PressureRepository;
import su.alexmcgil.blood_pressure.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PressureRepository pressureRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private Pressure testPressure;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setTelegramID(123456789L);
        testUser.setName("Test User");

        testPressure = new Pressure();
        testPressure.setSystolic((short) 120);
        testPressure.setDiastolic((short) 80);
        testPressure.setPulse((short) 70);
        testPressure.setTimePressure(LocalDateTime.now());
    }

    @Test
    void testFindOrCreateUser() {
        // Arrange
        Long telegramId = 123456789L;
        String userName = "Test User";
        when(userRepository.findById(telegramId.toString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User result = userService.findOrCreateUser(telegramId, userName);

        // Assert
        assertNotNull(result);
        assertEquals(telegramId, result.getTelegramID());
        assertEquals(userName, result.getName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testFindOrCreateUserExisting() {
        // Arrange
        Long telegramId = 123456789L;
        String userName = "Test User";
        when(userRepository.findById(telegramId.toString())).thenReturn(Optional.of(testUser));

        // Act
        User result = userService.findOrCreateUser(telegramId, userName);

        // Assert
        assertNotNull(result);
        assertEquals(telegramId, result.getTelegramID());
        assertEquals(userName, result.getName());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testAddPressure() {
        // Arrange
        Short systolic = 120;
        Short diastolic = 80;
        Short pulse = 70;

        when(userRepository.findById(testUser.getTelegramID().toString())).thenReturn(Optional.of(testUser));
        when(pressureRepository.save(any(Pressure.class))).thenReturn(testPressure);

        // Act
        boolean result = userService.addPressure(testUser.getTelegramID(), systolic, diastolic, pulse);

        // Assert
        assertTrue(result);
        verify(pressureRepository).save(any(Pressure.class));
        verify(userRepository).save(testUser);
    }

    @Test
    void testAddPressureUserNotFound() {
        // Arrange
        Short systolic = 120;
        Short diastolic = 80;
        Short pulse = 70;

        when(userRepository.findById(testUser.getTelegramID().toString())).thenReturn(Optional.empty());

        // Act
        boolean result = userService.addPressure(testUser.getTelegramID(), systolic, diastolic, pulse);

        // Assert
        assertFalse(result);
        verify(pressureRepository, never()).save(any(Pressure.class));
    }

    @Test
    void testGetUserRecords() {
        // Arrange
        List<Pressure> pressures = new ArrayList<>();
        pressures.add(testPressure);
        testUser.setUserPressures(pressures);

        when(userRepository.getRecords(testUser.getTelegramID())).thenReturn(Optional.of(testUser));

        // Act
        List<String> results = userService.getUserRecords(testUser.getTelegramID());

        // Assert
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertTrue(results.get(0).contains("120"));
        assertTrue(results.get(0).contains("80"));
        assertTrue(results.get(0).contains("70"));
    }

    @Test
    void testGetUserRecordsEmpty() {
        // Arrange
        when(userRepository.getRecords(testUser.getTelegramID())).thenReturn(Optional.empty());

        // Act
        List<String> results = userService.getUserRecords(testUser.getTelegramID());

        // Assert
        assertTrue(results.isEmpty());
    }

    @Test
    void testChangeUserNameFlag() {
        // Arrange
        when(userRepository.findById(testUser.getTelegramID().toString())).thenReturn(Optional.of(testUser));

        // Act
        boolean result = userService.changeUserNameFlag(testUser.getTelegramID());

        // Assert
        assertTrue(result);
        assertTrue(testUser.isUserWantChangeName());
        verify(userRepository).save(testUser);
    }

    @Test
    void testChangeUserNameFlagUserNotFound() {
        // Arrange
        when(userRepository.findById(testUser.getTelegramID().toString())).thenReturn(Optional.empty());

        // Act
        boolean result = userService.changeUserNameFlag(testUser.getTelegramID());

        // Assert
        assertFalse(result);
    }

    @Test
    void testChangeUserName() {
        // Arrange
        String newName = "New Test User";
        testUser.setUserWantChangeName(true);
        when(userRepository.findById(testUser.getTelegramID().toString())).thenReturn(Optional.of(testUser));

        // Act
        boolean result = userService.changeUserName(testUser.getTelegramID(), newName);

        // Assert
        assertTrue(result);
        assertEquals(newName, testUser.getName());
        assertFalse(testUser.isUserWantChangeName());
        verify(userRepository).save(testUser);
    }
}



