package eu.deltasource.elearning.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

/**
 * Represents an option for a quiz question.
 * Each option is associated with a specific question and contains text
 * and a flag indicating whether it is the correct answer.
 */
@Entity
@Data
@Schema(description = "Option for a quiz question")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    private String text;

    private boolean isCorrect;
}
