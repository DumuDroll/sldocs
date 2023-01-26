CREATE TABLE IF NOT EXISTS faculty
(
    id                    bigint NOT NULL AUTO_INCREMENT,
    code                  varchar(255) DEFAULT NULL,
    eas_filename          varchar(255) DEFAULT NULL,
    ind_plan_zip_filename varchar(255) DEFAULT NULL,
    name                  varchar(255) DEFAULT NULL,
    psl_filename          varchar(255) DEFAULT NULL,
    ipzip_filename        varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS department
(
    id         bigint NOT NULL AUTO_INCREMENT,
    code       varchar(255) DEFAULT NULL,
    name       varchar(255) DEFAULT NULL,
    short_name varchar(255) DEFAULT NULL,
    faculty_id bigint       DEFAULT NULL,
    PRIMARY KEY (id),
    KEY          FKj2xhv1clx0m0axk2y53wm4hgl (faculty_id),
    CONSTRAINT FKj2xhv1clx0m0axk2y53wm4hgl FOREIGN KEY (faculty_id) REFERENCES faculty (id)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS discipline
(
    id         bigint NOT NULL AUTO_INCREMENT,
    code       varchar(255) DEFAULT NULL,
    name       varchar(255) DEFAULT NULL,
    short_name varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1809 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS professor
(
    id               bigint NOT NULL AUTO_INCREMENT,
    asp_num          varchar(255) DEFAULT NULL,
    autumn_asp       varchar(255) DEFAULT NULL,
    email_address    varchar(255) DEFAULT NULL,
    emailed_date     varchar(255) DEFAULT NULL,
    full_name        varchar(255) DEFAULT NULL,
    ip_filename      varchar(255) DEFAULT NULL,
    name             varchar(255) DEFAULT NULL,
    nauk_stupin      varchar(255) DEFAULT NULL,
    note             varchar(255) DEFAULT NULL,
    posada           varchar(255) DEFAULT NULL,
    psl_filename     varchar(255) DEFAULT NULL,
    spring_asp       varchar(255) DEFAULT NULL,
    stavka           varchar(255) DEFAULT NULL,
    vch_zvana        varchar(255) DEFAULT NULL,
    bach_num         varchar(255) DEFAULT NULL,
    fifth_course_num varchar(255) DEFAULT NULL,
    master_prof_num  varchar(255) DEFAULT NULL,
    master_sc_num    varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=845 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS specialty
(
    id   bigint NOT NULL AUTO_INCREMENT,
    code varchar(255) DEFAULT NULL,
    name varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS curriculum
(
    id                  bigint NOT NULL AUTO_INCREMENT,
    aspirant_hours      varchar(255) DEFAULT NULL,
    consults_hours      varchar(255) DEFAULT NULL,
    course              varchar(255) DEFAULT NULL,
    cp_hours            varchar(255) DEFAULT NULL,
    dec_cell            varchar(255) DEFAULT NULL,
    diploma_hours       varchar(255) DEFAULT NULL,
    exam_hours          varchar(255) DEFAULT NULL,
    group_names         varchar(255) DEFAULT NULL,
    ind_task_hours      varchar(255) DEFAULT NULL,
    lab_hours           varchar(255) DEFAULT NULL,
    lec_hours           varchar(255) DEFAULT NULL,
    ndrs                varchar(255) DEFAULT NULL,
    note                varchar(255) DEFAULT NULL,
    number_of_subgroups varchar(255) DEFAULT NULL,
    other_forms_hours   varchar(255) DEFAULT NULL,
    pract_hours         varchar(255) DEFAULT NULL,
    practice            varchar(255) DEFAULT NULL,
    semester            varchar(255) DEFAULT NULL,
    students_number     varchar(255) DEFAULT NULL,
    year                varchar(255) DEFAULT NULL,
    zalik_hours         varchar(255) DEFAULT NULL,
    department_id       bigint       DEFAULT NULL,
    specialty_id        bigint       DEFAULT NULL,
    consult_hours       varchar(255) DEFAULT NULL,
    PRIMARY KEY (id),
    KEY                   FK6myxkmo0upbbkh7fnds3jb67v (department_id),
    KEY                   FKcb91o408ic3xbtaddeqyi7rfi (specialty_id),
    CONSTRAINT FK6myxkmo0upbbkh7fnds3jb67v FOREIGN KEY (department_id) REFERENCES department (id),
    CONSTRAINT FKcb91o408ic3xbtaddeqyi7rfi FOREIGN KEY (specialty_id) REFERENCES specialty (id)
) ENGINE=InnoDB AUTO_INCREMENT=11874 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS curriculum_discipline
(
    curriculum_id bigint NOT NULL,
    discipline_id bigint NOT NULL,
    PRIMARY KEY (curriculum_id, discipline_id),
    KEY             FK1k2vr5qwinr8bigpvsxim61mw (discipline_id),
    CONSTRAINT FK1k2vr5qwinr8bigpvsxim61mw FOREIGN KEY (discipline_id) REFERENCES discipline (id),
    CONSTRAINT FKsgt9someseudxbcd385qae9hx FOREIGN KEY (curriculum_id) REFERENCES curriculum (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS curriculum_professor
(
    curriculum_id bigint NOT NULL,
    professor_id  bigint NOT NULL,
    PRIMARY KEY (curriculum_id, professor_id),
    KEY             FKjoosr8187uxv4a4je6ia9xnh (professor_id),
    CONSTRAINT FKjoosr8187uxv4a4je6ia9xnh FOREIGN KEY (professor_id) REFERENCES professor (id),
    CONSTRAINT FKokx7th86kbayymxdlqmomrd5a FOREIGN KEY (curriculum_id) REFERENCES curriculum (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS roles
(
    id   int NOT NULL AUTO_INCREMENT,
    name varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS users
(
    id       bigint NOT NULL AUTO_INCREMENT,
    enabled  bit(1) NOT NULL,
    name     varchar(255) DEFAULT NULL,
    password varchar(255) DEFAULT NULL,
    username varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id bigint NOT NULL,
    role_id int    NOT NULL,
    PRIMARY KEY (user_id, role_id),
    KEY       FKj6m8fwv7oqv74fcehir1a9ffy (role_id),
    CONSTRAINT FK2o0jvgh89lemvvo17cbqvdxaa FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT FKj6m8fwv7oqv74fcehir1a9ffy FOREIGN KEY (role_id) REFERENCES roles (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE
OR REPLACE VIEW eas_vm AS
select c.id                  AS id,
       c.semester            AS csem,
       c.course              AS ccor,
       d.name                AS dname,
       c.group_names         AS group_names,
       c.lec_hours           AS lec_hours,
       c.lab_hours           AS lab_hours,
       p.name                AS pname,
       c.pract_hours         AS pract_hours,
       c.note                AS note,
       c.number_of_subgroups AS number_of_subgroups,
       c.year                AS year
from ((((discipline d join curriculum_discipline cd
         on ((d.id = cd.discipline_id))) join curriculum c
        on ((c.id = cd.curriculum_id))) join curriculum_professor cp
       on ((cp.curriculum_id = c.id))) join professor p on ((p.id = cp.professor_id)))
order by d.name, char_length(c.group_names);

CREATE
OR REPLACE VIEW psl_vm AS
select c.id                  AS id,
       c.year                AS year,
       c.course              AS course,
       c.semester            AS csem,
       c.number_of_subgroups AS number_of_subgroups,
       c.students_number     AS students_number,
       c.group_names         AS group_names,
       c.lec_hours           AS lec_hours,
       c.consults_hours      AS consult_hours,
       c.lab_hours           AS lab_hours,
       c.pract_hours         AS pract_hours,
       c.ind_task_hours      AS ind_task_hours,
       c.cp_hours            AS cp_hours,
       c.zalik_hours         AS zalik_hours,
       c.exam_hours          AS exam_hours,
       c.diploma_hours       AS diploma_hours,
       c.dec_cell            AS dec_cell,
       c.ndrs                AS ndrs,
       c.practice            AS practice,
       c.aspirant_hours      AS aspirant_hours,
       dep.name              AS dep_name,
       c.other_forms_hours   AS other_forms_hours,
       p.name                AS pname,
       d.name                AS dname,
       f.name                AS fac_name,
       p.nauk_stupin         AS nauk_stupin,
       p.posada              AS posada,
       p.vch_zvana           AS vch_zvana,
       p.stavka              AS stavka
from ((((((professor p join curriculum_professor cp
           on ((p.id = cp.professor_id))) join curriculum c
          on ((c.id = cp.curriculum_id))) join curriculum_discipline cd
         on ((c.id = cd.curriculum_id))) join discipline d
        on ((d.id = cd.discipline_id))) join department dep
       on ((dep.id = c.department_id))) join faculty f on ((dep.faculty_id = f.id)))
order by d.name, char_length(c.group_names);