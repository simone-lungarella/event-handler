package it.os.event.handler.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "event")
public class EventETY implements Serializable {

	/**
	 * Serial version uid.
	 */
	private static final long serialVersionUID = -3379318079047423133L;
 
	@Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column
	@NonNull
	@CsvBindByName(column = "Nome turbina")
    @CsvBindByPosition(position = 0)
	private String turbineName;

	@Column
	@NonNull
	@CsvBindByName(column = "Numero turbina")
    @CsvBindByPosition(position = 1)
	private String turbineNumber;

	@Column
	@NonNull
	@CsvBindByName(column = "Descrizione")
    @CsvBindByPosition(position = 3)
	private String description;

	@Column
	@CsvBindByName(column = "Numero ODL")
    @CsvBindByPosition(position = 2)
	private Integer odlNumber;

	@Column
	@NonNull
	@CsvBindByName(column = "Tipologia turbina")
    @CsvBindByPosition(position = 6)
	private String power;

	@Column
	@NonNull
	@ElementCollection(fetch = FetchType.EAGER, targetClass = String.class)
    @OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "id")
    @Cascade(value={CascadeType.ALL})
	@CsvBindByName(column = "Operazioni")
	@CsvBindByPosition(position = 7)
	private List<String> operation;

	@Column
	@NonNull
	@CsvBindByName(column = "Data creazione")
    @CsvBindByPosition(position = 4)
	private String creationDate;

	@Column
	@NonNull
	@CsvBindByName(column = "Stato turbina")
    @CsvBindByPosition(position = 5)
	private String turbineState;

	@Column
	private int completedSteps;

	@Column
	@CsvBindByName(column = "Inizio EEMM")
    @CsvBindByPosition(position = 8)
	private String startingDateEEMM;

	@Column
	@CsvBindByName(column = "Inizio OOCC")
    @CsvBindByPosition(position = 9)
	private String startingDateOOCC;

	@Column
	@CsvBindByName(column = "Smontaggio piazzola")
    @CsvBindByPosition(position = 12)
	private String completionDate;

	@Column
	@CsvBindByName(column = "Fine EEMM")
    @CsvBindByPosition(position = 10)
	private String completionDateEEMM;

	@Column
	@CsvBindByName(column = "Fine OOCC")
    @CsvBindByPosition(position = 11)
	private String completionDateOOCC;

	@Column
	private String permittingDate;

	@Column
	private boolean mailSent;

}
