package eu.deltasource.elearning.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import eu.deltasource.elearning.exception.InvalidCategoryException;

import java.util.Arrays;

public enum Category {
    GENERAL,
    DATA_SCIENCE,
    MOBILE_DEVELOPMENT,
    DESIGN,
    BUSINESS,
    MARKETING;

    @JsonCreator
    public static Category fromString(String value) {
        return Arrays.stream(Category.values())
                .filter(e -> e.name().equalsIgnoreCase(value.replace(" ", "_")))
                .findFirst()
                .orElseThrow(() -> new InvalidCategoryException("Invalid category: " + value));
    }
}
