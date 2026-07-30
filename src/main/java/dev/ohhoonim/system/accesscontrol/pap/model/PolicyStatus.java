package dev.ohhoonim.system.accesscontrol.pap.model;

import dev.ohhoonim.component.model.state.Status;
import dev.ohhoonim.component.model.state.TransitionResult;



public sealed interface PolicyStatus {

    record Draft() implements PolicyStatus {}
    record Verified(String verificationResult) implements PolicyStatus {}
    record Approved(String approvedBy) implements PolicyStatus {}
    record Deployed(String deploymentTarget) implements PolicyStatus {}

    default boolean isDraft() {
        return this instanceof Draft;
    }

    default boolean isVerified() {
        return this instanceof Verified;
    }

    default boolean isApproved() {
        return this instanceof Approved;
    }

    default boolean isDeployed() {
        return this instanceof Deployed;
    }
}

// public sealed interface PolicyStatus extends Status<PolicyStatus, PolicyTransitionEvent, Policy>
//         permits PolicyStatus.Draft,
//                 PolicyStatus.Verified,
//                 PolicyStatus.Published,
//                 PolicyStatus.Archived {

//     record Draft() implements PolicyStatus {
//         @Override
//         public TransitionResult<PolicyStatus, Policy> trigger(PolicyTransitionEvent event) {
//             return switch (event) {
//                 case PolicyTransitionEvent.VerifyEvent e -> new PolicyTransitionResult(new Verified(), e.actions());
//                 default -> throw new PolicyException("DRAFT 상태에서는 검증(Verify)만 수행할 수 있습니다.");
//             };
//         }
//     }

//     record Verified() implements PolicyStatus {
//         @Override
//         public TransitionResult<PolicyStatus, Policy> trigger(PolicyTransitionEvent event) {
//             return switch (event) {
//                 case PolicyTransitionEvent.PublishEvent e -> new PolicyTransitionResult(new Published(), e.actions());
//                 case PolicyTransitionEvent.VerifyEvent e -> new PolicyTransitionResult(new Verified(), e.actions());
//                 default -> throw new PolicyException("VERIFIED 상태에서는 게시(Publish) 또는 재검증만 가능합니다.");
//             };
//         }
//     }

//     record Published() implements PolicyStatus {
//         @Override
//         public TransitionResult<PolicyStatus, Policy> trigger(PolicyTransitionEvent event) {
//             return switch (event) {
//                 case PolicyTransitionEvent.ArchiveEvent e -> new PolicyTransitionResult(new Archived(), e.actions());
//                 default -> throw new PolicyException("PUBLISHED 상태에서는 보관(Archive) 처리만 가능합니다.");
//             };
//         }
//     }

//     record Archived() implements PolicyStatus {
//         @Override
//         public TransitionResult<PolicyStatus, Policy> trigger(PolicyTransitionEvent event) {
//             throw new PolicyException("ARCHIVED 상태에서는 더 이상 상태 전이를 수행할 수 없습니다.");
//         }
//     }
// }