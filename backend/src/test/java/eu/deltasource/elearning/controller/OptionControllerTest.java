package eu.deltasource.elearning.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.deltasource.elearning.DTOs.OptionDTO;
import eu.deltasource.elearning.service.OptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The @AutoConfigureMockMvc(addFilters = false) annotation is used in Spring Boot testing to disable the automatic addition of Spring Security filters when configuring MockMvc.
 * By default, when @AutoConfigureMockMvc is used, any registered filters (such as security filters) are applied to the MockMvc instance. Setting addFilters = false prevents these filters from being added, allowing tests to bypass security constraints and focus on controller logic without authentication or authorization requirements.
 **/
@WebMvcTest(OptionController.class)
@AutoConfigureMockMvc(addFilters = false)
class OptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OptionService optionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidOptionId_whenGetOptionById_thenReturnsOkAndServiceCalled() throws Exception {
        // Given
        UUID optionId = UUID.randomUUID();
        OptionDTO optionDTO = new OptionDTO();
        optionDTO.setId(optionId);
        optionDTO.setText("Option A");
        optionDTO.setCorrect(true);
        optionDTO.setQuestionId(UUID.randomUUID());
        when(optionService.getOptionById(optionId)).thenReturn(optionDTO);

        // When
        var result = mockMvc.perform(get("/api/options/{optionId}", optionId));

        // Then
        result.andExpect(status().isOk());
        verify(optionService, times(1)).getOptionById(eq(optionId));
    }

    @Test
    void givenValidQuestionId_whenGetOptionsByQuestionId_thenReturnsOkAndServiceCalled() throws Exception {
        // Given
        UUID questionId = UUID.randomUUID();
        List<OptionDTO> options = List.of(new OptionDTO(), new OptionDTO());
        when(optionService.getOptionsByQuestionId(questionId)).thenReturn(options);

        // When
        var result = mockMvc.perform(get("/api/options/question/{questionId}", questionId));

        // Then
        result.andExpect(status().isOk());
        verify(optionService, times(1)).getOptionsByQuestionId(eq(questionId));
    }

    @Test
    void givenValidOptionId_whenDeleteOption_thenReturnsNoContentAndServiceCalled() throws Exception {
        // Given
        UUID optionId = UUID.randomUUID();
        doNothing().when(optionService).deleteOption(optionId);

        // When
        var result = mockMvc.perform(delete("/api/options/{optionId}", optionId));

        // Then
        result.andExpect(status().isNoContent());
        verify(optionService, times(1)).deleteOption(eq(optionId));
    }

    @Test
    void givenInvalidOptionDTO_whenCreateOption_thenReturnsBadRequest() throws Exception {
        // Given
        OptionDTO optionDTO = new OptionDTO();

        // When
        var result = mockMvc.perform(post("/api/options")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(optionDTO)));

        // Then
        result.andExpect(status().isBadRequest());
    }
}
