package pl.apanowicz.backDPC;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.apanowicz.backDPC.user.UserAPI;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ScheduledTask {

    public enum QueueMessage {
        USER_CREATED,
        SUCCESS_DB_INSERT,
        ERROR_DB_INSERT,
        EMPTY;
    }

    @Value("${AGENT_QUEUE_NAME}")
    private String AGENT_QUEUE_NAME;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UserAPI userAPI;


    //is needed on the first startup to know whether to wait for the agent's response
    private boolean firstServiceStart = true;
    private boolean userGeneratorIsOff = false;

    @Scheduled(fixedRateString = "${intervalUserCreation}")
    public void mainLoop(){

        // check if user has already been generated
        if(userGeneratorIsOff && !firstServiceStart){
            log.info("I'm still waiting for agent's response with adding user to DB.");
            return;
        }

        // we already have a peirce start, turn off the checker
        firstServiceStart = false;

       // generate a new user and pass it to the API
        if(!userAPI.makeUser()){
            return;
        }

        // user was created, disable user generator
        userGeneratorIsOff = true;

        //let the agent know that a user has been created
        sendMessage(QueueMessage.USER_CREATED);

    }

    @RabbitListener(queues = "${BACKEND_DPC_QUEUE_NAME}")
    void waitForResponse(String recivedMessage){

        // convert recivedMessage to Enum object
        QueueMessage queMessage = QueueMessage.EMPTY;
        try {
            queMessage = QueueMessage.valueOf(recivedMessage);
        }catch (Exception e){
            log.error("unrecognized procedure");
            return;
        }

        switch (queMessage){
            case SUCCESS_DB_INSERT:
                // Agent add user to DB. Enable user generator
                log.info("Agent successfully added user to DataBase");
                userGeneratorIsOff = false;
                break;

            case ERROR_DB_INSERT:
                // Agent have issue with adding user to DB. Disable user generator
                log.warn("Agent have issue with adding user to DataBase");
                userGeneratorIsOff = true;
                break;

            default:
                log.warn("unknown procedure, need to create implementation to hadle this question");
        }
    }


    public boolean sendMessage(QueueMessage message){
        while(true) {
            try {
                rabbitTemplate.convertAndSend(AGENT_QUEUE_NAME, message.name());
                return true;
            } catch (Exception e) {
                log.warn("Error in sending information to the queue. Trying to send again");
                try {TimeUnit.SECONDS.sleep(10);}catch (Exception ex){} //delay 10 seconds
            }
        }
    }




}
