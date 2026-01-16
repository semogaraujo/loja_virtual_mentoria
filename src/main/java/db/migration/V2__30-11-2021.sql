/*
SELECT CONSTRAINT_NAME, TABLE_NAME, COLUMN_NAME
  FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
 WHERE TABLE_SCHEMA = 'loja_virtual_mentoria'
   AND CONSTRAINT_NAME REGEXP '^UK[0-9A-Za-z]{10,}$';
 */ 

ALTER TABLE usuarios_acesso DROP FOREIGN KEY acesso_fk;
ALTER TABLE usuarios_acesso DROP INDEX UK8bak9jswon2id2jbunuqlfl9e;

ALTER TABLE usuarios_acesso
ADD CONSTRAINT acesso_fk FOREIGN KEY (acesso_id) REFERENCES acesso(id);
