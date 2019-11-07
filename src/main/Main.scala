package main

import com.google.inject.{Guice, Injector}
import main.module.GuiceModule
import main.services.PhoneCostService
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {

  lazy val injector: Injector = Guice.createInjector(new GuiceModule)

  lazy val phoneCallPrinter: PhoneCostService = injector.getInstance(classOf[PhoneCostService])

  phoneCallPrinter.calculateCallCosts.map { totalCostOfCallsPerCustomer =>
    totalCostOfCallsPerCustomer.foreach(customer => println(s"Customer ID: ${customer.id}, Total cost: £${customer.totalPriceOfCalls}"))
  }

}
