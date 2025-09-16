package ru.mkenopsia.tasktrackeremailsender.listener;

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

    @KafkaListener(topics = "${EMAIL_SENDING_TASKS}")
    public void emailSendingRequestListener(ConsumerRecord<Long, EmailMessageDto> emailMessageConsumerRecord) {
        EmailMessageDto email = emailMessageConsumerRecord.value();
        this.emailSendingService.sendEmail(email.emailAddress(), email.header(), email.body());
    }
}
