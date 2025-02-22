package org.team_alilm.adapter.out.gateway

import org.jsoup.Jsoup
import org.springframework.stereotype.Component
import org.team_alilm.application.port.out.gateway.crawling.CrawlingGateway

@Component
class HtmlCrawlingGateway : CrawlingGateway {

    override fun htmlCrawling(request: CrawlingGateway.CrawlingGatewayRequest): CrawlingGateway.CrawlingGatewayResponse {
        val html = Jsoup
            .connect(request.url)
            .get()
            .html()

        return CrawlingGateway.CrawlingGatewayResponse(html)
    }

}