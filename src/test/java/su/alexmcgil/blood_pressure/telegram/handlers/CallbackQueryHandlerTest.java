package su.alexmcgil.blood_pressure.telegram.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.junit.jupiter.api.Assertions.*;

class CallbackQueryHandlerTest {

    private CallbackQueryHandler callbackQueryHandler;
    private CallbackQuery callbackQuery;
    private Message message;
    private Chat chat;

    @BeforeEach
    void setUp() {
        callbackQueryHandler = new CallbackQueryHandler();
        callbackQuery = new CallbackQuery();
        message = new Message();
        chat = new Chat();

        chat.setId(123456789L);
        message.setChat(chat);
        callbackQuery.setMessage(message);
    }

    @Test
    void testProcessCallbackQuery() {
        // Arrange
        callbackQuery.setData("test_data");

        // Act
        BotApiMethod<?> result = callbackQueryHandler.processCallbackQuery(callbackQuery);

        // Assert
        assertTrue(result instanceof SendMessage);
        SendMessage sendMessage = (SendMessage) result;
        assertEquals(chat.getId().toString(), sendMessage.getChatId());
        assertTrue(sendMessage.getText().contains("Easter Egg!"));
        assertTrue(sendMessage.getText().contains("test_data"));
    }
}



