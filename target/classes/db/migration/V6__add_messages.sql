-- Создание таблицы для сообщений чата
CREATE TABLE IF NOT EXISTS public.messages
(
    is_read boolean NOT NULL,
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    receiver_id bigint NOT NULL,
    sender_id bigint NOT NULL,
    "timestamp" timestamp(6) without time zone NOT NULL,
    content character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT messages_pkey PRIMARY KEY (id),
    CONSTRAINT fk4ui4nnwntodh6wjvck53dbk9m FOREIGN KEY (sender_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkt05r0b6n0iis8u7dfna4xdh73 FOREIGN KEY (receiver_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

-- Очистка таблиц сообщений, заказ-подарок и отзывов на сайт
TRUNCATE TABLE gift_order RESTART IDENTITY CASCADE;
TRUNCATE TABLE site_reviews RESTART IDENTITY CASCADE;
TRUNCATE TABLE messages RESTART IDENTITY CASCADE;