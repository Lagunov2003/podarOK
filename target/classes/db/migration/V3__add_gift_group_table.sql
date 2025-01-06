-- Создание таблицы для групп подарков
CREATE TABLE IF NOT EXISTS public.gift_group (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (INCREMENT 1 START 1),
    PRIMARY KEY (id)
);

-- Добавление поля group_id в таблицу Gift
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'gift' AND column_name = 'group_id'
    ) THEN
        ALTER TABLE public.gift ADD COLUMN group_id BIGINT REFERENCES public.gift_group (id) ON DELETE SET NULL;
    END IF;
END $$;