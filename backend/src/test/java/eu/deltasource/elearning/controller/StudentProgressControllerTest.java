package eu.deltasource.elearning.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.deltasource.elearning.DTOs.StudentProgressDTO;
import eu.deltasource.elearning.service.StudentProgressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentProgressController.class)
public class StudentProgressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentProgressService studentProgressService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getProgressPercentage_givenValidUUIDs_whenGetRequest_thenReturnStudentProgressDTO() throws Exception {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        StudentProgressDTO progressDTO = new StudentProgressDTO();
        progressDTO.setProgressPercentage(75.5);

        // When
        when(studentProgressService.getProgressPercentage(studentId, courseId)).thenReturn(progressDTO);
        mockMvc.perform(get("/students/progress/v1/{studentId}/courses/{courseId}", studentId, courseId))

                //Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.progressPercentage").value(75.5));
    }

    @Test
    public void updateProgress_givenValidUUIDs_whenPostRequest_thenProgressIsUpdated() throws Exception {
        //Given
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        UUID videoId = UUID.randomUUID();

       //When
        mockMvc.perform(post("/students/progress/v1/{studentId}/courses/{courseId}/videos/{videoId}/update",
                        studentId, courseId, videoId))

                //Then
                .andExpect(status().isOk());
        verify(studentProgressService, times(1)).updateProgress(studentId, courseId, videoId);
    }
}

