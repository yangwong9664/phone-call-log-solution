package main.services

import java.nio.charset.StandardCharsets
import javax.inject.Singleton

import akka.stream.alpakka.csv.scaladsl.{CsvParsing, CsvToMap}
import akka.stream.scaladsl.{Sink, Source}
import akka.util.ByteString
import main.app.AppSystem
import main.utils.Constants

import scala.util.{Failure, Success, Try}

@Singleton
class FileReader extends AppSystem with Constants {

  def getCsvFile(filePath: String) = {
      Source.single(getFileAsByteString(filePath))
        .via(CsvParsing.lineScanner(delimiter = ' '))
        .via(CsvToMap.withHeadersAsStrings(StandardCharsets.UTF_8, CALLER_ID, PHONE_NUMBER, DURATION))
        .runWith(Sink.seq)
  }

  def getFileAsByteString(filePath: String) = {
    import scala.io.Source
    Try {
      ByteString(Source.fromFile(filePath).mkString)
    } match {
      case Success(s) => s
      case Failure(f) =>
        logger.error(s"[FileReader][getFileAsByteString] Error when reading $filePath: ${f.getMessage}")
        throw f
    }
  }

}
