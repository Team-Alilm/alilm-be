package org.team_alilm.controller

import domain.product.Store
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.team_alilm.application.port.use_case.ProductCrawlingUseCase
import org.team_alilm.application.port.use_case.ProductCrawlingUseCaseResolver

@RestController
@RequestMapping("/api/v1/products")
class ProductCrawlingController(
    private val productCrawlingUseCaseResolver: ProductCrawlingUseCaseResolver,
) {

    @GetMapping("/crawling")
    fun crawling(
        productCrawlingParameter: ProductCrawlingParameter
    ) : ResponseEntity<ProductCrawlingResponse> {
        val store = productCrawlingParameter.getStore()

        val command = ProductCrawlingUseCase.ProductCrawlingCommand(
            url = productCrawlingParameter.url,
            store = store,
        )

        val productCrawlingUseCase = productCrawlingUseCaseResolver.resolve(store)
        val result = productCrawlingUseCase.crawling(command)
        val response = ProductCrawlingResponse.from(result)

        return ResponseEntity.ok(response)
    }

    data class ProductCrawlingParameter(
        val url: String
    ) {

        fun getStore(): Store {
            return when {
                url.contains("29cm") -> Store.CM29
                url.contains("musinsa") -> Store.MUSINSA
                url.contains("zigzag") -> Store.ZIGZAG
//                url.contains("a-bly") -> Store.A_BLY
                else -> throw IllegalArgumentException("지원하지 않는 URL입니다.")
            }
        }
    }

    data class ProductCrawlingResponse(
        val number: Long,
        val name: String,
        val brand: String,
        val thumbnailUrl: String,
        val imageUrlList: List<String> = emptyList(),
        val store: Store,
        val price: Int,
        val firstCategory: String,
        val secondCategory: String?,
        val firstOptions: List<String>,
        val secondOptions: List<String> = emptyList(),
        val thirdOptions: List<String> = emptyList(),
    ) {

        companion object {
            fun from(productCrawlingResult: ProductCrawlingUseCase.CrawlingResult): ProductCrawlingResponse {
                return ProductCrawlingResponse(
                    number = productCrawlingResult.number,
                    name = productCrawlingResult.name,
                    brand = productCrawlingResult.brand,
                    thumbnailUrl = productCrawlingResult.thumbnailUrl,
                    imageUrlList = if (productCrawlingResult.imageUrlList.size > 3) {
                        productCrawlingResult.imageUrlList.shuffled().take(3)
                    } else {
                        productCrawlingResult.imageUrlList
                    },
                    store = productCrawlingResult.store,
                    price = productCrawlingResult.price,
                    firstCategory = productCrawlingResult.firstCategory,
                    secondCategory = productCrawlingResult.secondCategory,
                    firstOptions = productCrawlingResult.firstOptions,
                    secondOptions = productCrawlingResult.secondOptions,
                    thirdOptions = productCrawlingResult.thirdOptions
                )
            }
        }
    }
}