-- mock departments
INSERT INTO departments (id, name, location)
VALUES ('44fcd927-fa6d-4c16-bdd1-2262bb24dd16', 'Dział wsparcia technicznego', 'Budynek C, piętro 2');

INSERT INTO departments (id, name, location)
VALUES ('d98c701e-dd38-476f-bdfb-0bf292ebd4a0', 'Dział księgowości', 'Budynek A, parter');


-- mock users for test profile (default password: 'password')
-- user with ADMIN role
INSERT INTO user_accounts (id, first_name, last_name, email, is_active, role, department_id)
VALUES ('1116041a-40b7-4419-848a-78e8c9d5b09f', 'Jan', 'Kowalski', 'admin@company.com', true, 'ADMINISTRATOR', '44fcd927-fa6d-4c16-bdd1-2262bb24dd16');

INSERT INTO user_credentials (user_id, password_hash, salt)
VALUES ('1116041a-40b7-4419-848a-78e8c9d5b09f', 'FdWbzJDMjC6kM1bY8w4KFxw6m410BBUcmgOQCw1JERk=', 'e9Cu8+ZDmqzJjN/C4xYNp8VpT4rs5pk9fv7octafP+U=');

-- user with TECHNICIAN role
INSERT INTO user_accounts (id, first_name, last_name, email, is_active, role, department_id)
VALUES ('cc4fa18d-8d87-4b4d-9128-1d25bbe561d1', 'Jan', 'Nowak', 'jan.nowak@company.com', true, 'TECHNICIAN', '44fcd927-fa6d-4c16-bdd1-2262bb24dd16');

INSERT INTO user_credentials (user_id, password_hash, salt)
VALUES ('cc4fa18d-8d87-4b4d-9128-1d25bbe561d1', 'FdWbzJDMjC6kM1bY8w4KFxw6m410BBUcmgOQCw1JERk=', 'e9Cu8+ZDmqzJjN/C4xYNp8VpT4rs5pk9fv7octafP+U=');

-- user with REPORTER role
INSERT INTO user_accounts (id, first_name, last_name, email, is_active, role, department_id)
VALUES ('57679399-2b5f-470b-a512-acc2e6940feb', 'Ewa', 'Kaczmarek', 'ewa.kaczmarek@company.com', true, 'REPORTER', 'd98c701e-dd38-476f-bdfb-0bf292ebd4a0');

INSERT INTO user_credentials (user_id, password_hash, salt)
VALUES ('57679399-2b5f-470b-a512-acc2e6940feb', 'FdWbzJDMjC6kM1bY8w4KFxw6m410BBUcmgOQCw1JERk=', 'e9Cu8+ZDmqzJjN/C4xYNp8VpT4rs5pk9fv7octafP+U=');