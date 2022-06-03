package it.os.event.handler.enums;

import lombok.Getter;

public enum TurbineStateEnum {

    MARCHING("In marcia", "Turbina in marcia"),
    STANDING("Ferma", "Turbine ferma"),
    LIMITED("Limitata", "Turbina limitata");

    @Getter
    private String name;

    @Getter
    private String description;

    TurbineStateEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static TurbineStateEnum get(final String name) {
        for (TurbineStateEnum operation : TurbineStateEnum.values()) {
            if (operation.getName().equals(name)) {
                return operation;
            }
        }
        return null;
    }
}
