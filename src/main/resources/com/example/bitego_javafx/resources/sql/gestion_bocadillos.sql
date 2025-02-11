/*
SQLyog Community v12.09 (64 bit)
MySQL - 10.4.19-MariaDB : Database - gestion_bocadillos
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`gestion_bocadillos` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `gestion_bocadillos`;

/*Table structure for table `alergeno` */

DROP TABLE IF EXISTS `alergeno`;

CREATE TABLE `alergeno` (
  `nombre` varchar(50) NOT NULL,
  `descripcion` text NOT NULL,
  `f_baja` datetime(6) DEFAULT NULL,
  `icono` text NOT NULL,
  PRIMARY KEY (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `alergeno` */

insert  into `alergeno`(`nombre`,`descripcion`,`f_baja`,`icono`) values ('Apio','Presente en apio y productos derivados.','9999-12-31 00:00:00.000000','icon_apio.png'),('Cacahuetes','Presente en cacahuetes y productos derivados.','9999-12-31 00:00:00.000000','icon_cacahuetes.png'),('Frutos Secos','Presente en nueces, almendras, avellanas y otros frutos secos.','9999-12-31 00:00:00.000000','icon_frutos_secos.png'),('Gluten','Presente en productos que contienen trigo, centeno, cebada o avena.','9999-12-31 00:00:00.000000','icon_gluten.png'),('Huevo','Presente en huevos y productos derivados.','9999-12-31 00:00:00.000000','icon_huevo.png'),('Lactosa','Azúcar presente en la leche y sus derivados.','9999-12-31 00:00:00.000000','icon_lactosa.png'),('Mariscos','Presente en crustáceos y productos derivados.','9999-12-31 00:00:00.000000','icon_mariscos.png'),('Mostaza','Presente en semillas de mostaza y productos derivados.','9999-12-31 00:00:00.000000','icon_mostaza.png'),('Pescado','Presente en pescado y productos derivados.','9999-12-31 00:00:00.000000','icon_pescado.png'),('Sésamo','Presente en semillas de sésamo y productos derivados.','9999-12-31 00:00:00.000000','icon_sesamo.png'),('Soja','Presente en productos derivados de la soja.','9999-12-31 00:00:00.000000','icon_soja.png'),('Sulfitos','Presente en productos que contienen sulfitos, como algunos vinos y alimentos procesados.','9999-12-31 00:00:00.000000','icon_sulfitos.png');

/*Table structure for table `alergeno_bocadillo` */

DROP TABLE IF EXISTS `alergeno_bocadillo`;

CREATE TABLE `alergeno_bocadillo` (
  `id_bocadillo` varchar(255) NOT NULL,
  `id_alergeno` varchar(255) NOT NULL,
  PRIMARY KEY (`id_bocadillo`,`id_alergeno`),
  KEY `FKl5ioukdy9hwunwl2cr5mwohys` (`id_alergeno`),
  CONSTRAINT `FKl5ioukdy9hwunwl2cr5mwohys` FOREIGN KEY (`id_alergeno`) REFERENCES `alergeno` (`nombre`),
  CONSTRAINT `FKmb87rl11bkd146qfw3h9wo8pe` FOREIGN KEY (`id_bocadillo`) REFERENCES `bocadillo` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `alergeno_bocadillo` */

insert  into `alergeno_bocadillo`(`id_bocadillo`,`id_alergeno`) values ('Bocadillo de Jamon y Queso','Gluten'),('Bocadillo de Lomo','Gluten'),('Bocadillo de Lomo','Lactosa');

/*Table structure for table `alumno` */

DROP TABLE IF EXISTS `alumno`;

CREATE TABLE `alumno` (
  `id_alumno` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `apellidos` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `id_curso` int(11) DEFAULT NULL,
  `fecha_baja` date DEFAULT NULL,
  `motivo_baja` text DEFAULT NULL,
  `abonado` tinyint(1) NOT NULL DEFAULT 0,
  `dni` varchar(255) NOT NULL,
  `localidad` varchar(255) NOT NULL,
  PRIMARY KEY (`id_alumno`),
  UNIQUE KEY `email` (`email`),
  KEY `id_curso` (`id_curso`),
  CONSTRAINT `alumno_ibfk_1` FOREIGN KEY (`id_curso`) REFERENCES `curso` (`id_curso`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

/*Data for the table `alumno` */

insert  into `alumno`(`id_alumno`,`nombre`,`apellidos`,`email`,`id_curso`,`fecha_baja`,`motivo_baja`,`abonado`,`dni`,`localidad`) values (2,'Pedro ','Galan Aguado','alumno1@example.com',NULL,'2028-02-09','Mucho Texto',1,'48729958L','Orihuela');

/*Table structure for table `bocadillo` */

DROP TABLE IF EXISTS `bocadillo`;

CREATE TABLE `bocadillo` (
  `nombre` varchar(100) NOT NULL,
  `tipo` enum('Frio','Caliente') NOT NULL,
  `descripcion` text NOT NULL,
  `precio_base` float NOT NULL,
  `dia_asociado` int(11) NOT NULL,
  PRIMARY KEY (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `bocadillo` */

insert  into `bocadillo`(`nombre`,`tipo`,`descripcion`,`precio_base`,`dia_asociado`) values ('Bocadillo de Atun','Frio','Atun con mayonesa, lechuga y tomate en pan de semillas.',5,1),('Bocadillo de Bacon y Queso','Caliente','Bacon crujiente con queso cheddar y salsa barbacoa.',5.5,1),('Bocadillo De Carrillera DELUXE','Caliente','Carrillera de Cerdo al Oporto con extracción de patata',14,4),('Bocadillo de Jamon y Queso','Frio','Jamon iberico con queso manchego en pan rustico.',4.5,2),('Bocadillo de Lomo','Caliente','Lomo de cerdo con pimientos asados y alioli en pan de pueblo.',5.6,2),('Bocadillo de Pollo','Caliente','Pollo a la plancha con queso fundido y mayonesa en pan crujiente.',5.2,3),('Bocadillo De SUGUS','Frio','Sugus recien traidos de BEAMUT caramelos SL',8,5),('Bocadillo de Tortilla','Frio','Tortilla española con alioli en pan de chapata.',4.3,3),('Bocadillo PEPITO','Caliente','Lomo Pimiento del padron y mantequilla al fallo',7,5),('Bocadillo Vegetal','Frio','Lechuga, tomate, huevo cocido y mayonesa en pan integral.',3.8,4);

/*Table structure for table `curso` */

DROP TABLE IF EXISTS `curso`;

CREATE TABLE `curso` (
  `id_curso` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_curso` varchar(50) NOT NULL,
  `anyo` year(4) DEFAULT NULL,
  PRIMARY KEY (`id_curso`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `curso` */

/*Table structure for table `descuento` */

DROP TABLE IF EXISTS `descuento`;

CREATE TABLE `descuento` (
  `id_descuento` int(11) NOT NULL AUTO_INCREMENT,
  `valor_descuento` decimal(5,2) NOT NULL,
  `id_alumno` int(11) DEFAULT NULL,
  `fecha_desde` date NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `fecha_hasta` date NOT NULL,
  PRIMARY KEY (`id_descuento`),
  KEY `id_alumno` (`id_alumno`),
  CONSTRAINT `descuento_ibfk_1` FOREIGN KEY (`id_alumno`) REFERENCES `alumno` (`id_alumno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `descuento` */

/*Table structure for table `pedido_bocadillo` */

DROP TABLE IF EXISTS `pedido_bocadillo`;

CREATE TABLE `pedido_bocadillo` (
  `id_pedido` int(11) NOT NULL AUTO_INCREMENT,
  `id_alumno` int(11) NOT NULL,
  `id_bocadillo` varchar(100) NOT NULL,
  `fecha_hora` datetime NOT NULL,
  `retirado` tinyint(1) NOT NULL,
  `costo_final` float NOT NULL,
  `id_descuento` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_pedido`),
  UNIQUE KEY `id_alumno` (`id_alumno`,`id_bocadillo`,`fecha_hora`),
  UNIQUE KEY `id_alumno_2` (`id_alumno`,`fecha_hora`),
  KEY `id_bocadillo` (`id_bocadillo`),
  KEY `fk_descuento` (`id_descuento`),
  CONSTRAINT `fk_descuento` FOREIGN KEY (`id_descuento`) REFERENCES `descuento` (`id_descuento`),
  CONSTRAINT `pedido_bocadillo_ibfk_1` FOREIGN KEY (`id_alumno`) REFERENCES `alumno` (`id_alumno`),
  CONSTRAINT `pedido_bocadillo_ibfk_2` FOREIGN KEY (`id_bocadillo`) REFERENCES `bocadillo` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `pedido_bocadillo` */

/*Table structure for table `usuario` */

DROP TABLE IF EXISTS `usuario`;

CREATE TABLE `usuario` (
  `mac` varchar(17) NOT NULL,
  `id_usuario` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `contrasenya` varchar(255) NOT NULL,
  `rol` enum('Alumno','Cocina','Administrador') NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `mac` (`mac`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4;

/*Data for the table `usuario` */

insert  into `usuario`(`mac`,`id_usuario`,`email`,`contrasenya`,`rol`) values ('00:1A:2B:3C:4D:5E',1,'alumno1@example.com','Pava','Alumno'),('01:2B:3C:4D:5E:6F',2,'alumno2@example.com','Pava','Alumno'),('02:3C:4D:5E:6F:7G',3,'alumno3@example.com','HALAL','Alumno'),('03:4D:5E:6F:7G:8H',4,'alumno4@example.com','Pava','Alumno'),('04:5E:6F:7G:8H:9I',5,'alumno5@example.com','Pava','Alumno'),('AC:DE:48:88:98:1A',6,'carlos.martinez@example.com','Pava','Alumno'),('BC:AE:45:67:8B:2C',7,'maria.gomez@example.com','Pava','Alumno'),('FA:BC:56:98:78:3D',8,'juan.lopez@example.com','Pava','Alumno'),('DA:DE:47:11:AC:4E',9,'ana.sanchez@example.com','Pava','Alumno'),('AC:BC:44:99:77:5F',10,'jose.perez@example.com','Pava','Alumno'),('CC:DE:42:76:AC:6A',11,'laura.ramos@example.com','Pava','Alumno'),('BD:CE:43:65:DF:7B',12,'miguel.diaz@example.com','Pava','Alumno'),('AE:BC:22:45:EF:8C',13,'carmen.rodriguez@example.com','Pava','Alumno'),('CA:BE:12:34:FA:9D',14,'luis.garcia@example.com','Pava','Alumno'),('AB:AD:23:54:CB:1E',15,'beatriz.molina@example.com','Pava','Alumno'),('FB:BC:33:87:DC:2F',16,'fernando.castillo@example.com','Pava','Alumno'),('CB:DA:13:78:FD:3A',17,'isabel.torres@example.com','Pava','Alumno'),('AA:FE:44:12:EB:4B',18,'rafael.martin@example.com','Pava','Alumno');

/* Procedure structure for procedure `usuarios_pares` */

/*!50003 DROP PROCEDURE IF EXISTS  `usuarios_pares` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `usuarios_pares`()
BEGIN
	DECLARE id_usuariolocal INT(11);
	DECLARE fin BOOL DEFAULT FALSE;
	
	-- Declaración del cursor
	DECLARE cursor_pares CURSOR FOR 
	SELECT id_usuario
	FROM usuario
	ORDER BY id_usuario;
	
	-- Manejador para el fin del cursor
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET fin = TRUE;
	
	-- Creación de la tabla temporal
	CREATE TEMPORARY TABLE usuarios_pares (
		mac VARCHAR(17) NOT NULL,
		id_usuario INT(11) NOT NULL,
		email VARCHAR(100) NOT NULL,
		contrasenya VARCHAR(255) NOT NULL,
		rol ENUM('Alumno', 'Cocina', 'Administrador') NULL,
		PRIMARY KEY(id_usuario)
	);
	
	-- Apertura del cursor
	OPEN cursor_pares;
	-- Bucle para procesar cada fila del cursor
	loop_pares: LOOP
		FETCH cursor_pares INTO id_usuariolocal;
		IF fin THEN
			LEAVE loop_pares;
		END IF;
		-- Insertar sólo si el id es par
		IF MOD(id_usuariolocal, 2) = 0 THEN
			INSERT INTO usuarios_pares (mac, id_usuario, email, contrasenya, rol)
			SELECT mac, id_usuario, email, contrasenya, rol
			FROM usuario 
			WHERE id_usuario = id_usuariolocal;
		END IF;
	END LOOP;
	-- Cierre del cursor
	CLOSE cursor_pares;
	-- Mostrar resultados
	SELECT * FROM usuarios_pares;
	-- Eliminar la tabla temporal
	DROP TEMPORARY TABLE usuarios_pares;
	
END */$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
