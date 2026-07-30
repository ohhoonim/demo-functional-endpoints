create table if not exists attribute_sources (
    attribute_source_id uuid not null,
    external_id uuid not null,
    
    name text not null,
    source_type varchar(30) not null,
    endpoint text not null,
    timeout_ms text,
    status varchar(30) not null,
    last_synced_at timestamptz(6),
    last_sync_result text,
    
    created_at timestamptz(6) not null default now(),
    created_by uuid not null,
    modified_at timestamptz(6) not null default now(),
    modified_by uuid not null,

    constraint pk_attribute_sources primary key (attribute_source_id),
    constraint uk_attribute_sources_external_id unique (external_id),
    constraint uk_attribute_sources_name unique (name)
);

comment on table attribute_sources is '속성 정보 데이터 원천 레지스트리 테이블';
comment on column attribute_sources.attribute_source_id is '내부 관리용 PK (ULID/UUID v7)';
comment on column attribute_sources.external_id is '외부 노출용 ID (UUID v4)';
comment on column attribute_sources.name is '속성 원천명';
comment on column attribute_sources.source_type is '원천 타입 (HR_DB, LDAP, API 등)';
comment on column attribute_sources.endpoint is '연동 엔드포인트 URL/Connection String';
comment on column attribute_sources.timeout_ms is '타임아웃 설정 (ms)';
comment on column attribute_sources.status is '속성 원천 활성화 상태';
comment on column attribute_sources.last_synced_at is '최종 동기화 일시';
comment on column attribute_sources.last_sync_result is '최종 동기화 결과/오류 메시지';
comment on column attribute_sources.created_at is '생성 일시';
comment on column attribute_sources.created_by is '생성자 ID';
comment on column attribute_sources.modified_at is '수정 일시';
comment on column attribute_sources.modified_by is '수정자 ID';

create index idx_attribute_sources_created_at on attribute_sources(created_at);