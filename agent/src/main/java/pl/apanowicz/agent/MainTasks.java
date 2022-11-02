package pl.apanowicz.agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class MainTasks {
    @Value("${BACKEND_DPC_QUEUE_NAME}")
    private String PUBLISHER_QUEUE;

    @Value("${BACKEND_DPC_URI_API_ADRESS}")
    private String BACKEND_DPC;

    public enum QueueMessage {
        USER_CREATED,
        SUCCESS_DB_INSERT,
        ERROR_DB_INSERT,
        EMPTY
    }

    @Autowired
    UserRepository userRepository;


    @RabbitListener(queues = "${AGENT_QUEUE_NAME}")
    public void readQueue(String recivedString){

        QueueMessage queMessage = decodeString(recivedString);

        switch (queMessage){
            case USER_CREATED:
                User user = null;
                do{
                    user = userRepository.getFromApi(BACKEND_DPC);
                    if(user==null)
                        try{TimeUnit.SECONDS.sleep(10);}catch (Exception c){} //delay 10 seconds
                }while (user==null);


                boolean insertStatus;

                do{
                    insertStatus = userRepository.insertToDB(user);
                    if(!insertStatus) {
                        try {TimeUnit.SECONDS.sleep(10);} catch (Exception c) {} //delay 10 seconds
                        sendMessage(QueueMessage.ERROR_DB_INSERT);
                    }
                }while (!insertStatus);

                sendMessage(QueueMessage.SUCCESS_DB_INSERT);
                log.info("Successfully add user to database");
                break;
            default:
                log.warn("unknown procedure, need to create implementation to hadle this quest");
        }
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void sendMessage(QueueMessage message) {
        while (true){
            try {
                rabbitTemplate.convertAndSend(PUBLISHER_QUEUE, message.name());
                return;

            } catch (Exception e) {
                log.warn("Error in sending information to the queue");
                try {TimeUnit.SECONDS.sleep(10);}catch (Exception ex){} //delay 10 seconds
            }
        }
    }

    private QueueMessage decodeString(String strMessage){
        QueueMessage queMessage = QueueMessage.EMPTY;
        try {
            queMessage = QueueMessage.valueOf(strMessage);
        }catch (Exception e){
            queMessage = QueueMessage.EMPTY;
        }
        return queMessage;
    }
}