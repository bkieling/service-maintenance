package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return RfaDto by id=1"

    request {
        url "/rfa/1"
        method GET()
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body (
                id: 1,
                content: "Test 1"
        )
    }
}