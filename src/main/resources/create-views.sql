DROP TABLE IF EXISTS `map`;
CREATE OR REPLACE VIEW `map` AS
	SELECT 
		`contact`.`id` AS `id`,
	    `contact`.`timestamp` AS `timestamp`,
	    `contact`.`callsign` AS `callsign`,
	    `contact`.`band` AS `band`,
	    `contact`.`logbook_id` AS `logbook_id`,
	    concat(`callbook_entry`.`fname`,' ',`callbook_entry`.`name`) AS `name`,
	    COALESCE(
			(SELECT
				`location` AS `grid`
			FROM `contact` `subquery_contact`
	        WHERE
				`subquery_contact`.`id` = `contact`.`id`
	            AND
	            `location` REGEXP "[a-zA-Z]{2}[0-9]{2}[a-zA-Z]{2}"
			),
	        `callbook_entry`.`grid`
		) AS `grid`,
	    `callbook_entry`.`lat` AS `lat`,
	    `callbook_entry`.`lon` AS `lon`
	FROM
		(`contact` LEFT JOIN `callbook_entry` ON
			((`contact`.`callsign` = `callbook_entry`.`callsign`))
		)
	WHERE
		(
			(`callbook_entry`.`lat` IS NOT null)
	        AND
			(`callbook_entry`.`lon` IS NOT null)
	);