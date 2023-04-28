package su.alexmcgil.blood_pressure.telegram.handlers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import su.alexmcgil.blood_pressure.entity.Pressure;
import su.alexmcgil.blood_pressure.entity.User;
import su.alexmcgil.blood_pressure.repository.UserRepository;
//import su.alexmcgil.blood_pressure.telegram.TelegramApiClient;
import su.alexmcgil.blood_pressure.telegram.keyboard.ReplyKeyboardMaker;
import su.alexmcgil.blood_pressure.utils.Buttons;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MessageHandler {

//    TelegramApiClient telegramApiClient;
    ReplyKeyboardMaker replyKeyboardMaker;
    private final UserRepository userRepository;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss");

    public BotApiMethod<?> answerMessage(Message message) {
        String chatId = message.getChatId().toString();

        String username = message.getFrom().getUserName();

        String inputText = message.getText();

        if (inputText == null) {
            throw new IllegalArgumentException();
        } else if (inputText.equals("/start") | inputText.equals(Buttons.RESTART.getButtonName())) {
            return getStartMessage(chatId, username);
        } else if (inputText.equals(Buttons.WRITE_DATA.getButtonName())) {
            return getWriteDataMessage(chatId);
        } else if (inputText.equals(Buttons.GET_RECORDS.getButtonName())) {
            return getRecordsMessage(chatId);
        } else if (inputText.chars().anyMatch(Character::isDigit)) {
            return getWriteSuccessMessage(chatId, inputText);
        } else if (inputText.equals(Buttons.SETTINGS.getButtonName())) {
            return getSettingsMessage(chatId);
        } else if (inputText.equals(Buttons.BACK.getButtonName())) {
            return getBackMessage(chatId);
        } else if (inputText.equals(Buttons.AUTHOR.getButtonName())) {
            return new SendMessage(chatId, "@alexmcgil");
        } else if (inputText.equals(Buttons.CHANGE_NAME.getButtonName())) {
            return getWantChangeNameMessage(chatId);
        } else {
            AtomicBoolean flag = new AtomicBoolean(false);
            System.out.println(flag);
            userRepository.findById(chatId).ifPresent(usr -> flag.set(true));
            System.out.println(flag);
            if (flag.get()) {
                return getChangeNameMessage(chatId, inputText);
            } else {
                return new SendMessage(chatId, "Это пока в разработке..");
            }
        }
    }

    private SendMessage getStartMessage(String chatId, String username) {
        SendMessage sendMessage = new SendMessage(chatId, "Привет, это бот для сохранения и вывода записей давления.");
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboard());
        User user = new User();
        user.setTelegramID(Long.parseLong(chatId));
        user.setName(username);
        userRepository.save(user);
        return sendMessage;
    }

    private SendMessage getWriteDataMessage(String chatId) {
        return new SendMessage(chatId, "Запишите показатели в формате XXX YYY ZZ");
    }

    private SendMessage getWriteSuccessMessage(String chatId, String inputMessage) {
        Optional<User> user = userRepository.findById(chatId);
        List<String> stringList = Arrays.stream(inputMessage.split(" ")).toList();
        Pressure pressure = new Pressure();
        LocalDateTime ldt = LocalDateTime.now();
        System.out.println(stringList);
        pressure.setSystolic(Short.parseShort(stringList.get(0)));
        pressure.setDiastolic(Short.parseShort(stringList.get(1)));
        pressure.setPulse(Short.parseShort(stringList.get(2)));
        pressure.setTimePressure(ldt);
        AtomicReference<String> message = new AtomicReference<>("Запись не добавлена, проверьте формат записи и попробуйте еще раз.");

        user.ifPresent(usr -> {
            List<Pressure> usrPressures = usr.getUserPressures();
            usrPressures.add(pressure);
            usr.setUserPressures(usrPressures);
            userRepository.save(usr);
            message.set("Запись добавлена, время записи: " + pressure.getTimePressure().format(dtf));
        });

        return new SendMessage(chatId, message.get());
    }

    private SendMessage getRecordsMessage(String chatId) {
        Optional<User> user = userRepository.getRecords(Long.parseLong(chatId));


        StringBuilder message = new StringBuilder("Записей нет");
        user.ifPresent(user1 -> {
            message.delete(0, 11);
            message.append("Имя: ").append(user1.getName()).append("\n");
            user1.getUserPressures().forEach(pres -> message.append("---").append("\n")
                    .append("Время: ").append(pres.getTimePressure().format(dtf)).append("\n")
                    .append("Давление:").append(pres.getSystolic()).append("/").append(pres.getDiastolic()).append("\n")
                    .append("Пульс: ").append(pres.getPulse()).append("\n"));
        });

        return new SendMessage(chatId, message.toString());
    }


    private BotApiMethod<?> getSettingsMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "⚙");
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getSettingsMenuKeyboard());

        return sendMessage;
    }

    private BotApiMethod<?> getBackMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "\uD83C\uDFE0");
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboard());

        return sendMessage;
    }

    private BotApiMethod<?> getWantChangeNameMessage(String chatId) {
        Optional<User> user = userRepository.findById(chatId);
        user.ifPresent(usr -> usr.setUserWantChangeName(true));


        SendMessage sendMessage = new SendMessage(chatId, "Укажите желаемое имя");
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getWantChangeName());


        return sendMessage;
    }

    private BotApiMethod<?> getChangeNameMessage(String chatId, String input) {
        Optional<User> user = userRepository.findById(chatId);
        AtomicReference<String> message = new AtomicReference<>("Что-то пошло не так.");
        user.ifPresent(usr -> {
            usr.setName(input);
            usr.setUserWantChangeName(false);
            message.set("Ваше имя изменено на " + usr.getName());
            userRepository.save(usr);
        });


        SendMessage sendMessage = new SendMessage(chatId, message.get());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboard());

        return sendMessage;
    }

}
