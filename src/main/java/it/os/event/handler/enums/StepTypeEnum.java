package it.os.event.handler.enums;

import lombok.Getter;

public enum StepTypeEnum {

    SOPRALLUOGO(1, "Sopralluogo", "Sopralluogo in sito"),
    REDAZIONE_REPORT(2, "Redazione report", "Redazione elaborato grafico attività adeguamento viabilità e piazzola"),
    PERMITTING(3, "Permitting", "Rilascio titolo abilitativo"),
    COMPLETAMENTO_OOCC(4, "Fine attività OOCC", "Completamento attività opere civili"),
    COMPLETAMENTO_EEMM(5, "Fine attività EEMM", "Completamento attività elettromeccaniche"),
    SMONTAGGIO_PIAZZOLA(6, "Smontaggio piazzola", "Smontaggio piazzola e ripristino aree ante operam"),
    CHIUSURA_PERMITTING(7, "Chiusura permitting", "Comunicazione enti chiusura cantiere");

    @Getter
    private Integer order;

    @Getter
    private String name;

    @Getter
    private String description;

    StepTypeEnum(Integer inOrder, String name, String description) {
        this.name = name;
        this.description = description;
        this.order = inOrder;
    }

    public static StepTypeEnum get(final String name) {
        for (StepTypeEnum stepTypeEnum : StepTypeEnum.values()) {
            if (stepTypeEnum.getName().equals(name)) {
                return stepTypeEnum;
            }
        }
        return null;
    }
}
