package dev.ohhoonim.system.accesscontrol.pip.model;

import java.time.Instant;

public sealed interface PipStatus {

    record Active() implements PipStatus {}
    record Syncing(Instant startedAt) implements PipStatus {}
    record SyncFailed(String errorMessage, Instant failedAt) implements PipStatus {}
    record Inactive() implements PipStatus {}

    default boolean isActive() {
        return this instanceof Active;
    }

    default boolean isSyncing() {
        return this instanceof Syncing;
    }

    default boolean isInactive() {
        return this instanceof Inactive;
    }
}