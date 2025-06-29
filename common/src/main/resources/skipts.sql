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
-- Создаем таблицу bans
CREATE TABLE public.bans (
  id SERIAL PRIMARY KEY,
  user_id INT NOT NULL REFERENCES users_chat(id) ON DELETE CASCADE,
  banned_until TIMESTAMP,
  reason TEXT,
  banned_by INT REFERENCES users_chat(id),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  is_permanent BOOLEAN DEFAULT FALSE
);

-- Комментарий к таблице
COMMENT ON TABLE public.bans IS 'Таблица для хранения информации о банах пользователей';

-- Комментарии к колонкам
COMMENT ON COLUMN public.bans.id IS 'Уникальный идентификатор записи о бане';
COMMENT ON COLUMN public.bans.user_id IS 'ID забаненного пользователя (связь с таблицей users_chat)';
COMMENT ON COLUMN public.bans.banned_until IS 'Дата и время, до которого действует бан';
COMMENT ON COLUMN public.bans.reason IS 'Причина бана (может быть NULL)';
COMMENT ON COLUMN public.bans.banned_by IS 'ID пользователя, который выдал бан (связь с таблицей users_chat)';
COMMENT ON COLUMN public.bans.created_at IS 'Дата и время создания записи о бане';
COMMENT ON COLUMN public.bans.is_permanent IS 'Флаг постоянного бана (если TRUE, бан бессрочный)';

-- Обновляем таблицу users_chat
ALTER TABLE users_chat ADD COLUMN is_active BOOLEAN DEFAULT TRUE;