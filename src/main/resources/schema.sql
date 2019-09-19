CREATE DATABASE IF NOT EXISTS gb1wsg;
-- Need to replace the map table with a view, however this only works if the DB has already been initialized

/*
DROP TABLE IF EXISTS `map`;
CREATE OR REPLACE VIEW `map` AS
	SELECT
		`contact`.`id` AS `id`,
        `contact`.`timestamp` AS `timestamp`,
		`contact`.`callsign` AS `callsign`,
        CONCAT(`callbook_entry`.`fname`, " ", `callbook_entry`.`name`) AS `name`,
		`callbook_entry`.`lat` AS `lat`,
		`callbook_entry`.`lon` AS `lon`
	FROM (
		`contact` LEFT JOIN `callbook_entry` ON (
			(`contact`.`callsign` = `callbook_entry`.`callsign`)
		)
	)
	WHERE (
		(`callbook_entry`.`lat` IS NOT NULL)
		AND
		(`callbook_entry`.`lon` IS NOT NULL)
	);
*/