package org.teamalilm.alilmbe.adapter.out.gateway

import org.jsoup.Jsoup
import org.springframework.stereotype.Service
import org.teamalilm.alilmbe.application.port.out.gateway.CrawlingGateway

@Service
class JsoupProductDataGateway : CrawlingGateway {

    override fun crawling(request: CrawlingGateway.CrawlingGatewayRequest): CrawlingGateway.CrawlingGatewayResponse {
        return Jsoup.connect(request.url).get().let {
            CrawlingGateway.CrawlingGatewayResponse(document = it)
        }
    }

}