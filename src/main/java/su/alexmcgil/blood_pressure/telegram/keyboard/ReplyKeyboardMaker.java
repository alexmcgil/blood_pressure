package su.alexmcgil.blood_pressure.telegram.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import su.alexmcgil.blood_pressure.utils.Buttons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ReplyKeyboardMaker {
    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(Buttons.WRITE_DATA.getButtonName()));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(Buttons.GET_RECORDS.getButtonName()));
        row2.add(new KeyboardButton(Buttons.SETTINGS.getButtonName()));

        return getReplyKeyboardMarkup(row1, row2);
    }

    public ReplyKeyboardMarkup getSettingsMenuKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(Buttons.CHANGE_NAME.getButtonName()));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(Buttons.AUTHOR.getButtonName()));
        row2.add(new KeyboardButton(Buttons.BACK.getButtonName()));

        KeyboardRow row3 = new KeyboardRow();
        row1.add(new KeyboardButton(Buttons.RESTART.getButtonName()));

        return getReplyKeyboardMarkup(row1, row2, row3);
    }

    public ReplyKeyboardMarkup getWantChangeName() {
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton(Buttons.BACK.getButtonName()));

        return getReplyKeyboardMarkup(row);
    }

    private ReplyKeyboardMarkup getReplyKeyboardMarkup(KeyboardRow ...rows) {
        List<KeyboardRow> keyboard = new ArrayList<>(Arrays.asList(rows));

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }
}
