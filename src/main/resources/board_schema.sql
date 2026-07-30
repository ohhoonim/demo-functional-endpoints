create table if not exists boards (
    board_id uuid not null,
    external_id uuid not null,
    title text not null,
    contents text not null,
    nick_name text not null,
    created_at timestamptz(6) not null default now(),
    created_by uuid not null,
    modified_at timestamptz(6) not null default now(),
    modified_by uuid not null,

    constraint pk_boards primary key (board_id),
    constraint uk_boards_external_id unique (external_id)
);

comment on table boards is '게시판 마스터 테이블';
comment on column boards.board_id is '내부 관리용 식별자 (ULID 또는 UUID v7)';
comment on column boards.external_id is '외부 노출용 식별자 (UUID v4)';
comment on column boards.title is '게시글 제목';
comment on column boards.contents is '게시글 내용';
comment on column boards.nick_name is '작성자 닉네임';
comment on column boards.created_at is '생성 일시';
comment on column boards.created_by is '생성자 식별자';
comment on column boards.modified_at is '수정 일시';
comment on column boards.modified_by is '수정자 식별자';

create index idx_boards_created_at on boards(created_at);