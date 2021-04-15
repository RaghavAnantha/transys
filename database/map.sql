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