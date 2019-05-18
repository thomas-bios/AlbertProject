DROP TABLES IF EXISTS jenuage_docs, jenuage_users;

CREATE TABLE `jenuage_docs` (
    `user` int(10) NOT NULL,i
    `date` char(255) NOT NULL,
    `path` varchar(1024) NOT NULL,
    `name` varchar(255) NOT NULL,
    `ext` varchar(16) NOT NULL,
    `share` int(1) NOT NULL,
    `folder` int(1) NOT NULL,
    `hash` varchar(255) NOT NULL,
    `file_id` int(16) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `parent_id` int(16) NOT NULL
);
CREATE TABLE `jenuage_users` (
    `id` int(10) NOT NULL,
    `user_name` char(255) NOT NULL,
    `password` char(255) NOT NULL
);
