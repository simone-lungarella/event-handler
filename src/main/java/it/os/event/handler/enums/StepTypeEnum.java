package it.os.event.handler.enums;

import lombok.Getter;

public enum StepTypeEnum {

    SOPRALLUOGO(1, "Sopralluogo", "Sopralluogo zona cantiere"),
    REDAZIONE_REPORT(2, "Redazione report", "Redazione report precedente ad inizio cantiere"),
    APERTURA_CANTIERE(3, "Apertura cantiere", "Avvio attività cantiere"),
    PERMITTING(4, "Permitting", "Permitting"),
    DOCUMENTAZIONE_SICUREZZA(5, "Documentazione sicurezza", "Documentazione sicurezza successiva all'avvio del cantiere"),
    COMPLETAMENTO_OOCC(6, "Completamento attività OOCC", "Completamento attività OOCC"),
    COMPLETAMENTO_EEMM(7, "Completamento attività EEMM", "Completamento attività EEMM"),
    SMONTAGGIO_PIAZZOLA(8, "Smontaggio piazzola", "Smontaggio piazzola prima del completamento del cantiere"),
    CHIUSURA_CANTIERE(9, "Chiusura cantiere", "Chiusura cantiere completo");

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
