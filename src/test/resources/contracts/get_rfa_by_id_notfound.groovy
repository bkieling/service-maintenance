package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 404 not found for id=3 "

    request {
        url "/rfa/3"
        method GET()
    }

    response {
        status 404
    }

}
