-- Создание таблицы пользователей
CREATE TABLE IF NOT EXISTS public.users (
    gender BOOLEAN,
    date_of_birth DATE,
    registration_date DATE,
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (INCREMENT 1 START 1),
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    password VARCHAR(255),
    phone_number VARCHAR(255),
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

-- Создание таблицы ролей
CREATE TABLE IF NOT EXISTS public.roles (
    id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY (INCREMENT 1 START 1),
    name VARCHAR(255),
    CONSTRAINT roles_pkey PRIMARY KEY (id)
);

-- Связь пользователей и ролей (многие ко многим)
CREATE TABLE IF NOT EXISTS public.users_roles (
    user_id BIGINT,
    role_id BIGINT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES public.users (id),
    FOREIGN KEY (role_id) REFERENCES public.roles (id)
);

-- Таблица для хранения кодов подтверждения
CREATE TABLE IF NOT EXISTS public.confirmation_code (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (INCREMENT 1 START 1),
    code VARCHAR(255) NOT NULL,
    own_user_id BIGINT NOT NULL REFERENCES public.users (id) ON DELETE CASCADE,
    expiry_date DATE,
    PRIMARY KEY (id)
);

-- Таблицы категорий, рекомендаций и случаев (occasion)
CREATE TABLE IF NOT EXISTS public.category (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (INCREMENT 1 START 1),
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.gift_recommendations (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (INCREMENT 1 START 1),
    gender BOOLEAN,
    min_age INT,
    max_age INT,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.occasion (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (INCREMENT 1 START 1),
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

-- Таблицы подарков и их связей
CREATE TABLE IF NOT EXISTS public.gift (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (INCREMENT 1 START 1),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    category_id BIGINT REFERENCES public.category (id),
    recommendation_id BIGINT REFERENCES public.gift_recommendations (id),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.gift_occasion (
    gift_id BIGINT REFERENCES public.gift (id) ON DELETE CASCADE,
    occasion_id BIGINT REFERENCES public.occasion (id)
);

CREATE TABLE IF NOT EXISTS public.gift_feature (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (INCREMENT 1 START 1),
    gift_id BIGINT REFERENCES public.gift (id) ON DELETE CASCADE,
    item_name VARCHAR(255) NOT NULL,
    item_value VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.gift_category (
    gift_id BIGINT REFERENCES public.gift (id) ON DELETE CASCADE,
    category_id BIGINT REFERENCES public.category (id)
);

-- Таблица корзины
CREATE TABLE IF NOT EXISTS public.cart (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (INCREMENT 1 START 1),
    user_id BIGINT REFERENCES public.users (id) ON DELETE CASCADE,
    gift_id BIGINT REFERENCES public.gift (id) ON DELETE CASCADE,
    item_count INT NOT NULL,
    PRIMARY KEY (id)
);

-- Таблица отзывов
CREATE TABLE IF NOT EXISTS public.review (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (INCREMENT 1 START 1),
    user_id BIGINT REFERENCES public.users (id) ON DELETE CASCADE,
    gift_id BIGINT REFERENCES public.gift (id) ON DELETE CASCADE,
    text TEXT,
    rating INT,
    creation_date DATE,
    PRIMARY KEY (id)
);

-- Таблица заказов
CREATE TABLE IF NOT EXISTS public.orders (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (INCREMENT 1 START 1),
    user_id BIGINT REFERENCES public.users (id) ON DELETE CASCADE,
    gift_id BIGINT REFERENCES public.gift (id),
    delivery_date DATE,
    rating INT,
    status VARCHAR(255),
    review_id BIGINT REFERENCES public.review (id),
    information VARCHAR(255),
    item_count INT,
    PRIMARY KEY (id)
);

-- Таблица избранного
CREATE TABLE IF NOT EXISTS public.favorites (
    user_id BIGINT REFERENCES public.users (id) ON DELETE CASCADE,
    gift_id BIGINT REFERENCES public.gift (id) ON DELETE CASCADE
);

-- Таблица уведомлений
CREATE TABLE IF NOT EXISTS public.notifications (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (INCREMENT 1 START 1),
    user_id BIGINT REFERENCES public.users (id) ON DELETE CASCADE,
    item_value TEXT,
    creation_datetime TIMESTAMP,
    PRIMARY KEY (id)
);

-- Таблица фотографий подарков
CREATE TABLE IF NOT EXISTS public.gift_photo (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (INCREMENT 1 START 1),
    gift_id BIGINT REFERENCES public.gift (id) ON DELETE CASCADE,
    photo_url VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);