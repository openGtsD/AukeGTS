CREATE DATABASE `gts_test` /*!40100 DEFAULT CHARACTER SET utf8 */;

CREATE TABLE `Device` (
  `accountID` varchar(32) NOT NULL,
  `deviceID` varchar(32) NOT NULL,
  `groupID` varchar(32) DEFAULT NULL,
  `equipmentType` varchar(40) DEFAULT NULL,
  `equipmentStatus` varchar(24) DEFAULT NULL,
  `vehicleMake` varchar(40) DEFAULT NULL,
  `vehicleModel` varchar(40) DEFAULT NULL,
  `vehicleID` varchar(24) DEFAULT NULL,
  `licensePlate` varchar(24) DEFAULT NULL,
  `licenseExpire` int(10) unsigned DEFAULT NULL,
  `insuranceExpire` int(10) unsigned DEFAULT NULL,
  `driverID` varchar(32) DEFAULT NULL,
  `driverStatus` int(10) unsigned DEFAULT NULL,
  `fuelCapacity` double DEFAULT NULL,
  `fuelEconomy` double DEFAULT NULL,
  `fuelRatePerHour` double DEFAULT NULL,
  `fuelCostPerLiter` double DEFAULT NULL,
  `fuelTankProfile` varchar(24) DEFAULT NULL,
  `speedLimitKPH` double DEFAULT NULL,
  `planDistanceKM` double DEFAULT NULL,
  `installTime` int(10) unsigned DEFAULT NULL,
  `resetTime` int(10) unsigned DEFAULT NULL,
  `expirationTime` int(10) unsigned DEFAULT NULL,
  `uniqueID` varchar(40) DEFAULT NULL,
  `deviceCode` varchar(24) DEFAULT NULL,
  `deviceType` varchar(24) DEFAULT NULL,
  `pushpinID` varchar(32) DEFAULT NULL,
  `displayColor` varchar(16) DEFAULT NULL,
  `serialNumber` varchar(24) DEFAULT NULL,
  `simPhoneNumber` varchar(24) DEFAULT NULL,
  `simID` varchar(24) DEFAULT NULL,
  `smsEmail` varchar(64) DEFAULT NULL,
  `imeiNumber` varchar(24) DEFAULT NULL,
  `dataKey` text,
  `ignitionIndex` smallint(6) DEFAULT NULL,
  `codeVersion` varchar(32) DEFAULT NULL,
  `featureSet` varchar(64) DEFAULT NULL,
  `ipAddressValid` varchar(128) DEFAULT NULL,
  `lastTotalConnectTime` int(10) unsigned DEFAULT NULL,
  `lastDuplexConnectTime` int(10) unsigned DEFAULT NULL,
  `pendingPingCommand` text,
  `lastPingTime` int(10) unsigned DEFAULT NULL,
  `totalPingCount` smallint(5) unsigned DEFAULT NULL,
  `maxPingCount` smallint(5) unsigned DEFAULT NULL,
  `commandStateMask` int(10) unsigned DEFAULT NULL,
  `expectAck` tinyint(4) DEFAULT NULL,
  `expectAckCode` int(10) unsigned DEFAULT NULL,
  `lastAckCommand` text,
  `lastAckTime` int(10) unsigned DEFAULT NULL,
  `dcsPropertiesID` varchar(32) DEFAULT NULL,
  `dcsConfigMask` int(10) unsigned DEFAULT NULL,
  `dcsConfigString` varchar(80) DEFAULT NULL,
  `dcsCommandHost` varchar(32) DEFAULT NULL,
  `supportsDMTP` tinyint(4) DEFAULT NULL,
  `supportedEncodings` tinyint(3) unsigned DEFAULT NULL,
  `unitLimitInterval` smallint(5) unsigned DEFAULT NULL,
  `maxAllowedEvents` smallint(5) unsigned DEFAULT NULL,
  `totalProfileMask` blob,
  `totalMaxConn` smallint(5) unsigned DEFAULT NULL,
  `totalMaxConnPerMin` smallint(5) unsigned DEFAULT NULL,
  `duplexProfileMask` blob,
  `duplexMaxConn` smallint(5) unsigned DEFAULT NULL,
  `duplexMaxConnPerMin` smallint(5) unsigned DEFAULT NULL,
  `lastTcpSessionID` varchar(32) DEFAULT NULL,
  `ipAddressCurrent` varchar(32) DEFAULT NULL,
  `remotePortCurrent` smallint(5) unsigned DEFAULT NULL,
  `listenPortCurrent` smallint(5) unsigned DEFAULT NULL,
  `lastInputState` int(10) unsigned DEFAULT NULL,
  `lastOutputState` int(10) unsigned DEFAULT NULL,
  `statusCodeState` int(10) unsigned DEFAULT NULL,
  `lastBatteryLevel` double DEFAULT NULL,
  `lastFuelLevel` double DEFAULT NULL,
  `lastFuelTotal` double DEFAULT NULL,
  `lastOilLevel` double DEFAULT NULL,
  `lastValidLatitude` double DEFAULT NULL,
  `lastValidLongitude` double DEFAULT NULL,
  `lastValidHeading` double DEFAULT NULL,
  `lastValidSpeedKPH` double DEFAULT NULL,
  `lastGPSTimestamp` int(10) unsigned DEFAULT NULL,
  `lastEventTimestamp` int(10) unsigned DEFAULT NULL,
  `lastCellServingInfo` varchar(100) DEFAULT NULL,
  `lastDistanceKM` double DEFAULT NULL,
  `lastOdometerKM` double DEFAULT NULL,
  `odometerOffsetKM` double DEFAULT NULL,
  `lastEngineOnHours` double DEFAULT NULL,
  `lastEngineOnTime` int(10) unsigned DEFAULT NULL,
  `lastEngineOffTime` int(10) unsigned DEFAULT NULL,
  `lastEngineHours` double DEFAULT NULL,
  `engineHoursOffset` double DEFAULT NULL,
  `lastIgnitionOnHours` double DEFAULT NULL,
  `lastIgnitionOnTime` int(10) unsigned DEFAULT NULL,
  `lastIgnitionOffTime` int(10) unsigned DEFAULT NULL,
  `lastIgnitionHours` double DEFAULT NULL,
  `lastStopTime` int(10) unsigned DEFAULT NULL,
  `lastStartTime` int(10) unsigned DEFAULT NULL,
  `lastMalfunctionLamp` tinyint(4) DEFAULT NULL,
  `lastFaultCode` varchar(96) DEFAULT NULL,
  `isActive` tinyint(4) DEFAULT NULL,
  `displayName` varchar(40) DEFAULT NULL,
  `description` varchar(128) DEFAULT NULL,
  `notes` text,
  `lastUpdateTime` int(10) unsigned DEFAULT NULL,
  `creationTime` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`accountID`,`deviceID`),
  KEY `altIndex` (`uniqueID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `EventData` (
  `accountID` varchar(32) NOT NULL,
  `deviceID` varchar(32) NOT NULL,
  `timestamp` int(10) unsigned NOT NULL,
  `statusCode` int(10) unsigned NOT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `gpsAge` int(10) unsigned DEFAULT NULL,
  `speedKPH` double DEFAULT NULL,
  `heading` double DEFAULT NULL,
  `altitude` double DEFAULT NULL,
  `transportID` varchar(32) DEFAULT NULL,
  `inputMask` int(10) unsigned DEFAULT NULL,
  `outputMask` int(10) unsigned DEFAULT NULL,
  `seatbeltMask` int(10) unsigned DEFAULT NULL,
  `address` varchar(90) DEFAULT NULL,
  `dataSource` varchar(32) DEFAULT NULL,
  `rawData` text,
  `distanceKM` double DEFAULT NULL,
  `odometerKM` double DEFAULT NULL,
  `odometerOffsetKM` double DEFAULT NULL,
  `geozoneIndex` int(10) unsigned DEFAULT NULL,
  `geozoneID` varchar(32) DEFAULT NULL,
  `creationTime` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`accountID`,`deviceID`,`timestamp`,`statusCode`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `MapPoint` (
  `id` varchar(100) NOT NULL,
  `trackerId` varchar(32) NOT NULL,
  `time` int(10) unsigned NOT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `altitude` double DEFAULT NULL,
  `speed` double DEFAULT NULL,
  `course` double DEFAULT NULL,
  `creationTime` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `User` (
  `id` varchar(100) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,

  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `person` (
  `id` varchar(100) NOT NULL,
  `phone` varchar(100) ,
  `email` varchar(100) ,
  `IM` varchar(100) ,

  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
