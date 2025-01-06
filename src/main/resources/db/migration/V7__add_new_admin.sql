UPDATE users
SET
    date_of_birth = '24-08-2002',
    gender = 'true',
    registration_date = '27-12-2024',
    email = 'petrov.nikita702@mail.ru',
    first_name = 'Никита',
    last_name = 'Петров',
    password = '$2a$10$qCwpEnhMUnDVI9/1LzdCAepS3Wn3cq8W9J0sCzAdtNgD7pexGKTF.',
    phone_number = '+77777777777'
WHERE
    id = 2;

UPDATE users_roles
SET role_id = 1
WHERE user_id = 2;