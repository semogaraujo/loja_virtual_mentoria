ALTER TABLE `loja_virtual_mentoria`.`pessoa_fisica` 
ADD COLUMN `tipo_pessoa` VARCHAR(255) NOT NULL AFTER `data_nascimento`;
ALTER TABLE `loja_virtual_mentoria`.`pessoa_juridica` 
ADD COLUMN `tipo_pessoa` VARCHAR(45) NOT NULL AFTER `razao_social`;