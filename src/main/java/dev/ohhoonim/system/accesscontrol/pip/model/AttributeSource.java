package dev.ohhoonim.system.accesscontrol.pip.model;

import java.time.Instant;
import dev.ohhoonim.component.model.unit.BaseEntity;

public class AttributeSource extends BaseEntity<AttributeSourceId> {

    private PipComponent.SourceInfo info;
    private PipComponent.SourceConnection connection;
    private AttributeValueCollection cachedAttributes;
    private PipStatus status;

    protected AttributeSource(AttributeSourceId id, PipComponent.SourceInfo info,
                              PipComponent.SourceConnection connection, String operator) {
        super(id, operator);
        if (info == null) throw new PipException("데이터 원천 정보가 누락되었습니다");
        if (connection == null) throw new PipException("데이터 원천 연결 설정이 누락되었습니다");

        this.info = info;
        this.connection = connection;
        this.cachedAttributes = new AttributeValueCollection(null);
        this.status = new PipStatus.Active();
    }

    private AttributeSource(AttributeSourceId id, PipComponent.SourceInfo info,
                            PipComponent.SourceConnection connection, AttributeValueCollection cachedAttributes,
                            PipStatus status, Instant createdAt, String createdBy, Instant modifiedAt, String modifiedBy) {
        super(id, createdAt, createdBy, modifiedAt, modifiedBy);
        this.info = info;
        this.connection = connection;
        this.cachedAttributes = cachedAttributes;
        this.status = status;
    }

    public static AttributeSource create(AttributeSourceId id, PipComponent.SourceInfo info,
                                         PipComponent.SourceConnection connection, String operator) {
        return new AttributeSource(id, info, connection, operator);
    }

    public static AttributeSource reconstitute(AttributeSourceId id, PipComponent.SourceInfo info,
                                               PipComponent.SourceConnection connection,
                                               AttributeValueCollection cachedAttributes, PipStatus status,
                                               Instant createdAt, String createdBy, Instant modifiedAt, String modifiedBy) {
        return new AttributeSource(id, info, connection, cachedAttributes, status, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public void startSync(Instant now, String operator) {
        if (status.isInactive()) {
            throw new PipException("비활성화된 데이터 원천은 동기화할 수 없습니다");
        }
        this.status = new PipStatus.Syncing(now);
        recordModification(operator);
    }

    public void completeSync(AttributeValueCollection newAttributes, String operator) {
        if (!status.isSyncing()) {
            throw new PipException("동기화 진행 중인 상태에서만 완료 처리를 수행할 수 있습니다");
        }
        this.cachedAttributes = newAttributes;
        this.status = new PipStatus.Active();
        recordModification(operator);
    }

    public void failSync(String errorMessage, Instant now, String operator) {
        if (!status.isSyncing()) {
            throw new PipException("동기화 진행 중인 상태에서만 실패 처리를 수행할 수 있습니다");
        }
        this.status = new PipStatus.SyncFailed(errorMessage, now);
        recordModification(operator);
    }

    public void deactivate(String operator) {
        this.status = new PipStatus.Inactive();
        recordModification(operator);
    }

    public void activate(String operator) {
        this.status = new PipStatus.Active();
        recordModification(operator);
    }

    public PipComponent.SourceInfo getInfo() {
        return info;
    }

    public PipComponent.SourceConnection getConnection() {
        return connection;
    }

    public AttributeValueCollection getCachedAttributes() {
        return cachedAttributes;
    }

    public PipStatus getStatus() {
        return status;
    }

    public Object touchModified(String operator) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'touchModified'");
    }

    public Object updateSyncResult(String resultMessage, String operator) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateSyncResult'");
    }
}