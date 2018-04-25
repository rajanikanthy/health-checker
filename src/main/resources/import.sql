drop table if exists service_registry;
create table service_registry(id INTEGER primary key autoincrement, name TEXT, uri TEXT, username TEXT, password TEXT, query TEXT, servicetype TEXT);