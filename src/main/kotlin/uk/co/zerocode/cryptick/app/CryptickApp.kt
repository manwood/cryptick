package uk.co.zerocode.cryptick.app

import info.bitrich.xchangestream.core.StreamingExchangeFactory
import info.bitrich.xchangestream.poloniex.PoloniexStreamingExchange
import org.knowm.xchange.currency.CurrencyPair
import java.util.concurrent.Executors

fun main(args: Array<String>) {
    val runnable = Runnable {
        println("Connecting..")
        val exchange = StreamingExchangeFactory.INSTANCE.createExchange(PoloniexStreamingExchange::class.java.name)
        exchange.connect().blockingAwait()
        println("Connected.")
        exchange.streamingMarketDataService
                .getTicker(CurrencyPair.ETH_BTC)
                .subscribe(
                        { trade -> println("$trade") },
                        { throwable -> println("Error in ticker subscription: $throwable") }
                )
        println("Subscribed.. awaiting ticks.")
    }

    val executor = Executors.newCachedThreadPool()
    executor.submit(runnable)
    Thread.sleep(600000)
}
