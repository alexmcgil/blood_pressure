package su.alexmcgil.blood_pressure.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import su.alexmcgil.blood_pressure.entity.Pressure;
import su.alexmcgil.blood_pressure.entity.User;
import su.alexmcgil.blood_pressure.repository.UserRepository;
import su.alexmcgil.blood_pressure.telegram.handlers.MessageHandler;
import su.alexmcgil.blood_pressure.telegram.keyboard.ReplyKeyboardMaker;
import su.alexmcgil.blood_pressure.utils.Buttons;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class MessageHandlerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReplyKeyboardMaker replyKeyboardMaker;

    @InjectMocks
    private MessageHandler messageHandler;

    private Message testMessage;
    private final Long TEST_CHAT_ID = 123L;

    @BeforeEach
    void setUp() {
        Chat testChat = new Chat();
        testChat.setId(TEST_CHAT_ID);
        testMessage = new Message();
        testMessage.setChat(testChat);
        org.telegram.telegrambots.meta.api.objects.User user = new org.telegram.telegrambots.meta.api.objects.User();
        user.setUserName("testuser");
        testMessage.setFrom(user);
    }

    @Test
    void testStartCommand() {
        // Given
        testMessage.setText("/start");
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        when(replyKeyboardMaker.getMainMenuKeyboard()).thenReturn(markup);

        // When
        BotApiMethod<?> response = messageHandler.answerMessage(testMessage);

        // Then
        assertInstanceOf(SendMessage.class, response);
        SendMessage sendMessage = (SendMessage) response;
        assertEquals(TEST_CHAT_ID.toString(), sendMessage.getChatId());
        assertTrue(sendMessage.getText().contains("Привет"));
    }

    @Test
    void testWriteDataCommand() {
        // Given
        testMessage.setText("120 80 70");
        User user = new User();
        user.setTelegramID(TEST_CHAT_ID);
        when(userRepository.findById(TEST_CHAT_ID.toString())).thenReturn(Optional.of(user));

        // When
        BotApiMethod<?> response = messageHandler.answerMessage(testMessage);

        // Then
        assertInstanceOf(SendMessage.class, response);
        SendMessage sendMessage = (SendMessage) response;
        assertTrue(sendMessage.getText().contains("Запись добавлена"));
    }

    @Test
    void testGetRecordsCommand() {
        // Given
        testMessage.setText(Buttons.GET_RECORDS.getButtonName());
        User user = new User();
        user.setTelegramID(TEST_CHAT_ID);
        user.setName("testuser");

        Pressure pressure = new Pressure();
        pressure.setSystolic((short)120);
        pressure.setDiastolic((short)80);
        pressure.setPulse((short)70);
        pressure.setTimePressure(LocalDateTime.now());
        user.setUserPressures(List.of(pressure));

        when(userRepository.getRecords(TEST_CHAT_ID)).thenReturn(Optional.of(user));

        // When
        BotApiMethod<?> response = messageHandler.answerMessage(testMessage);

        // Then
        assertInstanceOf(SendMessage.class, response);
        SendMessage sendMessage = (SendMessage) response;
        assertTrue(sendMessage.getText().contains("120/80"));
    }
}