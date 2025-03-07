package eu.deltasource.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * Represents a student entity in the system.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    private int id;

    private String email;

    private String fullName;
}
