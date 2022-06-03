package it.os.event.handler.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "event")
public class EventETY {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column
    @NonNull
    private String turbineName;

    @Column
    @NonNull
    private String description;

    @Column
    @NonNull
    private String operation;

    @Column
    @NonNull
    private String creationDate;

    @Column
    @NonNull
    private String turbineState;

    @Column
    private int completedSteps;

    @Column
    private String startingDateEEMM;

    @Column
    private String startingDateOOCC;

    @Column
    private String completionDate;

    @Column
    private String completionDateEEMM;

    @Column
    private String completionDateOOCC;

}
