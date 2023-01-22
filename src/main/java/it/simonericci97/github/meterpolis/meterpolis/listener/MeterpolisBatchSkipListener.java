package it.simonericci97.github.meterpolis.meterpolis.listener;

import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisBatchEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Just a listner which logs errors that have been skipped
 *
 * @param <T> type input of a step inside meterpolis batch
 * @param <S> type output of a step inside meterpolis batch
 */

@Slf4j
@Scope("prototype")
@Component
public class MeterpolisBatchSkipListener<T extends MeterpolisBatchEntity, S extends MeterpolisBatchEntity> implements SkipListener<MeterpolisBatchEntity, MeterpolisBatchEntity> {
    @Override
    public void onSkipInRead(Throwable throwable) {
        log.warn("[skip][read] {}", throwable.getMessage());
    }

    @Override
    public void onSkipInWrite(MeterpolisBatchEntity entity, Throwable throwable) {
        log.warn("[skip][write] {}: {}", entity.getDescription(), throwable.getMessage());
    }

    @Override
    public void onSkipInProcess(MeterpolisBatchEntity entity, Throwable throwable) {
        log.warn("[skip][process] {}: {}", entity.getDescription(), throwable.getMessage());
    }
}
