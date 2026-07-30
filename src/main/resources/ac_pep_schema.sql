create table if not exists access_enforcements (
    enforcement_id uuid not null,
    external_id uuid not null,
    
    request_uri text not null,
    http_method varchar(10) not null,
    client_ip text,
    target_service_id text not null,
    target_path text not null,
    status varchar(30) not null,
    response_status int4,
    failure_reason text,
    
    created_at timestamptz(6) not null default now(),
    created_by uuid not null,
    modified_at timestamptz(6) not null default now(),
    modified_by uuid not null,

    constraint pk_access_enforcements primary key (enforcement_id),
    constraint uk_access_enforcements_external_id unique (external_id)
);

comment on table access_enforcements is '접근 제어 집행 이력 테이블';
comment on column access_enforcements.enforcement_id is '내부 관리용 PK (ULID/UUID v7)';
comment on column access_enforcements.external_id is '외부 노출용 ID (UUID v4)';
comment on column access_enforcements.request_uri is '가로챈 요청 URI';
comment on column access_enforcements.http_method is 'HTTP 메서드';
comment on column access_enforcements.client_ip is '클라이언트 IP';
comment on column access_enforcements.target_service_id is '라벨링된 포워딩 대상 서비스 ID';
comment on column access_enforcements.target_path is '포워딩 대상 경로';
comment on column access_enforcements.status is '집행 진행 상태 (EVALUATING, PERMITTED, DENIED, FAILED)';
comment on column access_enforcements.response_status is '최종 응답 HTTP 상태 코드';
comment on column access_enforcements.failure_reason is '집행 실패 및 차단 사유';
comment on column access_enforcements.created_at is '생성 일시';
comment on column access_enforcements.created_by is '생성자 ID';
comment on column access_enforcements.modified_at is '수정 일시';
comment on column access_enforcements.modified_by is '수정자 ID';

create index idx_access_enforcements_created_at on access_enforcements(created_at);