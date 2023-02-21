-- MySQL Workbench Forward Engineering

SET
@OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET
@OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET
@OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema sldocs
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema sldocs
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `sldocs` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE
`sldocs` ;

-- -----------------------------------------------------
-- Table `sldocs`.`creation_metric`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sldocs`.`creation_metric`;

CREATE TABLE IF NOT EXISTS `sldocs`.`creation_metric`
(
    `id`
    BIGINT
    NOT
    NULL
    AUTO_INCREMENT,
    `teacher_number`
    INT
    NULL
    DEFAULT
    NULL,
    `time_to_form`
    DOUBLE
    NULL
    DEFAULT
    NULL,
    PRIMARY
    KEY
(
    `id`
))
    ENGINE = InnoDB
    AUTO_INCREMENT = 18
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `sldocs`.`discipline`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sldocs`.`discipline`;

CREATE TABLE IF NOT EXISTS `sldocs`.`discipline`
(
    `id`
    BIGINT
    NOT
    NULL
    AUTO_INCREMENT,
    `code`
    VARCHAR
(
    255
) NULL DEFAULT NULL,
    `name` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `short_name` VARCHAR
(
    255
) NULL DEFAULT NULL,
    PRIMARY KEY
(
    `id`
))
    ENGINE = InnoDB
    AUTO_INCREMENT = 2499
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `sldocs`.`formulary`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sldocs`.`formulary`;

CREATE TABLE IF NOT EXISTS `sldocs`.`formulary`
(
    `id`
    BIGINT
    NOT
    NULL
    AUTO_INCREMENT,
    `file_name`
    VARCHAR
(
    255
) NULL DEFAULT NULL,
    `eas_filename` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `psl_filename` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `ind_plan_zip_filename` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `department_short_name` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `department_full_name_genitive_case` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `department_full_name_nominative_case` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `academic_year` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `department_head_tittle` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `department_head_position_name` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `department_head_full_name` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `institute` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `protocol_number` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `protocol_date` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `approved_by_tittle` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `approved_by_position` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `approved_by_full_name` VARCHAR
(
    255
) NULL DEFAULT NULL,
    PRIMARY KEY
(
    `id`
))
    ENGINE = InnoDB
    AUTO_INCREMENT = 6
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `sldocs`.`roles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sldocs`.`roles`;

CREATE TABLE IF NOT EXISTS `sldocs`.`roles`
(
    `id`
    INT
    NOT
    NULL
    AUTO_INCREMENT,
    `name`
    VARCHAR
(
    255
) NULL DEFAULT NULL,
    PRIMARY KEY
(
    `id`
))
    ENGINE = InnoDB
    AUTO_INCREMENT = 2
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `sldocs`.`teacher_hours`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sldocs`.`teacher_hours`;

CREATE TABLE IF NOT EXISTS `sldocs`.`teacher_hours`
(
    `id`
    BIGINT
    NOT
    NULL
    AUTO_INCREMENT,
    `ip_filename`
    VARCHAR
(
    255
) NULL DEFAULT NULL,
    `bach_num` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `fifth_course_num` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `master_prof_num` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `master_sc_num` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `autumn_sum_fact` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `autumn_sum_plan` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `spring_sum_fact` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `spring_sum_plan` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `psl_filename` VARCHAR
(
    255
) NULL DEFAULT NULL,
    PRIMARY KEY
(
    `id`
))
    ENGINE = InnoDB
    AUTO_INCREMENT = 306
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `sldocs`.`teacher`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sldocs`.`teacher`;

CREATE TABLE IF NOT EXISTS `sldocs`.`teacher`
(
    `id`
    BIGINT
    NOT
    NULL
    AUTO_INCREMENT,
    `email_address`
    VARCHAR
(
    255
) NULL DEFAULT NULL,
    `emailed_date` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `full_name` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `name` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `nauk_stupin` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `note` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `posada` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `psl_filename` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `stavka` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `vch_zvana` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `teacher_hours_id` BIGINT NULL DEFAULT NULL,
    PRIMARY KEY
(
    `id`
),
    CONSTRAINT `fk_teacher2teacher_hours`
    FOREIGN KEY
(
    `teacher_hours_id`
)
    REFERENCES `sldocs`.`teacher_hours`
(
    `id`
))
    ENGINE = InnoDB
    AUTO_INCREMENT = 1150
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

CREATE INDEX `fk_teacher2teacher_hours` ON `sldocs`.`teacher` (`teacher_hours_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `sldocs`.`studyload_row`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sldocs`.`studyload_row`;

CREATE TABLE IF NOT EXISTS `sldocs`.`studyload_row`
(
    `id`
    BIGINT
    NOT
    NULL
    AUTO_INCREMENT,
    `aspirant_hours`
    VARCHAR
(
    255
) NULL DEFAULT NULL,
    `consults_hours` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `course` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `cp_hours` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `dec_cell` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `diploma_hours` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `exam_hours` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `group_names` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `ind_task_hours` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `lab_hours` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `lec_hours` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `ndrs` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `note` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `number_of_subgroups` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `other_forms_hours` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `pract_hours` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `practice` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `semester` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `students_number` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `year` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `zalik_hours` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `discipline_id` BIGINT NULL DEFAULT NULL,
    `teacher_id` BIGINT NULL DEFAULT NULL,
    `formulary_id` BIGINT NULL DEFAULT NULL,
    PRIMARY KEY
(
    `id`
),
    CONSTRAINT `discipline_id_fk`
    FOREIGN KEY
(
    `discipline_id`
)
    REFERENCES `sldocs`.`discipline`
(
    `id`
),
    CONSTRAINT `fk_studyload_row2formulary`
    FOREIGN KEY
(
    `formulary_id`
)
    REFERENCES `sldocs`.`formulary`
(
    `id`
),
    CONSTRAINT `teacher_id_fk`
    FOREIGN KEY
(
    `teacher_id`
)
    REFERENCES `sldocs`.`teacher`
(
    `id`
))
    ENGINE = InnoDB
    AUTO_INCREMENT = 14284
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

CREATE INDEX `discipline_id_fk` ON `sldocs`.`studyload_row` (`discipline_id` ASC) VISIBLE;

CREATE INDEX `teacher_id_fk` ON `sldocs`.`studyload_row` (`teacher_id` ASC) VISIBLE;

CREATE INDEX `fk_studyload_row2formulary` ON `sldocs`.`studyload_row` (`formulary_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `sldocs`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sldocs`.`users`;

CREATE TABLE IF NOT EXISTS `sldocs`.`users`
(
    `id`
    BIGINT
    NOT
    NULL
    AUTO_INCREMENT,
    `enabled`
    BIT
(
    1
) NOT NULL,
    `name` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `password` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `username` VARCHAR
(
    255
) NULL DEFAULT NULL,
    PRIMARY KEY
(
    `id`
))
    ENGINE = InnoDB
    AUTO_INCREMENT = 2
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `sldocs`.`users_roles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sldocs`.`users_roles`;

CREATE TABLE IF NOT EXISTS `sldocs`.`users_roles`
(
    `user_id`
    BIGINT
    NOT
    NULL,
    `role_id`
    INT
    NOT
    NULL,
    PRIMARY
    KEY
(
    `user_id`,
    `role_id`
),
    CONSTRAINT `FK2o0jvgh89lemvvo17cbqvdxaa`
    FOREIGN KEY
(
    `user_id`
)
    REFERENCES `sldocs`.`users`
(
    `id`
),
    CONSTRAINT `FKj6m8fwv7oqv74fcehir1a9ffy`
    FOREIGN KEY
(
    `role_id`
)
    REFERENCES `sldocs`.`roles`
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

CREATE INDEX `FKj6m8fwv7oqv74fcehir1a9ffy` ON `sldocs`.`users_roles` (`role_id` ASC) VISIBLE;

USE
`sldocs` ;

-- -----------------------------------------------------
-- Placeholder table for view `sldocs`.`eas_vm`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sldocs`.`eas_vm`
(
    `id`
    INT,
    `csem`
    INT,
    `ccor`
    INT,
    `discipline_name`
    INT,
    `group_names`
    INT,
    `lec_hours`
    INT,
    `lab_hours`
    INT,
    `teacher_name`
    INT,
    `pract_hours`
    INT,
    `note`
    INT,
    `number_of_subgroups`
    INT,
    `year`
    INT
);

-- -----------------------------------------------------
-- Placeholder table for view `sldocs`.`psl_vm`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sldocs`.`psl_vm`
(
    `id`
    INT,
    `year`
    INT,
    `course`
    INT,
    `csem`
    INT,
    `number_of_subgroups`
    INT,
    `students_number`
    INT,
    `group_names`
    INT,
    `lec_hours`
    INT,
    `consult_hours`
    INT,
    `lab_hours`
    INT,
    `pract_hours`
    INT,
    `ind_task_hours`
    INT,
    `cp_hours`
    INT,
    `zalik_hours`
    INT,
    `exam_hours`
    INT,
    `diploma_hours`
    INT,
    `dec_cell`
    INT,
    `ndrs`
    INT,
    `practice`
    INT,
    `aspirant_hours`
    INT,
    `dep_name_gc`
    INT,
    `dep_name_nc`
    INT,
    `other_forms_hours`
    INT,
    `teacher_name`
    INT,
    `discipline_name`
    INT,
    `institute`
    INT,
    `nauk_stupin`
    INT,
    `posada`
    INT,
    `vch_zvana`
    INT,
    `stavka`
    INT
);

-- -----------------------------------------------------
-- View `sldocs`.`eas_vm`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sldocs`.`eas_vm`;
DROP VIEW IF EXISTS `sldocs`.`eas_vm`;
USE
`sldocs`;
CREATE
OR REPLACE ALGORITHM=UNDEFINED VIEW `sldocs`.`eas_vm` AS
select `s`.`id`                  AS `id`,
       `s`.`semester`            AS `csem`,
       `s`.`course`              AS `ccor`,
       `d`.`name`                AS `discipline_name`,
       `s`.`group_names`         AS `group_names`,
       `s`.`lec_hours`           AS `lec_hours`,
       `s`.`lab_hours`           AS `lab_hours`,
       `t`.`name`                AS `teacher_name`,
       `s`.`pract_hours`         AS `pract_hours`,
       `s`.`note`                AS `note`,
       `s`.`number_of_subgroups` AS `number_of_subgroups`,
       `s`.`year`                AS `year`
from ((`sldocs`.`discipline` `d` join `sldocs`.`studyload_row` `s`
       on ((`d`.`id` = `s`.`discipline_id`))) join `sldocs`.`teacher` `t` on ((`t`.`id` = `s`.`teacher_id`)))
order by `d`.`name`, char_length(`s`.`group_names`);

-- -----------------------------------------------------
-- View `sldocs`.`psl_vm`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sldocs`.`psl_vm`;
DROP VIEW IF EXISTS `sldocs`.`psl_vm`;
USE
`sldocs`;
CREATE
OR REPLACE ALGORITHM=UNDEFINED VIEW `sldocs`.`psl_vm` AS
select `s`.`id`                                   AS `id`,
       `s`.`year`                                 AS `year`,
       `s`.`course`                               AS `course`,
       `s`.`semester`                             AS `csem`,
       `s`.`number_of_subgroups`                  AS `number_of_subgroups`,
       `s`.`students_number`                      AS `students_number`,
       `s`.`group_names`                          AS `group_names`,
       `s`.`lec_hours`                            AS `lec_hours`,
       `s`.`consults_hours`                       AS `consult_hours`,
       `s`.`lab_hours`                            AS `lab_hours`,
       `s`.`pract_hours`                          AS `pract_hours`,
       `s`.`ind_task_hours`                       AS `ind_task_hours`,
       `s`.`cp_hours`                             AS `cp_hours`,
       `s`.`zalik_hours`                          AS `zalik_hours`,
       `s`.`exam_hours`                           AS `exam_hours`,
       `s`.`diploma_hours`                        AS `diploma_hours`,
       `s`.`dec_cell`                             AS `dec_cell`,
       `s`.`ndrs`                                 AS `ndrs`,
       `s`.`practice`                             AS `practice`,
       `s`.`aspirant_hours`                       AS `aspirant_hours`,
       `f`.`department_full_name_genitive_case`   AS `dep_name_gc`,
       `f`.`department_full_name_nominative_case` AS `dep_name_nc`,
       `s`.`other_forms_hours`                    AS `other_forms_hours`,
       `t`.`name`                                 AS `teacher_name`,
       `d`.`name`                                 AS `discipline_name`,
       `f`.`institute`                            AS `institute`,
       `t`.`nauk_stupin`                          AS `nauk_stupin`,
       `t`.`posada`                               AS `posada`,
       `t`.`vch_zvana`                            AS `vch_zvana`,
       `t`.`stavka`                               AS `stavka`
from (((`sldocs`.`teacher` `t` join `sldocs`.`studyload_row` `s`
        on ((`t`.`id` = `s`.`teacher_id`))) join `sldocs`.`discipline` `d`
       on ((`d`.`id` = `s`.`discipline_id`))) join `sldocs`.`formulary` `f` on ((`f`.`id` = `s`.`formulary_id`)))
order by `d`.`name`, char_length(`s`.`group_names`);

SET
SQL_MODE=@OLD_SQL_MODE;
SET
FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET
UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;