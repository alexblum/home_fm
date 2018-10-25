-- audio_file table
CREATE TABLE `audio_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `filename` varchar(512) NOT NULL,
  `artist` varchar(128) DEFAULT NULL,
  `title` varchar(128) DEFAULT NULL,
  `album` varchar(128) DEFAULT NULL,
  `duration_in_seconds` bigint(20) NOT NULL DEFAULT '0',
  `last_change` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `filename` (`filename`(255))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- playlist table
CREATE TABLE `playlist` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `last_change` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- playlist_x_audio_file table
CREATE TABLE `playlist_x_audio_file` (
  `playlist_id` int(11) NOT NULL,
  `audio_file_id` int(11) NOT NULL,
  PRIMARY KEY (`playlist_id`,`audio_file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
