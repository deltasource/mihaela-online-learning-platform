package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.OptionDTO;
import eu.deltasource.elearning.exception.OptionNotFoundException;
import eu.deltasource.elearning.exception.QuestionNotFoundException;
import eu.deltasource.elearning.model.Option;
import eu.deltasource.elearning.model.Question;
import eu.deltasource.elearning.repository.OptionRepository;
import eu.deltasource.elearning.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OptionServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private OptionService optionService;

    @Test
    void givenValidOptionDTO_whenCreateOption_thenSavesAndReturnsDTO() {
        // Given
        UUID questionId = UUID.randomUUID();
        Question question = new Question();
        question.setId(questionId);
        OptionDTO optionDTO = new OptionDTO();
        optionDTO.setQuestionId(questionId);
        optionDTO.setText("Option A");
        optionDTO.setCorrect(true);
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        when(optionRepository.save(any(Option.class))).thenAnswer(inv -> {
            Option o = inv.getArgument(0);
            o.setId(UUID.randomUUID());
            return o;
        });

        // When
        OptionDTO result = optionService.createOption(optionDTO);

        // Then
        assertEquals("Option A", result.getText());
        assertTrue(result.isCorrect());
        assertEquals(questionId, result.getQuestionId());
        verify(optionRepository).save(any(Option.class));
    }

    @Test
    void givenNonExistentQuestion_whenCreateOption_thenThrowsQuestionNotFoundException() {
        // Given
        UUID questionId = UUID.randomUUID();
        OptionDTO optionDTO = new OptionDTO();
        optionDTO.setQuestionId(questionId);
        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(QuestionNotFoundException.class, () -> optionService.createOption(optionDTO));
    }

    @Test
    void givenExistingOptionId_whenGetOptionById_thenReturnsDTO() {
        // Given
        UUID optionId = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();
        Question question = new Question();
        question.setId(questionId);
        Option option = new Option();
        option.setId(optionId);
        option.setQuestion(question);
        option.setText("Option B");
        option.setCorrect(false);
        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));

        // When
        OptionDTO dto = optionService.getOptionById(optionId);

        // Then
        assertEquals(optionId, dto.getId());
        assertEquals(questionId, dto.getQuestionId());
        assertEquals("Option B", dto.getText());
        assertFalse(dto.isCorrect());
    }

    @Test
    void givenNonExistentOptionId_whenGetOptionById_thenThrowsOptionNotFoundException() {
        // Given
        UUID optionId = UUID.randomUUID();
        when(optionRepository.findById(optionId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(OptionNotFoundException.class, () -> optionService.getOptionById(optionId));
    }

    @Test
    void givenQuestionId_whenGetOptionsByQuestionId_thenReturnsList() {
        // Given
        UUID questionId = UUID.randomUUID();
        Question question = new Question();
        question.setId(questionId);
        Option optionOne = new Option();
        optionOne.setId(UUID.randomUUID());
        optionOne.setQuestion(question);
        optionOne.setText("A");
        optionOne.setCorrect(true);
        Option optionTwo = new Option();
        optionTwo.setId(UUID.randomUUID());
        optionTwo.setQuestion(question);
        optionTwo.setText("B");
        optionTwo.setCorrect(false);
        when(optionRepository.findByQuestionId(questionId)).thenReturn(Arrays.asList(optionOne, optionTwo));

        // When
        List<OptionDTO> result = optionService.getOptionsByQuestionId(questionId);

        // Then
        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getText());
        assertEquals("B", result.get(1).getText());
    }

    @Test
    void givenValidUpdate_whenUpdateOption_thenUpdatesAndReturnsDTO() {
        // Given
        UUID optionId = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();
        Question question = new Question();
        question.setId(questionId);
        Option existing = new Option();
        existing.setId(optionId);
        existing.setQuestion(question);
        existing.setText("Old");
        existing.setCorrect(false);
        OptionDTO optionDTO = new OptionDTO();
        optionDTO.setQuestionId(questionId);
        optionDTO.setText("New");
        optionDTO.setCorrect(true);
        when(optionRepository.findById(optionId)).thenReturn(Optional.of(existing));
        when(optionRepository.save(any(Option.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        OptionDTO result = optionService.updateOption(optionId, optionDTO);

        // Then
        assertEquals("New", result.getText());
        assertTrue(result.isCorrect());
        verify(optionRepository).save(existing);
    }

    @Test
    void givenUpdateWithQuestionChange_whenUpdateOption_thenUpdatesQuestion() {
        // Given
        UUID optionId = UUID.randomUUID();
        UUID oldQuestionId = UUID.randomUUID();
        UUID newQuestionId = UUID.randomUUID();
        Question oldQuestion = new Question();
        oldQuestion.setId(oldQuestionId);
        Question newQuestion = new Question();
        newQuestion.setId(newQuestionId);
        Option existing = new Option();
        existing.setId(optionId);
        existing.setQuestion(oldQuestion);
        OptionDTO optionDTO = new OptionDTO();
        optionDTO.setQuestionId(newQuestionId);
        when(optionRepository.findById(optionId)).thenReturn(Optional.of(existing));
        when(questionRepository.findById(newQuestionId)).thenReturn(Optional.of(newQuestion));
        when(optionRepository.save(any(Option.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        OptionDTO result = optionService.updateOption(optionId, optionDTO);

        // Then
        assertEquals(newQuestionId, result.getQuestionId());
        verify(optionRepository).save(existing);
    }

    @Test
    void givenUpdateWithNonExistentOption_whenUpdateOption_thenThrowsOptionNotFoundException() {
        // Given
        UUID optionId = UUID.randomUUID();
        OptionDTO optionDTO = new OptionDTO();
        when(optionRepository.findById(optionId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(OptionNotFoundException.class, () -> optionService.updateOption(optionId, optionDTO));
    }

    @Test
    void givenUpdateWithNonExistentQuestion_whenUpdateOption_thenThrowsQuestionNotFoundException() {
        // Given
        UUID optionId = UUID.randomUUID();
        UUID oldQuestionId = UUID.randomUUID();
        UUID newQuestionId = UUID.randomUUID();
        Question oldQuestion = new Question();
        oldQuestion.setId(oldQuestionId);
        Option existing = new Option();
        existing.setId(optionId);
        existing.setQuestion(oldQuestion);
        OptionDTO optionDTO = new OptionDTO();
        optionDTO.setQuestionId(newQuestionId);
        when(optionRepository.findById(optionId)).thenReturn(Optional.of(existing));
        when(questionRepository.findById(newQuestionId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(QuestionNotFoundException.class, () -> optionService.updateOption(optionId, optionDTO));
    }

    @Test
    void givenExistingOptionId_whenDeleteOption_thenDeletesOption() {
        // Given
        UUID optionId = UUID.randomUUID();
        Option option = new Option();
        option.setId(optionId);
        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));

        // When
        optionService.deleteOption(optionId);

        // Then
        verify(optionRepository).delete(option);
    }

    @Test
    void givenNonExistentOptionId_whenDeleteOption_thenThrowsOptionNotFoundException() {
        // Given
        UUID optionId = UUID.randomUUID();
        when(optionRepository.findById(optionId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(OptionNotFoundException.class, () -> optionService.deleteOption(optionId));
    }
}
