package org.team_alilm.controller.product

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.team_alilm.application.port.`in`.use_case.ProductDetailsUseCase
import org.team_alilm.application.port.`in`.use_case.ProductDetailsUseCase.*

@RestController
@Tag(name = "상품 상세 조회 API", description = "상품 상세 조회 API를 제공합니다.")
@RequestMapping("/api/v1/products")
class ProductDetailsController(
    private val productDetailsUseCase: ProductDetailsUseCase
) {

    @Operation(
        summary = "상품 상세 조회 API",
        description = """
            상품 상세 정보를 조회할 수 있는 API를 제공합니다.
    """
    )
    @GetMapping("/{productId}")
    fun productDetails(
        @PathVariable
        productId: Long

    ) : ResponseEntity<org.team_alilm.controller.product.ProductDetailsController.ProductDetailsResponse> {
        val command = ProductDetailsCommand(productId = productId)
        val result = productDetailsUseCase.productDetails(command = command)
        val response =
            org.team_alilm.controller.product.ProductDetailsController.ProductDetailsResponse.Companion.from(result)

        return ResponseEntity.ok(response)
    }

    data class ProductDetailsResponse(
        val id: Long,
        val number: Long,
        val name: String,
        val brand: String,
        val thumbnailUrl: String,
        val imageUrlList: List<String>,
        val store: String,
        val price: Int,
        val firstOption: String?,
        val secondOption: String?,
        val thirdOption: String?,
        val waitingCount: Long
    ) {

        companion object {
            fun from (productDetails: ProductDetailsResult): org.team_alilm.controller.product.ProductDetailsController.ProductDetailsResponse {
                return org.team_alilm.controller.product.ProductDetailsController.ProductDetailsResponse(
                    id = productDetails.id,
                    number = productDetails.number,
                    name = productDetails.name,
                    brand = productDetails.brand,
                    thumbnailUrl = productDetails.thumbnailUrl,
                    imageUrlList = productDetails.imageUrlList,
                    store = productDetails.store,
                    price = productDetails.price,
                    firstOption = productDetails.firstOption,
                    secondOption = productDetails.secondOption,
                    thirdOption = productDetails.thirdOption,
                    waitingCount = productDetails.waitingCount
                )
            }
        }
    }
}
