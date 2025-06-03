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

--Создание таблицы Ролей
CREATE TABLE role(
id SERIAL PRIMARY KEY,
role_name varchar(50) NOT NULL,
user_id INT REFERENCES users_chat(id) ON DELETE CASCADE
);