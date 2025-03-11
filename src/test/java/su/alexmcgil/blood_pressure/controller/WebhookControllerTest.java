package su.alexmcgil.blood_pressure.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import su.alexmcgil.blood_pressure.telegram.ReadWriteBot;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class WebhookControllerTest {

    @Mock
    private ReadWriteBot writeBot;

    private WebhookController webhookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webhookController = new WebhookController(writeBot);
    }

    @Test
    void onUpdateReceived_ShouldDelegateToBot() {
        // Arrange
        Update update = new Update();
        SendMessage expectedResponse = SendMessage.builder()
                .text("Test response")
                .build();
        when(writeBot.onWebhookUpdateReceived(update)).thenReturn(expectedResponse);

        // Act
        BotApiMethod<?> response = webhookController.onUpdateReceived(update);

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(writeBot, times(1)).onWebhookUpdateReceived(update);
    }
} 