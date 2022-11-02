package pl.apanowicz.backDPC.user;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
@Slf4j
public class UserRepository {
    private static final String uri = "https://randomuser.me/api/";


    public User generateUser(){

        String jsonString="";
        try {
            RestTemplate restTemplate = new RestTemplate();
            jsonString = restTemplate.getForObject(uri, String.class); // get Json string from outside API
        }catch (Exception e){
            log.error("Error with getting data from "+uri);
            System.out.println(e.toString());
            return null;
        }


        String firstName;
        String lastName;
        String city;
        String gender;
        String email;

        try {
            JsonObject jsonObject = new Gson().fromJson(jsonString, JsonObject.class); //deserialize json
            JsonArray jsonArrayOfUsers = jsonObject.get("results").getAsJsonArray(); // get array with users
            JsonObject jsonUser = jsonArrayOfUsers.get(0).getAsJsonObject(); // get first user from array

            gender = jsonUser.get("gender").getAsString();  // get gender String value from
            email = jsonUser.get("email").getAsString();

            JsonObject jsonUserName = jsonUser.get("name").getAsJsonObject();
            firstName = jsonUserName.get("first").getAsString();
            lastName = jsonUserName.get("last").getAsString();

            JsonObject jsonUserLocation = jsonUser.get("location").getAsJsonObject();
            city = jsonUserLocation.get("city").getAsString();
        }catch (Exception e){
            log.error("error during deserialization, probably api("+uri+") changed data structure");
            return null;
        }

        // load data to new User object
        return new User(firstName,lastName,city,gender,email);
    }


}