package utils

import main.app.AppConfig
import main.utils.PhoneCallCalculator
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.time.{Seconds, Span}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Matchers, WordSpec}

class PhoneCallCalculatorSpec extends WordSpec with Matchers with ScalaFutures
  with IntegrationPatience with BeforeAndAfterEach with BeforeAndAfterAll {

  val TIME_OUT = 5
  val INTERVAL = 0.1
  implicit val defaultPatience: PatienceConfig = PatienceConfig(timeout = Span(TIME_OUT, Seconds), interval = Span(INTERVAL, Seconds))
  val appConfig = new AppConfig
  val phoneCallCalculator = new PhoneCallCalculator {}

  "calculateIndividualCallCost" should {
    "correctly calculate a call cost under 3 mins (standard rate)" in {

      phoneCallCalculator.calculateIndividualCallCost("00:01:00",
        appConfig.additionalTime,appConfig.standardRate,appConfig.additionalRate) shouldBe 3
    }

    "correctly calculate a call cost at exactly 3 mins (standard rate)" in {

      phoneCallCalculator.calculateIndividualCallCost("00:03:00",
        appConfig.additionalTime,appConfig.standardRate,appConfig.additionalRate) shouldBe 9
    }

    "correctly calculate a call cost over 3 mins (additional rate)" in {

      phoneCallCalculator.calculateIndividualCallCost("00:03:01",
        appConfig.additionalTime,appConfig.standardRate,appConfig.additionalRate) shouldBe 9.03
    }
  }

}
