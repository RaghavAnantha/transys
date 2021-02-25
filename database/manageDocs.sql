-- Added hasDocs col to order able

 ALTER TABLE `transys`.`transysOrder`  CHANGE COLUMN `hasDocs` `hasDocs` VARCHAR(10) NULL DEFAULT 'N' COMMENT '';

update transys.transysOrder set hasDocs='N' where id !=0;