create table if not exists decision_evaluations (
    decision_evaluation_id uuid not null,
    external_id uuid not null,
    
    subject_id text not null,
    resource_id text not null,
    action text not null,
    combining_algorithm text not null,
    decision_result varchar(30) not null,
    reason text,
    
    created_at timestamptz(6) not null default now(),
    created_by uuid not null,
    modified_at timestamptz(6) not null default now(),
    modified_by uuid not null,

    constraint pk_decision_evaluations primary key (decision_evaluation_id),
    constraint uk_decision_evaluations_external_id unique (external_id)
);

comment on table decision_evaluations is '정책 결정 평가 이력 테이블';
comment on column decision_evaluations.decision_evaluation_id is '내부 관리용 PK (ULID/UUID v7)';
comment on column decision_evaluations.external_id is '외부 노출용 ID (UUID v4)';
comment on column decision_evaluations.subject_id is '평가 대상 주체 식별자';
comment on column decision_evaluations.resource_id is '평가 대상 자원 식별자';
comment on column decision_evaluations.action is '평가 대상 행위';
comment on column decision_evaluations.combining_algorithm is '사용된 정책 결합 알고리즘';
comment on column decision_evaluations.decision_result is '최종 평가 결과 (PERMIT, DENY, INDETERMINATE 등)';
comment on column decision_evaluations.reason is '평가 사유 및 미결정 원인';
comment on column decision_evaluations.created_at is '생성 일시';
comment on column decision_evaluations.created_by is '생성자 ID';
comment on column decision_evaluations.modified_at is '수정 일시';
comment on column decision_evaluations.modified_by is '수정자 ID';

create index idx_decision_evaluations_subject_id on decision_evaluations(subject_id);
create index idx_decision_evaluations_created_at on decision_evaluations(created_at);