package pl.apanowicz.agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RabbitMQ {
    public static final String PUBLISHER_QUEUE= "backPost";

    public enum QueueMessage {
        USER_CREATED,
        SUCCESS_DB_INSERT,
        ERROR_DB_INSERT,
        EMPTY
    }

    @Autowired
    UserRepository userRepository;


    @RabbitListener(queues = "agentPost")
    public void readQueue(String recivedMessage){

        // convert recivedMessage to Enum object
        QueueMessage queMessage = QueueMessage.EMPTY;
        try {
            queMessage = QueueMessage.valueOf(recivedMessage);
        }catch (Exception e){
            log.error("unrecognized procedure");
            return;
        }



        switch (queMessage){
            case USER_CREATED:
                //log.info("Goted message from Queue, user is ready to download");
                addDataToDB();
            break;
            default:
                log.warn("unknown procedure, need to create implementation to hadle this quest");
        }
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;
    public boolean sendMessage(QueueMessage message) {
        while (true){
            try {
                rabbitTemplate.convertAndSend(PUBLISHER_QUEUE, message.name());
                return true;

            } catch (Exception e) {
                log.warn("Error in sending information to the queue");
                try {TimeUnit.SECONDS.sleep(10);}catch (Exception ex){} //delay 10 seconds
            }
        }
    }

    public void addDataToDB(){
        User user;

        boolean erLogWasPrint = false;    // helps to print only one error LOG in loop
        // repeat until you get data from the API
        while(true) {
            user = userRepository.getFromApi();  //try to get data from API
            if (user != null) break;    // if user is null, wait some time and try again

            if(!erLogWasPrint){    // print one LOG if getting data from API was failure
                log.warn("Error downloading user data from API");
                erLogWasPrint=true;
            }

            try {TimeUnit.SECONDS.sleep(10);}catch (Exception e){} //delay 10 seconds
        }

        //clear log helper
        erLogWasPrint=false;

        // repeat until you save data to database
        while(true) {
            // if adding a user to the database was successful,
            if (userRepository.insertToDB(user)) {
                sendMessage(QueueMessage.SUCCESS_DB_INSERT); // send success message to Queue
                log.info("Successfully add user to database");
                return; // Insert data to DB was succesfull, return
            }
            sendMessage(QueueMessage.ERROR_DB_INSERT);    // if not, send error message to Queue

            // print one error LOG
            if(!erLogWasPrint){
                log.warn("Error in communication with MySQL");
                erLogWasPrint=true;
            }

            try {TimeUnit.SECONDS.sleep(10);}catch (Exception e){} //delay 10 seconds


        }
    }

}