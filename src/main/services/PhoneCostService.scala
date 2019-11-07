package main.services

import com.google.inject.Inject
import main.app.{AppConfig, AppSystem}
import main.models.{CallLog, CustomerCallCostData}
import main.utils.{Constants, PhoneCallCalculator}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.math.BigDecimal.RoundingMode



class PhoneCostService @Inject()(appConfig: AppConfig, fileReader: FileReader) extends PhoneCallCalculator with Constants with AppSystem {

  def calculateCallCosts: Future[Seq[CustomerCallCostData]] = {
    //get call logs file and convert to models
    getCustomerCallLogs.map { callLogs =>
      callLogs.map { customerData =>
        //apply promo if enabled
        val editBill = if (appConfig.promoOn) {
          promoRemoveHighestCostCall(customerData._2)
        } else customerData._2
        //return seq of customer ID with their total call cost
        CustomerCallCostData(customerData._1, calculateTotalCallCost(editBill))
      }.toSeq.sortBy(_.id)
    }
  }

  private def getCustomerCallLogs: Future[Map[String, Seq[CallLog]]] = {
    fileReader.getCsvFile(appConfig.callLogsPath).map { csv =>
      //sort calls by customerId, as a customer might have different phone numbers
      convertCsvMapToModels(csv).groupBy(_.callerId)
    }
  }

  private def convertCsvMapToModels(csvMap: Seq[Map[String, String]]): Seq[CallLog] = {
    //filter out any corrupt/incomplete data by ensuring each row contains no missing data
    csvMap.flatMap { csvRow =>
      (csvRow.get(CALLER_ID), csvRow.get(PHONE_NUMBER), csvRow.get(DURATION)) match {
        case (Some(id), Some(number), Some(duration)) =>
          Some(CallLog(callerId = id, phoneNumber = number, callCost =
            calculateIndividualCallCost(duration, appConfig.additionalTime, appConfig.standardRate,
              appConfig.additionalRate).setScale(2,RoundingMode.HALF_EVEN)))
        case _ => logger.warning("[PhoneCostService][convertCsvMapToModels] Found call log with missing data")
          None
      }
    }
  }

}
