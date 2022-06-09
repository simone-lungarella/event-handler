package it.os.event.handler.enums;

import lombok.Getter;

public enum StepTypeEnum {

    SOPRALLUOGO(1, "Sopralluogo", "Sopralluogo zona cantiere"),
    REDAZIONE_REPORT(2, "Redazione report", "Redazione report precedente ad inizio cantiere"),
    CME_ODC(3, "CME/ODC", "CME/ODC"),
    APERTURA_CANTIERE(4, "Richiesta apertura cantiere", "Invio report a permitting"),
    PERMITTING(5, "Permitting", "Permitting"),
    DOCUMENTAZIONE_SICUREZZA(6, "Documentazione sicurezza", "Documentazione sicurezza successiva all'avvio del cantiere"),
    COMPLETAMENTO_OOCC(7, "Fine attività OOCC", "Completamento attività OOCC"),
    COMPLETAMENTO_EEMM(8, "Fine attività EEMM", "Completamento attività EEMM"),
    SMONTAGGIO_PIAZZOLA(9, "Smontaggio piazzola", "Smontaggio piazzola prima del completamento del cantiere");

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
