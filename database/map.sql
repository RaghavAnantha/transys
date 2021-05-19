CREATE TABLE `transys`.`geocode` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  `address` varchar(250) NOT NULL,
  `latLng` varchar(75) NOT NULL,
  PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

ALTER TABLE `transys`.`geocode` 
ADD UNIQUE INDEX `geocode_address_UNIQ` (`address` ASC)  COMMENT '';

CREATE TABLE `transys`.`vehicle` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  `number` varchar(50) NOT NULL,
  `vin` varchar(75) NOT NULL,
  `year` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `registrationNumber` varchar(50) DEFAULT NULL,
  `make` varchar(75) NOT NULL,
  `model` varchar(75) NOT NULL,
  `size` int(11) NOT NULL,
  PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
 
ALTER TABLE `transys`.`vehicle` 
ADD UNIQUE INDEX `vehicle_number_UNIQ` (`number` ASC)  COMMENT '';

ALTER TABLE `transys`.`transysOrder` 
ADD COLUMN `vehicleId` BIGINT(20) NULL DEFAULT NULL COMMENT '' AFTER `hasDocs`;

ALTER TABLE `transys`.`transysOrder` 
ADD INDEX `orderVehicleRef_idx` (`vehicleId` ASC)  COMMENT '';

ALTER TABLE `transys`.`transysOrder` 
ADD CONSTRAINT `orderVehicleRef`
  FOREIGN KEY (`vehicleId`)
  REFERENCES `transys`.`vehicle` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
 ---------
 
ALTER TABLE `transys`.`transysOrder` 
DROP FOREIGN KEY `orderVehicleRef`;
ALTER TABLE `transys`.`transysOrder` 
CHANGE COLUMN `vehicleId` `dropOffVehicleId` BIGINT(20) NULL DEFAULT NULL COMMENT '' ;
ALTER TABLE `transys`.`transysOrder` 
ADD CONSTRAINT `orderVehicleRef`
  FOREIGN KEY (`dropOffVehicleId`)
  REFERENCES `transys`.`vehicle` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  
ALTER TABLE `transys`.`transysOrder` 
DROP FOREIGN KEY `orderVehicleRef`;
ALTER TABLE `transys`.`transysOrder` 
DROP INDEX `orderVehicleRef_idx` ,
ADD INDEX `orderDropOffVehicleRef_idx` (`dropOffVehicleId` ASC)  COMMENT '';
ALTER TABLE `transys`.`transysOrder` 
ADD CONSTRAINT `orderDropOffVehicleRef`
  FOREIGN KEY (`dropOffVehicleId`)
  REFERENCES `transys`.`vehicle` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;  
  
  