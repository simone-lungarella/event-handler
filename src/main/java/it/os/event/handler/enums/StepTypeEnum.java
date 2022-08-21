package it.os.event.handler.enums;

import lombok.Getter;

public enum StepTypeEnum {

    SOPRALLUOGO(1, "Sopralluogo", "Sopralluogo zona cantiere"),
    REDAZIONE_REPORT(2, "Redazione report", "Redazione report precedente ad inizio cantiere"),
    PERMITTING(3, "Permitting", "Invio report a permitting"),
    COMPLETAMENTO_OOCC(4, "Fine attività OOCC", "Completamento attività OOCC"),
    COMPLETAMENTO_EEMM(5, "Fine attività EEMM", "Completamento attività EEMM"),
    SMONTAGGIO_PIAZZOLA(6, "Smontaggio piazzola", "Smontaggio piazzola prima del completamento del cantiere"),
    CHIUSURA_PERMITTING(7, "Chiusura permitting", "Chiusura autorizzativa permitting cantiere");

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
