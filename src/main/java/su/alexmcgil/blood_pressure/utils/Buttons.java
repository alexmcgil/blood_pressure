package su.alexmcgil.blood_pressure.utils;

public enum Buttons {
    WRITE_DATA("Внести запись"),
    GET_RECORDS("Посмотреть все записи"),
    SETTINGS("Настройки"),
    CHANGE_NAME("Изменить имя"),
    BACK("Назад"),
    AUTHOR("Связь с автором"),
    RESTART("Перезапустить бота");

    private final String buttonName;

    Buttons(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}
