package su.alexmcgil.blood_pressure.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import su.alexmcgil.blood_pressure.telegram.ReadWriteBot;

@RestController
@AllArgsConstructor
public class WebhookController {
    private final ReadWriteBot writeBot;

    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return writeBot.onWebhookUpdateReceived(update);
    }
}
