-- Testing this script as this script is donloaded from cloud
use test;
drop table users;
create table `users`(
`activeUsers` int(11) default 0, 
`version` int(5),
`webUsers` int(11) default 0,
`link` varchar(200)
);
insert into users(`version`,`link`)values  (1.0,'https://elasticbeanstalk-ap-south-1-028486104386.s3.ap-south-1.amazonaws.com/SpringSecurityJDBCAuthenticationMySQL.zip');

select * from test.users;