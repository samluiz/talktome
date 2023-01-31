DROP TABLE IF EXISTS `tb_user`;

create table tb_user (
		id bigint not null auto_increment,
		first_name varchar(255),
		last_name varchar(255),
        gender smallint,
        partner_id bigint,
        email varchar(255),
        phone varchar(255),
        password varchar(255),
        created_at timestamp(6),
        updated_at timestamp(6),
        primary key (id)
    );
    
DROP TABLE IF EXISTS `tb_message`;

create table tb_message (
		id bigint not null auto_increment,
		title varchar(255),
		text varchar(255),
		mood smallint,
		from_user_id bigint,
		to_user_id bigint,
        created_at timestamp(6),
        updated_at timestamp(6),
        primary key (id)
    );
    
DROP TABLE IF EXISTS `tb_reply`;

create table tb_reply (
		id bigint not null auto_increment,
		title varchar(255),
		text varchar(255),
		reaction smallint,
		from_user_id bigint,
		to_user_id bigint,
		reply_to bigint,
        created_at timestamp(6),
        updated_at timestamp(6),
        primary key (id)
    )