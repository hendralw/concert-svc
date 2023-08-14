TRUNCATE TABLE tbl_concert;
TRUNCATE TABLE tbl_ticket;

INSERT INTO tbl_concert (id, name, date, venue)
VALUES
    ('5d700f63-7e12-4a50-a9d7-70adad1d423a', 'Concert A', '2023-08-12', 'Venue X'),
    ('5d700f63-7e12-4a50-a9d7-70adad1d423b', 'Concert B', '2023-08-11', 'Venue Y'),
    ('5d700f63-7e12-4a50-a9d7-70adad1d423c', 'Concert C', '2023-08-13', 'Venue Z'),
    ('5d700f63-7e12-4a50-a9d7-70adad1d423d', 'Concert D', '2023-08-14', 'Venue A'),
    ('5d700f63-7e12-4a50-a9d7-70adad1d423e', 'Concert E', '2023-08-15', 'Venue B');

INSERT INTO tbl_ticket (id, concert_id, type, price, available_qty)
VALUES
    ('1d700f63-7e12-4a50-a9d7-70adad1d423a', '5d700f63-7e12-4a50-a9d7-70adad1d423a', 'VIP', 100.0, 50),
    ('1d700f63-7e12-4a50-a9d7-70adad1d423b', '5d700f63-7e12-4a50-a9d7-70adad1d423a', 'Regular', 50.0, 100),
    ('1d700f63-7e12-4a50-a9d7-70adad1d423c', '5d700f63-7e12-4a50-a9d7-70adad1d423b', 'VIP', 120.0, 30),
    ('1d700f63-7e12-4a50-a9d7-70adad1d423d', '5d700f63-7e12-4a50-a9d7-70adad1d423b', 'Regular', 60.0, 80),
    ('1d700f63-7e12-4a50-a9d7-70adad1d423e', '5d700f63-7e12-4a50-a9d7-70adad1d423c', 'VIP', 90.0, 20),
    ('1d700f63-7e12-4a50-a9d7-70adad1d423f', '5d700f63-7e12-4a50-a9d7-70adad1d423c', 'Regular', 45.0, 70),
    ('1d700f63-7e12-4a50-a9d7-70adad1d4240', '5d700f63-7e12-4a50-a9d7-70adad1d423d', 'VIP', 110.0, 25),
    ('1d700f63-7e12-4a50-a9d7-70adad1d4241', '5d700f63-7e12-4a50-a9d7-70adad1d423d', 'Regular', 55.0, 60),
    ('1d700f63-7e12-4a50-a9d7-70adad1d4242', '5d700f63-7e12-4a50-a9d7-70adad1d423e', 'VIP', 80.0, 15),
    ('1d700f63-7e12-4a50-a9d7-70adad1d4243', '5d700f63-7e12-4a50-a9d7-70adad1d423e', 'Regular', 40.0, 50);



