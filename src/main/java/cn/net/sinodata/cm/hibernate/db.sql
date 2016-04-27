DROP TABLE cm_file_info;
CREATE TABLE cm_file_info ( fileid varchar(20) NOT NULL, batchid varchar(50) NOT NULL, filename varchar(50), MD5 varchar(100), createtime timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL, creator varchar(20), category varchar(2), PRIMARY KEY (fileid, batchid) ) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE cm_batch_info;
CREATE TABLE cm_batch_info ( batchid varchar(50) NOT NULL, sysid varchar(20), orgid varchar(20), createtime timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL, lastmodified timestamp DEFAULT '0000-00-00 00:00:00' NULL, creator varchar(20), version varchar(10) DEFAULT '""', provincecode varchar(20), PRIMARY KEY (batchid) ) ENGINE=InnoDB DEFAULT CHARSET=latin1;
