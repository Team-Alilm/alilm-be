package org.team_alilm.adapter.out.gateway

import org.jsoup.Jsoup
import org.springframework.stereotype.Service
import org.team_alilm.application.port.out.gateway.CrawlingGateway

@Service
class JsoupProductDataGateway : CrawlingGateway {

    override fun crawling(request: CrawlingGateway.CrawlingGatewayRequest): CrawlingGateway.CrawlingGatewayResponse {
        return Jsoup.connect(request.url).get().let {
            CrawlingGateway.CrawlingGatewayResponse(html = it.getElementsByTag("script").html())
        }
    }

}