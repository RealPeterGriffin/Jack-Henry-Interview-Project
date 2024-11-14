package server

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder

import scala.jdk.CollectionConverters.*

import server.Query.*

object Server {
    val okResponse = 200
    val methodNotAllowed = 405
    val Port = 50000

    // Main method that starts the server, accepts GET requests
    // from connecting clients and then calls firstQuery in Query.scala
    // to query the national weather service API
    //
    // @param args None
    //
    // @return None
    def main(args: Array[String]): Unit = 
        val server = HttpServer.create(new InetSocketAddress(Port), 0)

        server.createContext("/", new HttpHandler {
        override def handle(exchange: HttpExchange): Unit = 
            if exchange.getRequestMethod == "GET" then 
                val query = exchange.getRequestURI.getQuery
                val queryParams = extractParameters(query)
                val latitude = queryParams(0)
                val longitude = queryParams(1)

                val confirmation = s"Received coordinates $latitude and $longitude"
                println(confirmation)

                val response = firstQuery(latitude.toInt, longitude.toInt)
                exchange.sendResponseHeaders(okResponse, response.getBytes.length)
                val responseBody = exchange.getResponseBody
                responseBody.write(response.getBytes)
                responseBody.close()
            else 
                exchange.sendResponseHeaders(methodNotAllowed, -1)
        })

        server.setExecutor(null)
        server.start()
        println("Server is running on port %s".format(Port))

    // Helper function to extract latitude and longitude 
    // from query string. Example usage of splits:
    // 
    // latitude=30&longitude=-97
    // [latitude=30, longitude=-97]
    // [[latitude, 30], [longitude, -97]]
    //
    // @param query String query
    //
    // @return tuple containing extracted latitude and longitude
    def extractParameters(query: String): (String, String) =
        val firstSplit = query.split("&")
        (firstSplit(0).split("=")(1).strip, firstSplit(1).split("=")(1).strip)
}