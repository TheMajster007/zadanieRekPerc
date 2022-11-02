package pl.apanowicz.agent;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Repository
@Slf4j
public class UserRepository {
   private static final String aipUri="http://localhost:8080/api/user";

    @Autowired
    JdbcTemplate jdbcTemplate;


    public boolean insertToDB(User user){
        try {
            jdbcTemplate.update("INSERT INTO user(firstName, lastName, city, gender, email) VALUES(?,?,?,?,SHA2(?, 512))",
                    user.getFirstName(),
                    user.getLastName(),
                    user.getCity(),
                    user.getGender(),
                    user.getEmail());
            return true;
        }catch (Exception e){
            return false;
        }
    }



    public User getFromApi(){
        String jsonString="";

        // get JSON as String from API
        try {
            RestTemplate restTemplate = new RestTemplate();
            jsonString = restTemplate.getForObject(aipUri, String.class); // get Json string from outside API
        }catch (Exception e){
            return null;
        }

        String firstName;
        String lastName;
        String city;
        String gender;
        String email;

        //deserialize json String
        try {
            JsonObject jsonUser = new Gson().fromJson(jsonString, JsonObject.class); //deserialize json
            gender = jsonUser.get("gender").getAsString();  // get gender String value from
            email = jsonUser.get("email").getAsString();
            firstName = jsonUser.get("firstName").getAsString();
            lastName = jsonUser.get("lastName").getAsString();
            city = jsonUser.get("city").getAsString();

        }catch (Exception e){
            log.error("error during deserialization, probably api changed data structure");
            return null;
        }

        return new User(firstName, lastName, city ,gender ,email);
    }

}
