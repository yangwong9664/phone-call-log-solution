package main.app

import com.google.inject.Singleton
import com.typesafe.config.{Config, ConfigFactory}

@Singleton
class AppConfig {
  lazy val config: Config = ConfigFactory.load()
  //these variables are in the application.conf so that they can be easily overridden in an environment or test suite

  lazy val callLogsPath = config.getString("callLogsPath")

  lazy val standardRate = config.getDouble("rates.standard")
  lazy val additionalRate = config.getDouble("rates.additional")
  lazy val additionalTime = config.getInt("rates.additionalTime")
  lazy val promoOn = config.getBoolean("rates.promoOn")
}
