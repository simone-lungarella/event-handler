package it.os.event.handler.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum OperationTypeEnum {

    SOST_TRAVERSA("Sostituzione Traversa", "Sostituzione Traversa"),
    RIP_PALE("Riparazione pale", "Riparazione pale"),
    SOST_TRAFO("Sostituzione trafo", "Sostituzione trafo"),
    SOST_GENERATORE("Sostituzione generatore", "Sostituzione generatore"),
    SOST_RALLA("Sostituzione Ralla", "Sostituzione Ralla"),
    PULIZIA_TUBOLARE("Pulizia Tubolare", "Pulizia Tubolare"),
    SOST_IMS("Sostituzione IMS", "Sostituzione IMS"),
    SERRAGGIO_BULLONI_NOSE_CONE("Serraggio bulloni nose cone", "Serraggio bulloni nose cone"),
    SOST_STELLA_HUB("Sostituzione Stella HUB", "Sostituzione Stella HUB"),
    SOST_PALA_DISCESA_ROTORE("Sostituzione Pala discesa rotore", "Sostituzione Pala discesa rotore"),
    SOST_CUSCINETTO("Sostituzione cuscinetto", "Sostituzione cuscinetto"),
    SOST_ASTA_PITCH("Sostituzione Asta Pitch", "Sostituzione Asta Pitch"),
    SOST_YAW_GEAR("Sostituzione Yaw Gear", "Sostituzione Yaw Gear"),
    SOST_GEARBOX("Sostituzione Gearbox", "Sostituzione Gearbox"),
    SOST_PALA_RJ("Sostituzione Pala RJ", "Sostituzione Pala RJ"),
    SOST_ALBERO_LENTO("Sostituzione Albero lento", "Sostituzione Albero lento"),
    ISPEZIONE_RJ("Ispezione RJ", "Ispezione RJ"),
    SOST_BLADE_BEARING("Sostituzione Blade Bearing", "Sostituzione Blade Bearing"),
    SOST_MAIN_BEARING("Sostituzione Main Bearing", "Sostituzione Main Bearing"),
    MANUTENZIONE_ORDINARIA("Manutenzione ordinaria viabilità", "Manutenzione ordinaria viabilità"),
    ATTIVITA_VARIE("Attività varie", "Attività varie"),
    BONIFICA_AMBIENTALE("Bonifica ambientale", "Bonifica ambientale"),
    SOST_PALA("Sostituzione Pala", "Sostituzione pala"),
    SOST_PITCH_GEAR("Sostituzione Pitch Gear", "Sostituzione Pitch Gear");

    private String name;
    private String description;

    public static OperationTypeEnum get(final String name) {
        for (OperationTypeEnum operation : OperationTypeEnum.values()) {
            if (operation.getName().equals(name)) {
                return operation;
            }
        }
        return null;
    }
}
