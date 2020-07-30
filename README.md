# Notes
`sbt run` to run application, `CTRL + C` to stop

`sbt test` to run tests

# Telephone Company

Each day a batch job collates customer calls for the previous day into a single log file of:

`'customerId','phoneNumber called','callDuration'`

The cost of a call up to and including 3 minutes is charged at 0.05p/sec, any call over 3 minutes is charged at 0.03p/sec. However, there is a promotion on and the calls made to the phone number with the greatest total cost is removed from the customer's bill.

## Task

Write a program that when run will parse the `calls.log` file and print out the total cost of calls for the day for each customer.

