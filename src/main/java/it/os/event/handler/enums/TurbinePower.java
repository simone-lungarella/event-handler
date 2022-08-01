package it.os.event.handler.enums;

import lombok.Getter;

public enum TurbinePower {

    MEGAWATT("Megawatt"),
    KILOWATT("Kilowatt");

    @Getter
    private String name;

    TurbinePower(String name) {
        this.name = name;
    }

    public static TurbinePower get(final String name) {
        for (TurbinePower type : TurbinePower.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
