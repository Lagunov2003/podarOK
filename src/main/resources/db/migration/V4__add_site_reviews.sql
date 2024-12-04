-- Создание таблицы для отзывов о сайте
CREATE TABLE IF NOT EXISTS public.site_reviews (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (INCREMENT 1 START 1),
    review TEXT,
    mark INT,
    accepted BOOLEAN,
    user_id BIGINT REFERENCES public.users (id),
    PRIMARY KEY (id)
)