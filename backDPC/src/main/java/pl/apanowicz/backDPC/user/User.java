package pl.apanowicz.backDPC.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Repository
public class User {
    private String firstName;
    private String lastName;
    private String city;
    private String gender;
    private String email;

}
