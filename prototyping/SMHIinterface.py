import requests
from  pathlib import Path
import time

# Endpoint SMHI
URL_forecast = "https://opendata-download-metfcst.smhi.se"


# find relevant Gothenburg meaurepoints
# assume Gothenburg is a rectangle with 
# NW corner: long, lat 57.72787297412955, 11.940363362519513
# SE Corner: long, lat 57.67117105976618, 12.018108769018003
getMultipointRequest = "/api/category/pmp3g/version/2/geotype/multipoint.json?downsample=2"

r = requests.get(URL_forecast+getMultipointRequest)
data = r.json()
numberOfLocations = len(data["coordinates"])
print(f"all in all there are {numberOfLocations} locations that we can ask forecast for")


NorthBorder =  57.8 # 57.72787297412955
SouthBorder =  57.6 # 57.67117105976618
WestBorder  =  11.9 # 11.940363362519513
EastBorder  =  12.1 # 12.018108769018003


filteredLocations = [pair for pair in data["coordinates"] if WestBorder <= pair[0] <= EastBorder]
filteredLocations = [pair for pair in filteredLocations if SouthBorder <= pair[1] <= NorthBorder]
# print(filteredLocations) This turned out to be only one location ...


for filteredLocation in filteredLocations:
    # getting the weather for the location we filtered
    getWeatherRequest = "/api/category/pmp3g/version/2/geotype/point/lon/" + str(filteredLocation[0]) + "/lat/" + str(filteredLocation[1]) + "/data.json"

    r = requests.get(URL_forecast+getWeatherRequest)
  
    # extracting data in json format
    data = r.json()

    timeForPrognosis = data["timeSeries"][0]["validTime"]
    for datapoint in data["timeSeries"][0]["parameters"]:
        if datapoint["name"] == 't':
            Temperature = datapoint["values"][0]

    print(f"We predict the temperature will be {Temperature} at {timeForPrognosis} in the location(lat,long) {str(filteredLocation[1])}, {str(filteredLocation[0])}")
    
URL_observations = "https://opendata-download-metobs.smhi.se"
version = "1.0"
ext = "json"
getVersionsRequest = "/api/version/" + version + "." + ext

r = requests.get(URL_observations+getVersionsRequest)
data = r.json()
# station 71420 seems to be Gothenburg A
parameter = "1"
station = "71420"
period = "latest-day"
getStationWeatherRequest = "/api/version/" + version+ "/parameter/" + parameter + "/station/" + station + "/period/" + period+ "/data." + ext
r = requests.get(URL_observations+getStationWeatherRequest)
data = r.json()
temperatureMeasurements = data["value"]
for temperatureMeasurement in temperatureMeasurements:
    # get the Epoch time of the mesurement and convert it to s from ms Need to check if it is GMT or local time
    t = time.gmtime(temperatureMeasurement["date"]/1000)
    temperature = temperatureMeasurement["value"]
    
    print(f"At {t.tm_hour:02}:{t.tm_min:02} on {t.tm_mday}/{t.tm_mon} the temperature was {temperature}")

