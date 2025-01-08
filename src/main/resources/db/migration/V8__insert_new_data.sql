TRUNCATE TABLE site_reviews RESTART IDENTITY CASCADE;

INSERT INTO site_reviews(review, mark, accepted, user_id) VALUES
('Отличный сайт, чтобы подобрать интересный подарок!', 5, true, 12),
('Мне всё понравилось. Буду заказывать здесь постоянно!', 5, true, 14),
('Очень быстрая доставка, общительный администратор! Всё супер!', 5, true, 16),
('Лучший сайт по подбору подарков!', 5, true, 18),
('Разнообразный ассортимент, быстрая доставка, приятный интерфейс!', 5, true, 19);

UPDATE orders
SET status = 'Оформлен',
    from_delivery_time = '8:00:00',
    to_delivery_time = '11:00:00',
    pay_method = 'Картой',
    order_cost = 1800.00,
    recipient_name = 'Иванов Иван',
    recipient_email = 'ivanov@mail.ru',
    recipient_phone_number = '+71234567890'
WHERE id BETWEEN 1 AND 5;

UPDATE orders
SET status = 'Доставлен',
    from_delivery_time = '11:00:00',
    to_delivery_time = '13:00:00',
    pay_method = 'Картой',
    order_cost = 2800.00,
    recipient_name = 'Семенов Семен',
    recipient_email = 'semenov@yandex.ru',
    recipient_phone_number = '+71234567890'
WHERE id BETWEEN 6 AND 10;

UPDATE orders
SET status = 'Собран'
WHERE id BETWEEN 11 AND 14;

UPDATE orders
SET status = 'В пути'
WHERE id BETWEEN 15 AND 20;

INSERT INTO gift_order(order_id, gift_id, item_count) VALUES
(1, 1, 1),
(1, 2, 2),
(2, 3, 1),
(2, 4, 4),
(3, 17, 4),
(4, 12, 5),
(5, 16, 4),
(6, 15, 3),
(7, 14, 4),
(8, 21, 4),
(9, 11, 1),
(10, 22, 1),
(11, 24, 1),
(12, 27, 1),
(13, 21, 1),
(14, 26, 1),
(15, 30, 1),
(16, 12, 1),
(17, 14, 1),
(18, 16, 1),
(19, 4, 1),
(20, 1, 1);