package org.team_alilm.adapter.`in`.web.controller.product

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.team_alilm.application.port.`in`.use_case.product.crawling.ProductCrawlingUseCase
import org.team_alilm.application.port.`in`.use_case.product.crawling.ProductCrawlingUseCaseResolver
import org.team_alilm.domain.product.Store
import org.team_alilm.global.error.NotParserProduct

@RestController
@Tag(name = "상품 크롤링 조회 API", description = "상품 크롤링 조회 API를 제공합니다.")
@RequestMapping("/api/v1/products")
class ProductCrawlingController(
    private val productCrawlingUseCaseResolver: ProductCrawlingUseCaseResolver,
) {

    @Operation(
        summary = "상품 크롤링 조회 API",
        description = """
            상품 크롤링을 통해 상품 정보를 조회할 수 있는 API를 제공합니다.
    """
    )
    @GetMapping("/crawling")
    fun crawling(
        @ParameterObject
        @Valid
        productCrawlingParameter: ProductCrawlingParameter,
    ) : ResponseEntity<ProductCrawlingResponse> {
        val store = productCrawlingParameter.getStore()
        val productNumber = productCrawlingParameter.getProductNumber() ?: throw NotParserProduct()
        val command = ProductCrawlingUseCase.ProductCrawlingCommand(
            url = productCrawlingParameter.url,
            store = store,
            productNumber = productNumber
        )

        val productCrawlingUseCase = productCrawlingUseCaseResolver.resolve(store)
        val result = productCrawlingUseCase.crawling(command)

        val response = ProductCrawlingResponse.from(result)

        return ResponseEntity.ok(response)
    }

    data class ProductCrawlingParameter(
        @field:NotBlank(message = "URL은 비워둘 수 없습니다.")
        @field:Pattern(
            regexp = "^(https?://).+",
            message = "URL은 반드시 http 또는 https로 시작해야 합니다."
        )
        val url: String
    ) {

        fun getStore(): Store {
            return when {
                url.contains("29cm") -> Store.CM29
                url.contains("musinsa") -> Store.MUSINSA
                url.contains("a-bly") -> Store.A_BLY
                else -> throw IllegalArgumentException("지원하지 않는 URL입니다.")
            }
        }

        fun getProductNumber(): Long? {
            // 정규식으로 6자리 이상의 숫자 추출
            val regex = "\\d{6,}".toRegex()
            return regex.find(url)?.value?.toLongOrNull()
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
                    imageUrlList = productCrawlingResult.imageUrlList,
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