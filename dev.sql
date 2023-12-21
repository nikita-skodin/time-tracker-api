CREATE OR REPLACE FUNCTION calculate_duration(start_time timestamp, stop_time timestamp)
    RETURNS INTERVAL AS
$BODY$
BEGIN
    IF start_time IS NOT NULL AND stop_time IS NOT NULL THEN
        RETURN stop_time - start_time;
    ELSE
        RETURN NULL;
    END IF;
END
$BODY$
    LANGUAGE plpgsql IMMUTABLE;

DROP TABLE IF EXISTS task_time_tracker;
DROP TABLE IF EXISTS app_user_time_tracker;

create table app_user_time_tracker
(
    id       UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    username varchar(255)
);

create table task_time_tracker
(
    id         UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    user_id    uuid
        constraint fkjj1i2pgajk09d4h5gvejku2ds
            references app_user_time_tracker on delete cascade ,
    context    varchar(255),
    start_time timestamp(6),
    stop_time  timestamp(6),
    duration   INTERVAL GENERATED ALWAYS AS (calculate_duration(start_time, stop_time)) STORED
);

INSERT INTO app_user_time_tracker(id, username)
VALUES ('3a84d75a-ab83-481e-a7a3-3c442e721ca3', 'nikita');

INSERT INTO task_time_tracker (context, start_time, stop_time, user_id)
VALUES ('context1', '2023-01-01 12:00:00'::timestamp, '2023-01-01 14:00:00'::timestamp,
        '3a84d75a-ab83-481e-a7a3-3c442e721ca3'),
       ('context2', '2023-01-02 10:00:00'::timestamp, '2023-01-02 12:30:00'::timestamp,
        '3a84d75a-ab83-481e-a7a3-3c442e721ca3'),
       ('context3', '2023-01-03 08:00:00'::timestamp, '2023-01-03 10:45:00'::timestamp,
        '3a84d75a-ab83-481e-a7a3-3c442e721ca3'),
       ('context4', '2023-01-04 15:30:00'::timestamp, '2023-01-04 18:00:00'::timestamp,
        '3a84d75a-ab83-481e-a7a3-3c442e721ca3'),
       ('context5', '2023-01-05 13:00:00'::timestamp, '2023-01-05 16:15:00'::timestamp,
        '3a84d75a-ab83-481e-a7a3-3c442e721ca3');

SELECT * FROM public.task_time_tracker
         WHERE user_id = '3a84d75a-ab83-481e-a7a3-3c442e721ca3'
           AND start_time >= '2023-01-01 12:00:00.000000'::timestamp
           AND stop_time <= '2023-01-03 10:45:00.000000'::timestamp;