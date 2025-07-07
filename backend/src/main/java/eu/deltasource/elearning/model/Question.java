package eu.deltasource.elearning.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Schema(description = "Question entity in a quiz")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Unique question identifier", example = "301")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    @Schema(description = "Quiz to which this question belongs")
    private Quiz quiz;

    @Schema(description = "The question text", example = "Which HTML element is used to define an unordered list?")
    private String question;

    @Schema(description = "Type of the question", example = "multiple-choice")
    private String questionType;

    @Schema(description = "Number of points this question is worth", example = "10")
    private int points;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @Schema(description = "List of possible answer options")
    private List<Option> options;

    @Schema(
            description = "Order of the question in the quiz",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private int order;
}
