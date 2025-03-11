package su.alexmcgil.blood_pressure.it;

import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import su.alexmcgil.blood_pressure.telegram.ReadWriteBot;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WebhookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReadWriteBot writeBot;

    WebhookControllerTest(ReadWriteBot writeBot) {
        this.writeBot = writeBot;
    }

    @Test
    void testWebhookEndpoint() throws Exception {
        // Given
        String updateJson = """
            {
                "update_id": 123456789,
                "message": {
                    "message_id": 1,
                    "from": {
                        "id": 123456789,
                        "first_name": "Test",
                        "username": "testuser"
                    },
                    "chat": {
                        "id": 123456789,
                        "type": "private"
                    },
                    "date": 1615456789,
                    "text": "120 80 70"
                }
            }
            """;

        SendMessage response = new SendMessage();
        response.setChatId("123456789");
        response.setText("Запись добавлена");

        Mockito.doReturn(response).when(writeBot).onWebhookUpdateReceived(any(Update.class));

        // When & Then
        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chat_id").value("123456789"))
                .andExpect(jsonPath("$.text").value("Запись добавлена"));
    }
}