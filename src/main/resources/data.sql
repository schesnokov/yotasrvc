INSERT INTO gigabytes (id, amount, days_count, expiration_date) VALUES (1, 10, 15, parsedatetime('03-11-2019', 'dd-MM-yyyy')),
                                                                       (2, 30, 30, parsedatetime('05-11-2019', 'dd-MM-yyyy')),
                                                                       (3, 100, 60, parsedatetime('05-12-2019', 'dd-MM-yyyy'));

INSERT INTO minutes (id, amount, days_count, expiration_date) VALUES (1, 200, 15, parsedatetime('08-11-2019', 'dd-MM-yyyy')),
                                                                     (2, 300, 30, parsedatetime('10-11-2019', 'dd-MM-yyyy')),
                                                                     (3, 500, 60, parsedatetime('30-11-2019', 'dd-MM-yyyy'));

INSERT INTO account (acc_id, phone_number, active, minutes_package_id, gigabytes_package_id) VALUES (1, '+79213335671', 1, 1, 1),
                                                                                                    (2, '+79213335672', 1, 2, 2),
                                                                                                    (3, '+79213335673', 0, 3, 3);

