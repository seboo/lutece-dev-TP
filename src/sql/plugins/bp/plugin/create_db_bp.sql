
--
-- Structure for table bp_project
--

DROP TABLE IF EXISTS bp_project;
CREATE TABLE bp_project (
id_project int(6) NOT NULL,
name varchar(50) NOT NULL default '',
description varchar(255) NOT NULL default '',
image_url varchar(255) NOT NULL default '',
PRIMARY KEY (id_project)
);
