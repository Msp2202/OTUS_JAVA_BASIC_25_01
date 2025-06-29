-- СОздание таблицы Пользователей
create table users_chat(
id SERIAL primary key,
login varchar(50) not null unique,
password varchar(50) not null,
user_name varchar(50) not null unique,
role varchar(50) not null DEFAULT 'USER'
);

-- Заполняем БД
INSERT INTO users_chat (login, password, user_name, role) VALUES
('qwe', 'qwe', 'qwe1', 'USER'),
('asd', 'asd', 'asd1', 'USER'),
('zxc', 'zxc', 'zxc1', 'USER'),
('admin', 'admin', 'admin', 'ADMIN');

--Создаем справочник ролей
CREATE TABLE public.roles (
	id serial4 NOT NULL,
	"name" varchar(50) NOT NULL,
	CONSTRAINT roles_name_key UNIQUE (name),
	CONSTRAINT roles_pkey PRIMARY KEY (id)
);

-- ЗАполняем
INSERT INTO roles (name) VALUES
    ('USER'), ('ADMIN'), ('MODERATOR');

--  Создаем таблицу связи пользователей с ролями
CREATE TABLE user_roles (
    user_id INT REFERENCES users_chat(id) ON DELETE CASCADE,
    role_id INT REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);
-- Переносим существующие назначения
INSERT INTO user_roles(user_id, role_id)
SELECT
u.id AS user_id
r.id AS role_id
FROM users_chat u
JOIN role r ON
    CASE
            WHEN u.id = 4 THEN r.name = 'ADMIN'
            WHEN u.id = 3 THEN r.name IN ('USER', 'MODERATOR')
            WHEN u.id = 6 THEN r.name = 'ADMIN'
            ELSE r.name = 'USER'
        END;
SELECT role From users_chat WHERE id=3 -> USER;
SELECT role_id From user_roles WHERE user_id = 3; -> 1. 3
UPDATE INTO user_chat (role) VALUE ('MODERATOR')
WHERE id=3
-- Автоматическое обновление не произошло, нашел решение пока очень сложное
Решение 1: Триггер для автоматического обновления