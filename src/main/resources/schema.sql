create table persons(
	id integer auto_increment not null,
	name varchar(255) not null,
	birthday date not null,
	age integer not null,
	primary key(id)
);

create table batch_job_instance(
	job_instance_id bigint primary key,
	version bigint,
	job_name varchar(100) not null,
	job_key varchar(2500)
);

create table batch_job_execution(
	job_execution_id bigint primary key,
	version bigint,
	job_instance_id bigint not null,
	create_time timestamp not null,
	start_time timestamp default null,
	end_time timestamp default null,
	status varchar(10),
	exit_code varchar(20),
	exit_message varchar(2500),
	last_updated timestamp,
	job_configuration_location varchar(2500) null
);

create table batch_job_execution_params(
    job_execution_id bigint not null,
    type_cd varchar(6) not null,
    key_name varchar(100) not null,
    string_val varchar(250),
    date_val timestamp default null,
    long_val bigint,
    double_val double precision,
    identifying char(1) not null
);

create table batch_step_execution(
	step_execution_id bigint primary key,
	version bigint not null,
	step_name varchar(100) not null,
	job_execution_id bigint not null,
	start_time timestamp not null,
	end_time timestamp default null,
	status varchar(10),
	commit_count bigint,
	read_count bigint,
	filter_count bigint,
	write_count bigint,
	read_skip_count bigint,
	write_skip_count bigint,
	process_skip_count bigint,
	rollback_count bigint,
	exit_code varchar(20),
	exit_message varchar(2500),
	last_updated timestamp
);

create table batch_job_execution_context(
	job_execution_id bigint primary key,
	short_context varchar(2500) not null,
	serialized_context text
);

create table batch_step_execution_context(
	step_execution_id bigint primary key,
	short_context varchar(2500) not null,
	serialized_context text
);

create sequence batch_job_seq;

create sequence batch_job_execution_seq;

create sequence batch_step_execution_seq;

alter table batch_job_execution 
	add constraint job_instance_execution_fk 
	foreign key (job_instance_id) 
	references batch_job_instance(job_instance_id);

alter table batch_job_execution_params
	add constraint job_exec_params_fk
	foreign key (job_execution_id)
    references batch_job_execution(job_execution_id);

alter table batch_step_execution 
	add constraint job_execution_step_fk 
	foreign key (job_execution_id)
	references batch_job_execution(job_execution_id);

alter table batch_job_execution_context
	add constraint job_exec_ctx_fk 
	foreign key (job_execution_id)
	references batch_job_execution(job_execution_id);

alter table batch_step_execution_context
	add constraint step_exec_ctx_fk
	foreign key (step_execution_id)
	references batch_step_execution(step_execution_id);