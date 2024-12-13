create table user
(
    user_id  varchar(50)  not null
        primary key,
    username varchar(255) null,
    password varchar(255) null
);

create table budget
(
    budget_name varchar(50)    not null
        primary key,
    user_id     varchar(50)    null,
    amount      decimal(10, 2) null,
    start_date  date           null,
    end_date    date           null,
    limit       decimal(10, 2) null
);

create table expense
(
    expense_id  varchar(50)    not null
        primary key,
    user_id     varchar(50)    null,
    amount      decimal(10, 2) null,
    description varchar(200)   null,
    date        date           null,
    category    varchar(100)   null
);

create table financialaccount
(
    account_id   varchar(50)    not null
        primary key,
    user_id      varchar(50)    null,
    account_name varchar(255)   null,
    number       int            null,
    account_type varchar(255)   null,
    balance      decimal(10, 2) null
);

create table income
(
    income_id   varchar(50)    not null
        primary key,
    user_id     varchar(50)    null,
    amount      decimal(10, 2) null,
    description varchar(200)   null,
    date        date           null,
    category    varchar(100)   null,
    account     varchar(100)   null
);

create table financialgoal
(
    goal_id       int auto_increment
        primary key,
    user_id       varchar(50)    null,
    expense_id    varchar(50)    null,
    income_id     varchar(50)    null,
    budget_name   varchar(50)    null,
    name          varchar(255)   null,
    target_amount decimal(10, 2) null,
    target_date   date           null,
    achieved      tinyint(1)     null,
    category      varchar(50)    null
);

create table reminder
(
    reminder_id   int auto_increment
        primary key,
    user_id       varchar(50)   null,
    reminder_date date          null,
    reminder_text varchar(1000) null,
    reminder_time time          null,
    goal_id       int           null
);

-- Add foreign key constraints
ALTER TABLE budget
    ADD CONSTRAINT budget_user_fk
        FOREIGN KEY (user_id) REFERENCES user (user_id)
            ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE expense
    ADD CONSTRAINT expense_user_fk
        FOREIGN KEY (user_id) REFERENCES user (user_id)
            ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE financialaccount
    ADD CONSTRAINT financialaccount_user_fk
        FOREIGN KEY (user_id) REFERENCES user (user_id)
            ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE income
    ADD CONSTRAINT income_user_fk
        FOREIGN KEY (user_id) REFERENCES user (user_id)
            ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE financialgoal
    ADD CONSTRAINT financialgoal_expense_fk
        FOREIGN KEY (expense_id) REFERENCES expense (expense_id)
            ON UPDATE CASCADE ON DELETE CASCADE,
ADD CONSTRAINT financialgoal_income_fk
FOREIGN KEY (income_id) REFERENCES income (income_id)
ON UPDATE CASCADE ON DELETE CASCADE,
    ADD CONSTRAINT financialgoal_budget_fk
    FOREIGN KEY (budget_name) REFERENCES budget (budget_name)
   ON UPDATE CASCADE ON DELETE CASCADE,
    ADD CONSTRAINT financialgoal_user_fk
    FOREIGN KEY (user_id) REFERENCES user (user_id)
      ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE reminder
    ADD CONSTRAINT reminder_goal_fk
        FOREIGN KEY (goal_id) REFERENCES financialgoal (goal_id)
            ON UPDATE CASCADE ON DELETE CASCADE,
ADD CONSTRAINT reminder_user_fk
FOREIGN KEY (user_id) REFERENCES user (user_id)
ON UPDATE CASCADE ON DELETE CASCADE;

CREATE TABLE Reminder_Goal_Association (
                                           reminder_id INT,
                                           goal_id INT,
                                           created_at TIMESTAMP,
                                           updated_at TIMESTAMP,
                                           FOREIGN KEY (reminder_id) REFERENCES Reminder(reminder_id)ON UPDATE CASCADE ON DELETE CASCADE,
                                           FOREIGN KEY (goal_id) REFERENCES FinancialGoal(goal_id) ON UPDATE CASCADE ON DELETE CASCADE
);