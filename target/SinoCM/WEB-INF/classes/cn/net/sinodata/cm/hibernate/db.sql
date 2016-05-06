DROP TABLE cm_file_info;
CREATE TABLE
    cm_batch_info
    (
        batchid VARCHAR(50) NOT NULL,
        sysid VARCHAR(20),
        orgid VARCHAR(20),
        createtime TIMESTAMP,
        lastmodified TIMESTAMP,
        creator VARCHAR(20),
        version VARCHAR(10) DEFAULT '',
        provincecode VARCHAR(20),
        remark VARCHAR(255),
        verify_result INT,
        verify_remark VARCHAR(255),
        PRIMARY KEY (batchid)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8

DROP TABLE cm_batch_info;
CREATE TABLE
    cm_file_info
    (
        fileid VARCHAR(20) NOT NULL,
        batchid VARCHAR(50) NOT NULL,
        filename VARCHAR(50) NOT NULL,
        MD5 VARCHAR(100),
        createtime TIMESTAMP,
        creator VARCHAR(20),
        category VARCHAR(2),
        filesize bigint,
        verify_result INT,
        verify_remark VARCHAR(255),
        invoice_no VARCHAR(20),
        PRIMARY KEY (batchid, filename)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8

DROP TABLE cm_invoice_info;
CREATE TABLE
    cm_invoice_info
    (
        invoice_no VARCHAR(20) NOT NULL,
        filename VARCHAR(20),
        author VARCHAR(20),
        createtime TIMESTAMP NULL,
        batchid VARCHAR(20),
        PRIMARY KEY (invoice_no)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8
