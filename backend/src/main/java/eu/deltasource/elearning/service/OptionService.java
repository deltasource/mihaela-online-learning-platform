package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.OptionDTO;
import eu.deltasource.elearning.exception.OptionNotFoundException;
import eu.deltasource.elearning.exception.QuestionNotFoundException;
import eu.deltasource.elearning.model.Option;
import eu.deltasource.elearning.model.Question;
import eu.deltasource.elearning.repository.OptionRepository;
import eu.deltasource.elearning.repository.QuestionRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public OptionDTO createOption(OptionDTO optionDTO) {
        Question question = questionRepository.findById(optionDTO.getQuestionId())
                .orElseThrow(() -> new QuestionNotFoundException("Question not found"));

        Option option = new Option();
        option.setId(UUID.randomUUID());
        option.setQuestion(question);
        option.setText(optionDTO.getText());
        option.setCorrect(optionDTO.isCorrect());

        option = optionRepository.save(option);
        return mapToOptionDTO(option);
    }

    public OptionDTO getOptionById(UUID optionId) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new OptionNotFoundException("Option not found"));
        return mapToOptionDTO(option);
    }

    public List<OptionDTO> getOptionsByQuestionId(UUID questionId) {
        List<Option> options = optionRepository.findByQuestionId(questionId);
        return options.stream()
                .map(this::mapToOptionDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public OptionDTO updateOption(UUID optionId, OptionDTO optionDTO) {
        Option existingOption = optionRepository.findById(optionId)
                .orElseThrow(() -> new OptionNotFoundException("Option not found"));

        if (!existingOption.getQuestion().getId().equals(optionDTO.getQuestionId())) {
            Question newQuestion = questionRepository.findById(optionDTO.getQuestionId())
                    .orElseThrow(() -> new QuestionNotFoundException("Question not found"));
            existingOption.setQuestion(newQuestion);
        }

        existingOption.setText(optionDTO.getText());
        existingOption.setCorrect(optionDTO.isCorrect());

        existingOption = optionRepository.save(existingOption);
        return mapToOptionDTO(existingOption);
    }

    @Transactional
    public void deleteOption(UUID optionId) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new OptionNotFoundException("Option not found"));
        optionRepository.delete(option);
    }

    @NotNull
    private OptionDTO mapToOptionDTO(Option option) {
        OptionDTO optionDTO = new OptionDTO();
        optionDTO.setId(option.getId());
        optionDTO.setQuestionId(option.getQuestion().getId());
        optionDTO.setText(option.getText());
        optionDTO.setCorrect(option.isCorrect());
        return optionDTO;
    }
}
