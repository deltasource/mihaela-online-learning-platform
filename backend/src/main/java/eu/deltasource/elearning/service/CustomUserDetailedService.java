package eu.deltasource.elearning.service;

import eu.deltasource.elearning.model.Student;
import eu.deltasource.elearning.model.Instructor;
import eu.deltasource.elearning.repository.StudentRepository;
import eu.deltasource.elearning.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailedService implements UserDetailsService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = studentRepository.findByUsername(username).orElse(null);
        if (student != null) {
            return new CustomUserDetails(student);
        }
        Instructor instructor = instructorRepository.findByUsername(username).orElse(null);
        if (instructor != null) {
            return new CustomUserDetails(instructor);
        }
        throw new UsernameNotFoundException("User not found");
    }
}
