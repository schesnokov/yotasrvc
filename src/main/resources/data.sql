INSERT INTO gigabytes (id, amount, days_count, package_name) VALUES
  (1, 10, 30, '10 Gigs'),
  (2, 20, 60, '20 Gigs');

INSERT INTO minutes (id, amount, days_count, package_name) VALUES
  (1, 100, 30, '100 Mins'),
  (2, 200, 60, '200 Mins');

INSERT INTO account (acc_id, active, gigabytes_expiration, minutes_expiration, gigabytes_package_id, minutes_package_id) VALUES
  (1, 1, parsedatetime('31-12-2019 23:59:59', 'dd-MM-yyyy hh:mm:ss'), parsedatetime('31-12-2019 23:59:59', 'dd-MM-yyyy hh:mm:ss'),
   2, 2),
   (2, 1, parsedatetime('31-12-2019 23:59:59', 'dd-MM-yyyy hh:mm:ss'), parsedatetime('31-12-2019 23:59:59', 'dd-MM-yyyy hh:mm:ss'),
   1, 1);