package org.team_alilm.quartz.job

import io.awspring.cloud.sqs.operations.SqsTemplate
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.team_alilm.application.port.out.*
import domain.product.Product
import org.slf4j.LoggerFactory
import org.team_alilm.application.port.out.gateway.SendSlackGateway

@Component
@Transactional(readOnly = true)
class SoldoutCheckJob(
    private val loadCrawlingProductsPort: LoadCrawlingProductsPort,
    private val sqsTemplate: SqsTemplate,
) : Job {

    private val log = LoggerFactory.getLogger(SoldoutCheckJob::class.java)

    @Transactional
    override fun execute(context: JobExecutionContext) {
        val productList: List<Product> = loadCrawlingProductsPort.loadCrawlingProducts()
        log.info("SoldoutCheckJob started with ${productList.size} products.")

        productList.forEach {
            sqsTemplate.send("product-soldout-check-queue", it)
        }
    }
}
