UPDATE users
SET
    date_of_birth = '24-10-2002',
    gender = 'true',
    registration_date = '29-12-2024',
    email = 'zhizhindm@yandex.ru',
    first_name = 'Дмитрий',
    last_name = 'Жижин',
    password = '$2a$10$LaNOGSbo7mQIIczV4OFho.8mTbODfXcrzXaipSJ2F1SVV48Rn2Sjy',
    phone_number = '+78888888888'
WHERE
    id = 68;

UPDATE users_roles
SET role_id = 2
WHERE user_id = 68;