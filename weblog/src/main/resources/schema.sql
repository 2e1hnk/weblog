CREATE OR REPLACE VIEW `map` AS
	SELECT
		`contact`.`id` AS `id`,
		`contact`.`callsign` AS `callsign`,
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