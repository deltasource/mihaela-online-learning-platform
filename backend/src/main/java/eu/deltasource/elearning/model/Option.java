package eu.deltasource.elearning.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Schema(description = "Option for a quiz question")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Unique option ID", example = "401")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @Schema(description = "Question this option belongs to")
    private Question question;

    @Schema(description = "Option text", example = "<ul>")
    private String text;

    @Schema(description = "Is this the correct answer?", example = "true")
    private boolean isCorrect;
}
