CREATE TABLE `account` (
  `accountNumber` INT NULL,
  `pin` INT NULL,
  `balance` DOUBLE NULL,
  `overdraft` DOUBLE NULL);

INSERT INTO Account (accountNumber,pin,balance,overdraft)
VALUES ('123123', '2493',10000,20000);
VALUES ('134234', '3000',30000,10000);