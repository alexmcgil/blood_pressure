package su.alexmcgil.blood_pressure.telegram.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import su.alexmcgil.blood_pressure.repository.UserRepository;
import su.alexmcgil.blood_pressure.telegram.keyboard.ReplyKeyboardMaker;
import su.alexmcgil.blood_pressure.utils.Buttons;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageHandlerTest {

    @Mock
    private ReplyKeyboardMaker replyKeyboardMaker;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MessageHandler messageHandler;

    private Message message;
    private Chat chat;
    private User telegramUser;

    @BeforeEach
    void setUp() {
        message = new Message();
        chat = new Chat();
        telegramUser = new User();

        chat.setId(123456789L);
        message.setChat(chat);

        telegramUser.setId(123456789L);
        telegramUser.setUserName("testUser");
        message.setFrom(telegramUser);
    }

    @Test
    void testStartCommand() {
        // Arrange
        message.setText("/start");
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        // Act
        BotApiMethod<?> result = messageHandler.answerMessage(message);

        // Assert
        assertTrue(result instanceof SendMessage);
        SendMessage sendMessage = (SendMessage) result;
        assertEquals(chat.getId().toString(), sendMessage.getChatId());
        verify(replyKeyboardMaker).getMainMenuKeyboard();
    }

    @Test
    void testWriteDataButton() {
        // Arrange
        message.setText(Buttons.WRITE_DATA.getButtonName());
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        // Act
        BotApiMethod<?> result = messageHandler.answerMessage(message);

        // Assert
        assertTrue(result instanceof SendMessage);
        SendMessage sendMessage = (SendMessage) result;
        assertEquals(chat.getId().toString(), sendMessage.getChatId());
        assertTrue(sendMessage.getText().contains("Запишите показатели"));
    }

    @Test
    void testGetRecordsButton() {
        // Arrange
        message.setText(Buttons.GET_RECORDS.getButtonName());
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        when(userRepository.getRecords(any())).thenReturn(Optional.empty());

        // Act
        BotApiMethod<?> result = messageHandler.answerMessage(message);

        // Assert
        assertTrue(result instanceof SendMessage);
        SendMessage sendMessage = (SendMessage) result;
        assertEquals(chat.getId().toString(), sendMessage.getChatId());
        assertEquals("Записей нет", sendMessage.getText());
    }

    @Test
    void testSettingsButton() {
        // Arrange
        message.setText(Buttons.SETTINGS.getButtonName());
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        // Act
        BotApiMethod<?> result = messageHandler.answerMessage(message);

        // Assert
        assertTrue(result instanceof SendMessage);
        SendMessage sendMessage = (SendMessage) result;
        assertEquals(chat.getId().toString(), sendMessage.getChatId());
        verify(replyKeyboardMaker).getSettingsMenuKeyboard();
    }
}



