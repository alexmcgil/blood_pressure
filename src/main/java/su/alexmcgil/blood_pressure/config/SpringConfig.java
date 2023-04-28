package su.alexmcgil.blood_pressure.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import su.alexmcgil.blood_pressure.telegram.ReadWriteBot;
import su.alexmcgil.blood_pressure.telegram.handlers.CallbackQueryHandler;
import su.alexmcgil.blood_pressure.telegram.handlers.MessageHandler;

@Configuration
@AllArgsConstructor
public class SpringConfig {
    private final TelegramConfig telegramConfig;

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(telegramConfig.getWebhookpath()).build();
    }

    @Bean
    public ReadWriteBot springWebhookBot(SetWebhook setWebhook,
                                         MessageHandler messageHandler,
                                         CallbackQueryHandler callbackQueryHandler) {
        ReadWriteBot bot = new ReadWriteBot(setWebhook, messageHandler, callbackQueryHandler);

        bot.setBotPath(telegramConfig.getWebhookpath());
        bot.setBotUsername(telegramConfig.getBotName());
        bot.setBotToken(telegramConfig.getBotToken());

        return bot;
    }
}
