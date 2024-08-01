insert into account (id, active) values (123, true);

insert into account_balance (id, account_id, balance_type, balance)
values (1, 123, 'FOOD', 100);

insert into account_balance (id, account_id, balance_type, balance)
values (2, 123, 'MEAL', 100);

insert into account_balance (id, account_id, balance_type, balance)
values (3, 123, 'CASH', 1000);