package su.alexmcgil.blood_pressure.telegram.keyboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import su.alexmcgil.blood_pressure.utils.Buttons;

import static org.junit.jupiter.api.Assertions.*;

class ReplyKeyboardMakerTest {

    private ReplyKeyboardMaker keyboardMaker;

    @BeforeEach
    void setUp() {
        keyboardMaker = new ReplyKeyboardMaker();
    }

    @Test
    void testGetMainMenuKeyboard() {
        // Act
        ReplyKeyboardMarkup keyboard = keyboardMaker.getMainMenuKeyboard();

        // Assert
        assertNotNull(keyboard);
        assertNotNull(keyboard.getKeyboard());
        assertEquals(2, keyboard.getKeyboard().size());
        assertTrue(keyboard.getKeyboard().get(0).get(0).getText().equals(Buttons.WRITE_DATA.getButtonName()));
        assertTrue(keyboard.getKeyboard().get(1).get(0).getText().equals(Buttons.GET_RECORDS.getButtonName()));
        assertTrue(keyboard.getKeyboard().get(1).get(1).getText().equals(Buttons.SETTINGS.getButtonName()));
        assertTrue(keyboard.isResizeKeyboard());
    }

    @Test
    void testGetSettingsMenuKeyboard() {
        // Act
        ReplyKeyboardMarkup keyboard = keyboardMaker.getSettingsMenuKeyboard();

        // Assert
        assertNotNull(keyboard);
        assertNotNull(keyboard.getKeyboard());
        assertEquals(2, keyboard.getKeyboard().size());
        assertTrue(keyboard.getKeyboard().get(0).get(0).getText().equals(Buttons.CHANGE_NAME.getButtonName()));
        assertTrue(keyboard.getKeyboard().get(0).get(1).getText().equals(Buttons.AUTHOR.getButtonName()));
        assertTrue(keyboard.getKeyboard().get(1).get(0).getText().equals(Buttons.BACK.getButtonName()));
        assertTrue(keyboard.isResizeKeyboard());
    }
}