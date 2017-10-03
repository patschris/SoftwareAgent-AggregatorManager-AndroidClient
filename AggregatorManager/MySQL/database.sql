SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `database` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
USE `database` ;

-- -----------------------------------------------------
-- Table `database`.`Users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `database`.`Users` ;

CREATE  TABLE IF NOT EXISTS `database`.`Users` (
  `Username` VARCHAR(20) NOT NULL ,
  `Password` VARCHAR(64) NOT NULL ,
  `Active` TINYINT(1) NOT NULL DEFAULT false ,
  PRIMARY KEY (`Username`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `database`.`Agents`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `database`.`Agents` ;

CREATE  TABLE IF NOT EXISTS `database`.`Agents` (
  `HashKey` BIGINT NOT NULL ,
  `DeviceName` VARCHAR(45) NOT NULL ,
  `IpAddress` VARCHAR(20) NOT NULL ,
  `MacAddress` VARCHAR(20) NOT NULL ,
  `OsVersion` VARCHAR(45) NOT NULL ,
  `NmapVersion` VARCHAR(20) NOT NULL ,
  `Online` TINYINT(1) NOT NULL ,
  PRIMARY KEY (`HashKey`) )
ENGINE = InnoDB
PACK_KEYS = DEFAULT;


-- -----------------------------------------------------
-- Table `database`.`NmapJobs`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `database`.`NmapJobs` ;

CREATE  TABLE IF NOT EXISTS `database`.`NmapJobs` (
  `Id` INT NOT NULL AUTO_INCREMENT ,
  `Command` VARCHAR(60) NOT NULL ,
  `PeriodicFlag` TINYINT(1) NOT NULL ,
  `Time` INT UNSIGNED NOT NULL ,
  `HashKey` BIGINT NOT NULL ,
  `Sent` TINYINT(1) NOT NULL ,
  PRIMARY KEY (`Id`) ,
  INDEX `fk_NmapJobs_Agents1` (`HashKey` ASC) ,
  CONSTRAINT `fk_NmapJobs_Agents1`
    FOREIGN KEY (`HashKey` )
    REFERENCES `database`.`Agents` (`HashKey` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `database`.`Results`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `database`.`Results` ;

CREATE  TABLE IF NOT EXISTS `database`.`Results` (
  `ResultId` INT NOT NULL AUTO_INCREMENT ,
  `Output` MEDIUMTEXT NOT NULL ,
  `Timestamp` VARCHAR(60) NOT NULL ,
  `HashKey` BIGINT NOT NULL ,
  `JobId` INT NOT NULL ,
  INDEX `fk_Results_Agents1` (`HashKey` ASC) ,
  PRIMARY KEY (`ResultId`) ,
  INDEX `fk_Results_NmapJobs1` (`JobId` ASC) ,
  CONSTRAINT `fk_Results_Agents1`
    FOREIGN KEY (`HashKey` )
    REFERENCES `database`.`Agents` (`HashKey` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Results_NmapJobs1`
    FOREIGN KEY (`JobId` )
    REFERENCES `database`.`NmapJobs` (`Id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `database`.`AndroidClients`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `database`.`AndroidClients` ;

CREATE  TABLE IF NOT EXISTS `database`.`AndroidClients` (
  `Username` VARCHAR(45) NOT NULL ,
  `Password` VARCHAR(64) NOT NULL ,
  `Active` TINYINT(1) NOT NULL DEFAULT false ,
  PRIMARY KEY (`Username`) )
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `database`.`Users`
-- -----------------------------------------------------
START TRANSACTION;
USE `database`;
INSERT INTO `database`.`Users` (`Username`, `Password`, `Active`) VALUES ('christos', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 0);
INSERT INTO `database`.`Users` (`Username`, `Password`, `Active`) VALUES ('ilias', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 0);
INSERT INTO `database`.`Users` (`Username`, `Password`, `Active`) VALUES ('guest', '84983c60f7daadc1cb8698621f802c0d9f9a3c3c295c810748fb048115c186ec', 0);

COMMIT;
