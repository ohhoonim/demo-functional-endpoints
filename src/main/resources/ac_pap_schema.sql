create table if not exists policies (
    policy_id uuid not null,
    external_id uuid not null,
    
    name text not null,
    description text,
    status varchar(30) not null,
    content text not null,
    version int4 not null default 1,
    verification_result text,
    
    created_at timestamptz(6) not null default now(),
    created_by uuid not null,
    modified_at timestamptz(6) not null default now(),
    modified_by uuid not null,

    constraint pk_policies primary key (policy_id),
    constraint uk_policies_external_id unique (external_id),
    constraint uk_policies_name_version unique (name, version)
);

comment on table policies is '접근 제어 정책 정보 테이블';
comment on column policies.policy_id is '내부 관리용 PK (ULID/UUID v7)';
comment on column policies.external_id is '외부 노출용 ID (UUID v4)';
comment on column policies.name is '정책명';
comment on column policies.description is '정책 설명';
comment on column policies.status is '정책 상태 (DRAFT, VERIFIED, PUBLISHED 등)';
comment on column policies.content is '정책 원문 내용';
comment on column policies.version is '정책 버전에 따른 순번';
comment on column policies.verification_result is '검증 수행 결과 메시지';
comment on column policies.created_at is '생성 일시';
comment on column policies.created_by is '생성자 ID';
comment on column policies.modified_at is '수정 일시';
comment on column policies.modified_by is '수정자 ID';

create index idx_policies_created_at on policies(created_at);