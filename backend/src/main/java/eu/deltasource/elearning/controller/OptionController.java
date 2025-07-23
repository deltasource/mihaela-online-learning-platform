package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.OptionDTO;
import eu.deltasource.elearning.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Tag(name = "Question Options Management")
@RestController
@RequestMapping("/api/options")
@RequiredArgsConstructor
@Slf4j
public class OptionController {

    private final OptionService optionService;

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Create a new option")
    public OptionDTO createOption(@RequestBody @Valid OptionDTO optionDTO) {
        log.info("Creating a new option with text: {}", optionDTO.getText());
        return optionService.createOption(optionDTO);
    }

    @GetMapping("/{optionId}")
    @Operation(summary = "Get option by ID")
    public OptionDTO getOptionById(@PathVariable @NotNull UUID optionId) {
        log.info("Getting option by ID: {}", optionId);
        return optionService.getOptionById(optionId);
    }

    @GetMapping("/question/{questionId}")
    @Operation(summary = "Get all options for a question")
    public List<OptionDTO> getOptionsByQuestionId(@PathVariable @NotNull UUID questionId) {
        log.info("Getting all options for question ID: {}", questionId);
        return optionService.getOptionsByQuestionId(questionId);
    }

    @PutMapping("/{optionId}")
    @Operation(summary = "Update an option")
    public OptionDTO updateOption(
            @PathVariable @NotNull UUID optionId,
            @RequestBody @Valid OptionDTO optionDTO) {
        log.info("Updating option with ID: {}", optionId);
        return optionService.updateOption(optionId, optionDTO);
    }

    @DeleteMapping("/{optionId}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Delete an option")
    public void deleteOption(@PathVariable @NotNull UUID optionId) {
        log.info("Deleting option with ID: {}", optionId);
        optionService.deleteOption(optionId);
    }
}
