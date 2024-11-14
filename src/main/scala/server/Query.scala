package server

import requests.Response

import upickle.default.*

import java.time.*

object Query {
    val current_date = LocalDate.now()
    val current_time = LocalTime.now()

    val Properties = "properties"
    val Periods = "periods"
    val Forecast = "forecast"
    val ShortForecast = "shortForecast"
    val Temperature = "temperature"

    // Simple function to return the temperature characterization
    // based on temperature itself and personal preference.
    //
    // @param temperature Temperature value
    //
    // @return Cold, Moderate or Hot depending on temperature
    def temperatureCharacterization(temperature: Double): String = 
        if temperature < 55 then
            "Cold"
        else if temperature >= 55 && temperature <= 75 then 
            "Moderate"
        else
            "Hot"

    // In order to get the weather information needed,
    // two requests need to be sent to the weather service
    // servers. The first one, here, will obtain the grid 
    // coordinates we need and the URL that we can query later. 
    // Function also writes to a file to record what was returned. 
    //
    // @param latitude Latitude coordinate
    // @param longitude Longitude coordinate
    //
    // @return Formatted string containing the short forecast, 
    // temperature and the temperature characterization
    def firstQuery(latitude: Int, longitude: Int): String = 
        val firstQuery: Response = requests.get(("https://api.weather.gov/points/%s,%s").format(latitude, longitude))
        val firstResponseText = firstQuery.text()

        val json = ujson.read(firstResponseText)
        val properties = json(Properties).obj
        val forecast = properties.get(Forecast)(0).str

        val filename = s"first_query_response_$latitude _$longitude _$current_date _$current_time.json".replace(":", "-")
        os.write(os.pwd / filename, firstResponseText)
        secondQuery(latitude, longitude, forecast)
        
    // The second requests gets the relevant information, that 
    // being the temperature and the short forecast. Also writes 
    // the response to a file. 
    //
    // @param latitude Latitude coordinate
    // @param longitude Longitude coordinate
    //
    // @return Formatted string containing the short forecast, 
    // temperature and the temperature characterization
    def secondQuery(latitude: Int, longitude: Int, url: String): String =
        val secondQuery: Response = requests.get(url)
        val secondResponseText = secondQuery.text()

        val json = ujson.read(secondResponseText)
        val properties = json(Properties).obj
        val periods = properties.get(Periods).arr
        val today = periods(0)(0).obj
        val temperature = today.get(Temperature)(0)
        val shortForecast = today.get(ShortForecast)(0).str
        
        val filename = s"second_query_response_$latitude _$longitude _$current_date _$current_time.json".replace(":", "-")
        os.write(os.pwd / filename, secondResponseText)
        
        val characterization = temperatureCharacterization(temperature.num)
        s"$shortForecast $temperature $characterization"
}