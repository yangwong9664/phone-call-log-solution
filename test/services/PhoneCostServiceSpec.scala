package services

import akka.util.ByteString
import main.app.AppConfig
import main.services.{FileReader, PhoneCostService}
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.time.{Seconds, Span}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Matchers, WordSpec}

class PhoneCostServiceSpec extends WordSpec with Matchers with ScalaFutures
  with IntegrationPatience with BeforeAndAfterEach with BeforeAndAfterAll {

  val TIME_OUT = 5
  val INTERVAL = 0.1
  implicit val defaultPatience: PatienceConfig = PatienceConfig(timeout = Span(TIME_OUT, Seconds), interval = Span(INTERVAL, Seconds))

  val appConfig = new AppConfig

  "calculateCallCosts" should {
    "calculate the total costs of calls for 2 customers" in {
      val fileReader = new FileReader {
        override def getFileAsByteString(filePath: String): ByteString = super.getFileAsByteString("test/resources/calls.log")
      }
      val phoneCostService = new PhoneCostService(appConfig,fileReader)

      whenReady(phoneCostService.calculateCallCosts) { res =>
        res.length shouldBe 2

        res.head.totalPriceOfCalls shouldBe 37.04
        res.last.totalPriceOfCalls shouldBe 30.63
      }
    }

    "identify 4 customers given a very large data set and 4 unique ID's" in {
      val fileReader = new FileReader {
        override def getFileAsByteString(filePath: String): ByteString = super.getFileAsByteString("test/resources/callsBig.log")
      }
      val phoneCostService = new PhoneCostService(appConfig,fileReader)

      whenReady(phoneCostService.calculateCallCosts) { res =>
        res.length shouldBe 4

        res.head.id shouldBe "A"

        res(1).id shouldBe "B"

        res(2).id shouldBe "C"

        res(3).id shouldBe "D"
      }
    }
  }


}
