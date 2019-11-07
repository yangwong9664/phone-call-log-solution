package main.utils

import java.time.LocalTime

import main.models.CallLog

trait PhoneCallCalculator {

  def calculateIndividualCallCost(callLength: String, additionalTime: Int, standardRate: Double, additionalRate: Double): BigDecimal = {
    //take a time duration and convert it to total seconds, then find the cost of the call
    val callSeconds = LocalTime.parse(callLength).toSecondOfDay
    if(callSeconds > additionalTime){
      //charge the normal rate up to 3 mins, then the additional rate over 3 mins
      (additionalTime * standardRate) + ((callSeconds - additionalTime) * additionalRate)
    } else {
      standardRate * callSeconds
    }
  }

  def calculateTotalCallCost(customerCalls: Seq[CallLog]) = {
    customerCalls.map(_.callCost).sum
  }

  def promoRemoveHighestCostCall(customerCalls: Seq[CallLog]): Seq[CallLog] = {
    //sort calls by cost total, drop the highest call
    customerCalls.sortBy(_.callCost).init
  }

}
