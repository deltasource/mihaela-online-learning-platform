package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.OptionDTO;
import eu.deltasource.elearning.service.OptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OptionControllerTest {

    @Mock
    private OptionService optionService;

    @InjectMocks
    private OptionController optionController;

    @Test
    void givenValidOptionData_whenCreateOption_thenOptionIsSuccessfullyCreated() {
        // Given
        OptionDTO optionDTO = new OptionDTO();
        optionDTO.setQuestionId(UUID.randomUUID());
        optionDTO.setText("Sample Option");
        optionDTO.setCorrect(true);
        when(optionService.createOption(optionDTO)).thenReturn(optionDTO);

        // When
        OptionDTO response = optionController.createOption(optionDTO);

        // Then
        assertEquals(optionDTO, response);
        verify(optionService, times(1)).createOption(optionDTO);
    }

    @Test
    void givenOptionIdExists_whenGetOptionById_thenCorrectOptionIsReturned() {
        // Given
        UUID optionId = UUID.randomUUID();
        OptionDTO optionDTO = new OptionDTO();
        optionDTO.setId(optionId);
        optionDTO.setText("Sample Option");
        when(optionService.getOptionById(optionId)).thenReturn(optionDTO);

        // When
        OptionDTO response = optionController.getOptionById(optionId);

        // Then
        assertEquals(optionDTO, response);
        verify(optionService, times(1)).getOptionById(optionId);
    }

    @Test
    void givenQuestionIdExists_whenGetOptionsByQuestionId_thenAllOptionsAreReturned() {
        // Given
        UUID questionId = UUID.randomUUID();
        List<OptionDTO> options = List.of(new OptionDTO(), new OptionDTO());
        when(optionService.getOptionsByQuestionId(questionId)).thenReturn(options);

        // When
        List<OptionDTO> response = optionController.getOptionsByQuestionId(questionId);

        // Then
        assertEquals(options, response);
        verify(optionService, times(1)).getOptionsByQuestionId(questionId);
    }

    @Test
    void givenOptionExists_whenUpdateOption_thenOptionIsUpdatedSuccessfully() {
        // Given
        UUID optionId = UUID.randomUUID();
        OptionDTO optionDTO = new OptionDTO();
        optionDTO.setText("Updated Option");
        optionDTO.setCorrect(false);
        when(optionService.updateOption(optionId, optionDTO)).thenReturn(optionDTO);

        // When
        OptionDTO response = optionController.updateOption(optionId, optionDTO);

        // Then
        assertEquals(optionDTO, response);
        verify(optionService, times(1)).updateOption(optionId, optionDTO);
    }

    @Test
    void givenOptionExists_whenDeleteOption_thenOptionIsDeletedSuccessfully() {
        // Given
        UUID optionId = UUID.randomUUID();
        doNothing().when(optionService).deleteOption(optionId);

        // When
        optionController.deleteOption(optionId);

        // Then
        verify(optionService, times(1)).deleteOption(optionId);
    }
}
