package services

import java.io.FileNotFoundException

import main.services.FileReader
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.time.{Seconds, Span}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Matchers, WordSpec}

class FileReaderSpec extends WordSpec with Matchers with ScalaFutures
  with IntegrationPatience with BeforeAndAfterEach with BeforeAndAfterAll {

  val TIME_OUT = 5
  val INTERVAL = 0.1
  implicit val defaultPatience: PatienceConfig = PatienceConfig(timeout = Span(TIME_OUT, Seconds), interval = Span(INTERVAL, Seconds))

  val fileReader = new FileReader()

  "getCsvFile" should {
    "parse a CSV file" in {
      //there's an extra blank line in the CSV, wasn't sure if it was part of the task so I left it in, hence the blank data at the end
      whenReady(fileReader.getCsvFile("test/resources/calls.log")) { res =>
        res shouldBe Vector(Map("callerId" -> "A", "phoneNumber" -> "555-333-212", "duration" -> "00:02:03"), Map("callerId" -> "A", "phoneNumber" -> "555-433-242", "duration" -> "00:06:41"), Map("callerId" -> "A", "phoneNumber" -> "555-433-242", "duration" -> "00:01:03"), Map("callerId" -> "B", "phoneNumber" -> "555-333-212", "duration" -> "00:01:20"), Map("callerId" -> "A", "phoneNumber" -> "555-333-212", "duration" -> "00:01:10"), Map("callerId" -> "A", "phoneNumber" -> "555-663-111", "duration" -> "00:02:09"), Map("callerId" -> "A", "phoneNumber" -> "555-333-212", "duration" -> "00:04:28"), Map("callerId" -> "B", "phoneNumber" -> "555-334-789", "duration" -> "00:00:03"), Map("callerId" -> "A", "phoneNumber" -> "555-663-111", "duration" -> "00:02:03"), Map("callerId" -> "B", "phoneNumber" -> "555-334-789", "duration" -> "00:00:53"), Map("callerId" -> "B", "phoneNumber" -> "555-971-219", "duration" -> "00:09:51"), Map("callerId" -> "B", "phoneNumber" -> "555-333-212", "duration" -> "00:02:03"), Map("callerId" -> "B", "phoneNumber" -> "555-333-212", "duration" -> "00:04:31"), Map("callerId" -> "B", "phoneNumber" -> "555-334-789", "duration" -> "00:01:59"),
          Map("callerId" -> ""))
      }
    }

    "parse a big CSV file" in {
      whenReady(fileReader.getCsvFile("test/resources/callsBig.log")) { res =>
        res.length shouldBe 10400
      }
    }
  }

  "getFileAsByteString" should {
    "read a CSV file from path" in {

      fileReader.getFileAsByteString("test/resources/calls.log").utf8String shouldBe
      """A 555-333-212 00:02:03
        |A 555-433-242 00:06:41
        |A 555-433-242 00:01:03
        |B 555-333-212 00:01:20
        |A 555-333-212 00:01:10
        |A 555-663-111 00:02:09
        |A 555-333-212 00:04:28
        |B 555-334-789 00:00:03
        |A 555-663-111 00:02:03
        |B 555-334-789 00:00:53
        |B 555-971-219 00:09:51
        |B 555-333-212 00:02:03
        |B 555-333-212 00:04:31
        |B 555-334-789 00:01:59
        |
        |""".stripMargin
    }

    "throw an exception if a file can't be found" in {

      assertThrows[FileNotFoundException] { // Result type: Assertion
        fileReader.getFileAsByteString("test/resources/???.log")
      }

    }
  }

}
