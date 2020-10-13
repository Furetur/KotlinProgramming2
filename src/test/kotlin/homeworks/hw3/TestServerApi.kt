package homeworks.hw3

import homeworks.hw3.server.ServerResponse

class TestServerApi : ClientNetworkApi {

    var carArrivalsCount = 0
    var carDeparturesCount = 0

    override fun handleCarArrival(): ServerResponse {
        carArrivalsCount += 1
        return ServerResponse.SUCCESS
    }

    override fun handleCarDeparture(): ServerResponse {
        carDeparturesCount += 1
        return ServerResponse.SUCCESS
    }
}
