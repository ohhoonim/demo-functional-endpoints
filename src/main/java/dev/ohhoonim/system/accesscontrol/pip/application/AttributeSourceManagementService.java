package dev.ohhoonim.system.accesscontrol.pip.application;

import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import dev.ohhoonim.system.accesscontrol.pip.activity.AttributeFetchActivity;
import dev.ohhoonim.system.accesscontrol.pip.activity.out.AttributeSourcePersistencePort;
import dev.ohhoonim.system.accesscontrol.pip.model.AttributeSource;
import dev.ohhoonim.system.accesscontrol.pip.model.AttributeSourceId;
import dev.ohhoonim.system.accesscontrol.pip.model.AttributeValueCollection;
import dev.ohhoonim.system.accesscontrol.pip.model.PipComponent;
import dev.ohhoonim.system.accesscontrol.pip.model.PipException;

@Service
public class AttributeSourceManagementService {

    private final AttributeSourcePersistencePort attributeSourcePersistencePort;
    private final AttributeFetchActivity attributeFetchActivity;

    public AttributeSourceManagementService(AttributeSourcePersistencePort attributeSourcePersistencePort,
                                            AttributeFetchActivity attributeFetchActivity) {
        this.attributeSourcePersistencePort = attributeSourcePersistencePort;
        this.attributeFetchActivity = attributeFetchActivity;
    }

    @Transactional
    public AttributeSourceId registerSource(PipComponent.SourceInfo info,
                                            PipComponent.SourceConnection connection,
                                            String operator) {
        AttributeSourceId id = AttributeSourceId.Creator.generate();
        AttributeSource attributeSource = AttributeSource.create(id, info, connection, operator);

        AttributeSource savedSource = attributeSourcePersistencePort.save(attributeSource);
        return savedSource.getId();
    }

    @Transactional
    public void syncAttributes(AttributeSourceId id, Instant now, String operator) {
        AttributeSource attributeSource = findSourceOrThrow(id);

        attributeSource.startSync(now, operator);
        attributeSourcePersistencePort.save(attributeSource);

        try {
            AttributeValueCollection fetchedAttributes = attributeFetchActivity.fetchAttributes(attributeSource);
            attributeSource.completeSync(fetchedAttributes, operator);
        } catch (Exception e) {
            attributeSource.failSync(e.getMessage(), Instant.now(), operator);
        }

        attributeSourcePersistencePort.save(attributeSource);
    }

    @Transactional
    public void deactivateSource(AttributeSourceId id, String operator) {
        AttributeSource attributeSource = findSourceOrThrow(id);
        attributeSource.deactivate(operator);

        attributeSourcePersistencePort.save(attributeSource);
    }

    @Transactional
    public void activateSource(AttributeSourceId id, String operator) {
        AttributeSource attributeSource = findSourceOrThrow(id);
        attributeSource.activate(operator);

        attributeSourcePersistencePort.save(attributeSource);
    }

    private AttributeSource findSourceOrThrow(AttributeSourceId id) {
        return attributeSourcePersistencePort.findById(id)
                .orElseThrow(() -> new PipException("존재하지 않는 속성 데이터 원천입니다: " + id.getPublicValue()));
    }
}