DROP TABLE Lumber IF EXISTS;
DROP TABLE Timber IF EXISTS;
DROP TABLE Orders IF EXISTS;
DROP TABLE Task IF EXISTS;
DROP TABLE ASSIGNMENT IF EXISTS;

CREATE TABLE TIMBER (
  ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  WOOD_TYPE VARCHAR(20) NOT NULL,
  FESTMETER DOUBLE NOT NULL,
  AMOUNT INT NOT NULL,
  MAX_AMOUNT INT NOT NULL,
  LENGTH INT NOT NULL,
  QUALITY VARCHAR (10) NOT NULL,
  DIAMETER INT NOT NULL,
  PRICE INT NOT NULL,
  LAST_EDITED TIMESTAMP
);

create table LUMBER(
  ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  DESCRIPTION varchar(50) NOT NULL,
  FINISHING varchar(30) NOT NULL,
  WOOD_TYPE varchar(20) NOT NULL,
  QUALITY varchar(20) NOT NULL,
  SIZE INT NOT NULL,
  WIDTH INT NOT NULL,
  LENGTH INT NOT NULL,
  QUANTITY INT NOT NULL,
  RESERVED_QUANTITY INT NOT NULL
);

create table ORDERS (
  ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  CUSTOMER_NAME varchar(50) NOT NULL,
  CUSTOMER_ADDRESS varchar(50) NOT NULL,
  CUSTOMER_UID varchar(20) NOT NULL,
  ORDER_DATE Datetime NOT NULL,
  DELIVERY_DATE TIMESTAMP,
  INVOICE_DATE TIMESTAMP,
  GROSS_AMOUNT INT DEFAULT(0),
  NET_AMOUNT INT DEFAULT(0),
  TAX_AMOUNT INT DEFAULT(0),
  ISPAIDFLAG boolean NOT NULL,
  ISDONEFLAG boolean NOT NULL
);


CREATE TABLE TASK (
  ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  ORDERID INT NOT NULL,
  DESCRIPTION VARCHAR(50) NOT NULL,
  FINISHING VARCHAR(15) NOT NULL,
  WOOD_TYPE VARCHAR(10) NOT NULL,
  QUALITY VARCHAR(10) NOT NULL,
  SIZE INT NOT NULL,
  WIDTH INT NOT NULL,
  LENGTH INT NOT NULL,
  QUANTITY INT NOT NULL,
  PRODUCED_QUANTITY INT NOT NULL,
  PRICE INT NOT NULL,
  DONE boolean,
  IN_PROGRESS BIT DEFAULT 0,
  DELETED boolean,
  FOREIGN KEY (ORDERID) REFERENCES ORDERS(ID)
);


create table ASSIGNMENT(
  ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  CREATION_DATE Datetime NOT NULL,
  AMOUNT INT NOT NULL,
  BOX_ID INT REFERENCES TIMBER(ID),
  ISDONE boolean NOT NULL,
  TASK_ID INT NOT Null,
  FOREIGN KEY (TASK_ID) REFERENCES TASKS(ID)
);


INSERT INTO TIMBER(wood_type,festmeter,amount, MAX_AMOUNT, length, quality,diameter,price,last_edited) VALUES
( 'Fichte',203.35,151, 300,5000, 'A', 520,176,now()),
( 'Fichte',83.56,79,300, 4000, 'A', 520,173,now()),
( 'Fichte',114.35,187,800, 3500, 'A', 220,167,now()),
( 'Fichte',144.65,191,450, 5000, 'B', 440,145,now()),
( 'Fichte',153.60,314,480, 4000, 'B', 390,141,now()),
( 'Fichte',199.76,326,480, 5000, 'B', 360,138,now()),
( 'Fichte',110.88,282,500, 4000, 'C', 350,111,now()),
( 'Fichte',312.38,458,600, 4500, 'C', 320,110,now()),
( 'Fichte',60.18,77,300, 4000, 'C', 510,107,now()),
( 'Fichte',163.90,514,680, 4000, 'CX', 290,77,now()),
( 'Fichte',47.19,48,300, 5000, 'CX', 510,72,now()),
( 'Fichte',98.40,361,680, 4000, 'CX', 280,75,now()),
( 'Tanne',230.89,190,300, 5000, 'A', 520,174,now()),
( 'Tanne',270.45,257,330, 4500, 'A', 500,173,now()),
( 'Tanne',219.32,214,450, 3500, 'A', 450,170,now()),
( 'Tanne',440.53,580,300, 4000, 'B', 510,148,now()),
( 'Tanne',116.66,193,450, 4000, 'B', 440,144,now()),
( 'Tanne',217.77,285,450, 4500, 'B', 420,144,now()),
( 'Tanne',210.28,660,680, 4000, 'C', 290,123,now()),
( 'Tanne',490.80,252,600, 4000, 'C', 320,122,now()),
( 'Tanne',42.69,87,480, 4000, 'C', 360,122,now()),
( 'Tanne',125.73,255,480, 5000, 'CX', 350,65,now()),
( 'Tanne',40.17,70,480, 4500, 'CX', 360,60,now()),
( 'Laerche',199.25,366,600, 4000, 'CX', 320,60,now()),
( 'Laerche',167.54,231,650,4500, 'CX', 300,60,now());