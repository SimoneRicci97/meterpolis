package it.simonericci97.github.meterpolis.meterpolis.listener;

import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisBatchEntity;
import org.slf4j.MDC;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A ItemProcessListener which values MDC to recognize entity that is processing
 *
 * @param <I> type input of a processor
 * @param <O> type output of a processor
 */

@Scope("prototype")
@Component
public class MeterpolisItemProcessorListener<I extends MeterpolisBatchEntity, O extends MeterpolisBatchEntity> implements ItemProcessListener<MeterpolisBatchEntity,MeterpolisBatchEntity> {
    @Override
    public void beforeProcess(MeterpolisBatchEntity entity) {
        MDC.put("entity", entity.getDescription());
    }

    @Override
    public void afterProcess(MeterpolisBatchEntity entity, MeterpolisBatchEntity entity2) {
        MDC.remove("entity");
    }

    @Override
    public void onProcessError(MeterpolisBatchEntity entity, Exception e) {
        MDC.remove("entity");
    }
}
