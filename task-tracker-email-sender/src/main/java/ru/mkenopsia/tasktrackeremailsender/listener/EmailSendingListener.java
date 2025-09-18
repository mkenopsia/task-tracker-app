package ru.mkenopsia.tasktrackeremailsender.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import ru.mkenopsia.tasktrackeremailsender.dto.EmailMessageDto;
import ru.mkenopsia.tasktrackeremailsender.service.EmailSendingService;

@Component
@RequiredArgsConstructor
public class EmailSendingListener {

    private final EmailSendingService emailSendingService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${EMAIL_SENDING_TASKS}")
    public void emailSendingRequestListener(ConsumerRecord<String, String> emailMessageConsumerRecord) throws JsonProcessingException {
        EmailMessageDto email = this.objectMapper.readValue(emailMessageConsumerRecord.value(), EmailMessageDto.class);
        this.emailSendingService.sendEmail(email.emailAddress(), email.header(), email.body());
    }
}
