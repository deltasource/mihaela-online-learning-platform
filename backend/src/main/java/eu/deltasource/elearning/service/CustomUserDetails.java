package eu.deltasource.elearning.service;

import eu.deltasource.elearning.model.Student;
import eu.deltasource.elearning.model.Instructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
public class CustomUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final String role;

    public CustomUserDetails(Student student) {
        this.username = student.getUsername();
        this.password = student.getPassword();
        this.role = "ROLE_STUDENT";
    }

    public CustomUserDetails(Instructor instructor) {
        this.username = instructor.getUsername();
        this.password = instructor.getPassword();
        this.role = "ROLE_INSTRUCTOR";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}
