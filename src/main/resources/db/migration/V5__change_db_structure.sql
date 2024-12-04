--Изменение таблицы с заказами
ALTER TABLE public.orders
    DROP COLUMN rating,
    DROP COLUMN review_id,
    DROP COLUMN item_count,
    DROP COLUMN gift_id,
    ADD COLUMN from_delivery_time TIME,
    ADD COLUMN to_delivery_time TIME,
    ADD COLUMN pay_method VARCHAR(255),
    ADD COLUMN order_cost DECIMAL(10, 2),
    ADD COLUMN recipient_name VARCHAR(255),
    ADD COLUMN recipient_email VARCHAR(255),
    ADD COLUMN recipient_phone_number VARCHAR(255);

--Создание таблицы связи заказа и подарков в нём
CREATE TABLE IF NOT EXISTS public.gift_order (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (INCREMENT 1 START 1),
    order_id BIGINT REFERENCES public.orders (id) ON DELETE CASCADE,
    gift_id BIGINT REFERENCES public.gift (id),
    item_count INT,
    PRIMARY KEY (id)
);

-- Вставка данных в таблицу заказов
INSERT INTO public.orders(delivery_date, user_id, information, status, from_delivery_time, to_delivery_time, pay_method, order_cost, recipient_name, recipient_email, recipient_phone_number) VALUES
('2024-11-10', 62, 'г.Ярославль ул. Чехова д. 96 кв. 1', 'Доставлен', '08:00:00', '11:00:00', 'Картой', 500.00, 'Иванов Иван', 'ivanov@mail.com', '+79001234567'),
('2024-11-10', 4, 'г.Ярославль бульвар Ломоносова д. 41 кв. 2', 'Доставлен', '11:00:00', '14:00:00', 'Онлайн', 400.00, 'Петрова Мария', 'petrova@mail.com', '+79007654321'),
('2024-10-16', 5, 'г.Ярославль спуск Ленина д. 8 кв. 3', 'Доставлен', '14:00:00', '17:00:00', 'Картой', 600.00, 'Смирнов Алексей', 'smirnov@mail.com', '+79003216543'),
('2024-10-25', 49, 'г.Ярославль проезд Косиора д. 53 кв. 4', 'Доставлен', '11:00:00', '14:00:00', 'Наличные', 450.00, 'Лебедев Олег', 'lebedev@mail.com', '+79004567890'),
('2024-11-01', 4, 'г.Ярославль пр. Сталина д. 10 кв. 5', 'Доставлен', '14:00:00', '17:00:00', 'Онлайн', 550.00, 'Тимофеева Ольга', 'timofeeva@mail.com', '+79001112233'),
('2024-11-15', 50, 'г.Ярославль пр. Домодедовская д. 62 кв. 6', 'Доставлен', '14:00:00', '17:00:00', 'Наличные', 700.00, 'Кузнецов Сергей', 'kuznetsov@mail.com', '+79006677889'),
('2024-11-08', 8, 'г.Ярославль пл. Бухарестская д. 31 кв. 7', 'Доставлен', '14:00:00', '17:00:00', 'Онлайне', 350.00, 'Федорова Анна', 'fedorova@mail.com', '+79009876543'),
('2024-11-10', 25, 'г.Ярославль въезд Косиора д. 6 кв. 8', 'Доставлен', '11:00:00', '14:00:00', 'Картой', 600.00, 'Захаров Илья', 'zaharov@mail.com', '+79002223344'),
('2024-10-26', 4, 'г.Ярославль шоссе Ладыгина д. 57 кв. 9', 'Доставлен', '11:00:00', '14:00:00', 'Онлайн', 480.00, 'Максимова Светлана', 'maksimova@mail.com', '+79005432109'),
('2024-11-08', 4, 'г.Ярославль ул. Уральская д. 30 кв. 10', 'Доставлен', '11:00:00', '14:00:00', 'Онлайн', 520.00, 'Васильев Дмитрий', 'vasilyev@mail.com', '+79006543210');

-- Вставка данных в таблицу связи заказов и подарков
INSERT INTO public.gift_order (order_id, gift_id, item_count) VALUES
(1, 22, 1),
(2, 40, 2),
(3, 31, 1),
(4, 25, 3),
(5, 22, 2),
(6, 43, 1),
(7, 15, 4),
(8, 21, 1),
(9, 33, 2),
(10, 36, 1);
