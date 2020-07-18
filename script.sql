
use test;
drop table users;
create table users(activeUsers int(11), version int(5));
insert into users
values(0,1.0);
select * from test.users;