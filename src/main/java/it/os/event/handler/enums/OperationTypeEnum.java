package it.os.event.handler.enums;

import lombok.Getter;

public enum OperationTypeEnum {

    GENERATOR_REPLACING("Sost. generatore", "Rimpiazzo generatore"),
    GEARBOX_REPLACING("Sost. Gearbox", "Rimpiazzo Gearbox"),
    ROOT_JOINT("Root Joint", "Root joint"),
    PITCH_ROD("Asta pitch", "Asta pitch");

    @Getter
    private String name;

    @Getter
    private String description;

    OperationTypeEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static OperationTypeEnum get(final String name) {
        for (OperationTypeEnum operation : OperationTypeEnum.values()) {
            if (operation.getName().equals(name)) {
                return operation;
            }
        }
        return null;
    }
}
