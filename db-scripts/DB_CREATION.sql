create schema USERMANAGEMENT;

create table USERMANAGEMENT.USER (
ID INT auto_increment,
USERNAME varchar(64) UNIQUE not null,
EMAIL varchar(64) UNIQUE not null,
PASSWORD varchar(128) not null,
NAME varchar(64) not null,
ROLE ENUM('user', 'admin') not null,
PHONENUMBER VARCHAR(64),
ADDRESS VARCHAR(64),
ISDELETED BIT default 0,
primary key(ID)
);

create table USERMANAGEMENT.GROUP (
ID INT auto_increment,
NAME varchar(64) not null,
DESCRIPTION varchar(128),
ISDELETED BIT default 0,
primary key(ID)
);

create table USERMANAGEMENT.USERGROUP (
ID INT auto_increment,
GROUPID INT not null,
USERID INT not null,
ISDELETED BIT default 0,
primary key(ID),
foreign key(GROUPID) references USERMANAGEMENT.GROUP (ID),
foreign key(USERID) references USERMANAGEMENT.USER (ID)
);



DELIMITER $$
CREATE
    TRIGGER default_admin_delete_trigger BEFORE DELETE
    ON USERMANAGEMENT.USER
    FOR EACH ROW BEGIN

	IF OLD.ID = 1 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Cannot delete default admin';

	END IF;
    END$$

DELIMITER ;

DELIMITER $$
CREATE
    TRIGGER default_admin_update_trigger BEFORE UPDATE
    ON USERMANAGEMENT.USER
    FOR EACH ROW BEGIN

	IF OLD.ID = 1 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Cannot update default admin';

	END IF;
    END$$

DELIMITER ;


DELIMITER $$
CREATE
    TRIGGER default_group_delete_trigger BEFORE DELETE
    ON USERMANAGEMENT.GROUP
    FOR EACH ROW BEGIN

	IF OLD.ID = 1 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Cannot delete default group';

	END IF;
    END$$

DELIMITER ;

DELIMITER $$
CREATE
    TRIGGER default_usergroup_delete_trigger BEFORE DELETE
    ON USERMANAGEMENT.USERGROUP
    FOR EACH ROW BEGIN

	IF OLD.ID = 1 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Cannot remove default user from default group';

	END IF;
    END$$

DELIMITER ;

DELIMITER $$
CREATE
    TRIGGER default_usergroup_update_trigger BEFORE UPDATE
    ON USERMANAGEMENT.USERGROUP
    FOR EACH ROW BEGIN

	IF OLD.ID = 1 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Cannot remove default user from default group';

	END IF;
    END$$

DELIMITER ;


insert into USERMANAGEMENT.USER (ID, USERNAME, EMAIL, PASSWORD, NAME, ROLE, PHONENUMBER, ADDRESS) values(1,'default_admin', 'default_admin@usermail.com', '123' , 'Default Admin', 'admin', '123-056-44', 'maadi');

insert into USERMANAGEMENT.GROUP (ID, NAME, DESCRIPTION) values(1, 'default_group', 'default group (cannot be deleted');

insert into USERMANAGEMENT.GROUP (ID, NAME, DESCRIPTION) values(2, 'group 1', 'normal group');

insert into USERMANAGEMENT.USERGROUP values(1,1,1,0);