

CREATE TABLE IF NOT EXISTS `${db}`.`estados` (
    `id_estado` BIGINT NOT NULL AUTO_INCREMENT,
    `nombre_estado` VARCHAR(50) NOT NULL,
    `descripcion_estado` VARCHAR(255) NULL,
    PRIMARY KEY (`id_estado`)
    ) ENGINE=InnoDB;

INSERT INTO `${db}`.`estados` (`nombre_estado`, `descripcion_estado`)
SELECT * FROM (SELECT 'PENDIENTE', 'Solicitud pendiente de revisión') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM `${db}`.`estados` WHERE `nombre_estado` = 'PENDIENTE');

INSERT INTO `${db}`.`estados` (`nombre_estado`, `descripcion_estado`)
SELECT * FROM (SELECT 'APROBADO', 'Solicitud aprobada') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM `${db}`.`estados` WHERE `nombre_estado` = 'APROBADO');

INSERT INTO `${db}`.`estados` (`nombre_estado`, `descripcion_estado`)
SELECT * FROM (SELECT 'DENEGADO', 'Solicitud denegada') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM `${db}`.`estados` WHERE `nombre_estado` = 'DENEGADO');



CREATE TABLE IF NOT EXISTS `${db}`.`tipo_prestamos` (
    `id_tipo_prestamo` BIGINT NOT NULL AUTO_INCREMENT,
    `nombre` VARCHAR(100) NOT NULL,
    `monto_minimo` DECIMAL(15,2) NOT NULL,
    `monto_maximo` DECIMAL(15,2) NOT NULL,
    `tasa_interes` DECIMAL(5,2) NOT NULL,
    `validacion_automatica` BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (`id_tipo_prestamo`)
    ) ENGINE=InnoDB;


-- Insertar tipos de préstamo por defecto (si no existen)
INSERT INTO `${db}`.`tipo_prestamos` (`nombre`, `monto_minimo`, `monto_maximo`, `tasa_interes`, `validacion_automatica`)
SELECT * FROM (SELECT 'Préstamo de Consumo', 500000, 5000000, 15.00, FALSE) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM `${db}`.`tipo_prestamos` WHERE `nombre` = 'Préstamo de Consumo');

INSERT INTO `${db}`.`tipo_prestamos` (`nombre`, `monto_minimo`, `monto_maximo`, `tasa_interes`, `validacion_automatica`)
SELECT * FROM (SELECT 'Préstamo Hipotecario', 10000000, 500000000, 10.00, FALSE) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM `${db}`.`tipo_prestamos` WHERE `nombre` = 'Préstamo Hipotecario');

INSERT INTO `${db}`.`tipo_prestamos` (`nombre`, `monto_minimo`, `monto_maximo`, `tasa_interes`, `validacion_automatica`)
SELECT * FROM (SELECT 'Préstamo Vehicular', 5000000, 100000000, 12.00, FALSE) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM `${db}`.`tipo_prestamos` WHERE `nombre` = 'Préstamo Vehicular');

-- Crear tabla Solicitud
CREATE TABLE IF NOT EXISTS `${db}`.`solicitudes` (
    `id_solicitud` BIGINT NOT NULL AUTO_INCREMENT,
    `monto` DECIMAL(15,2) NOT NULL,
    `plazo` INT NOT NULL,
    `email` VARCHAR(150) NOT NULL,
    `documento_identidad` VARCHAR(50) NOT NULL,
    `id_estado` BIGINT NOT NULL,
    `id_tipo_prestamo` BIGINT NOT NULL,
    PRIMARY KEY (`id_solicitud`),
    CONSTRAINT `fk_solicitud_estado` FOREIGN KEY (`id_estado`) REFERENCES `${db}`.`estados`(`id_estado`)
    ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT `fk_solicitud_tipo_prestamo` FOREIGN KEY (`id_tipo_prestamo`) REFERENCES `${db}`.`tipo_prestamos`(`id_tipo_prestamo`)
    ON UPDATE CASCADE ON DELETE RESTRICT
    ) ENGINE=InnoDB;

-- Índices útiles
CREATE INDEX `idx_solicitud_estado` ON `${db}`.`solicitudes` (`id_estado`);
CREATE INDEX `idx_solicitud_tipo_prestamo` ON `${db}`.`solicitudes` (`id_tipo_prestamo`);
