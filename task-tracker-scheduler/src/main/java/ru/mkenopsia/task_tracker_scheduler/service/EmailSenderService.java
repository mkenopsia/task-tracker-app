package ru.mkenopsia.task_tracker_scheduler.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendDailyTaskDigest() {
        /*
        todo проходим по всем пользователям, раскладываем их задача на:
        сделанные сегодня
        просроченные за сегодня

        -> отправляем письма на соответствующие ящики
         */
//        kafkaTemplate.send()
    }
}
