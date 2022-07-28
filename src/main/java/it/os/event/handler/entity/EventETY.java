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
	private String turbineName;

	@Column
	@NonNull
	private String turbineNumber;

	@Column
	@NonNull
	private String description;

	@Column
	@NonNull
	private String power;

	@Column
	@NonNull
	@ElementCollection(fetch = FetchType.EAGER, targetClass = String.class)
    @OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "id")
    @Cascade(value={CascadeType.ALL})
	private List<String> operation;

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
