package org.team_alilm.adapter.`in`.web.controller.alilm

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.team_alilm.application.port.`in`.use_case.AlilmRestockRankingUseCase

@RestController
@RequestMapping("/api/v1/alilms/restock/rangin7")
class AlilmRestockRankingController(
    private val alilmRestockRankingUseCase: AlilmRestockRankingUseCase
) {

    @Operation(
        summary = "최근 알림 조회 API",
        description = """
            최근 재 입고 상품 목록 7개를 반환합니다.
    """)
    @GetMapping
    fun alilmRestockRangin(
        @RequestBody request: AlilmRestockRankingRequest
    ) : ResponseEntity<AlilmRestockRanking7Response> {
        val command = AlilmRestockRankingUseCase.AlilmRestockRankingCommand(
            count = request.count
        )
        val result = alilmRestockRankingUseCase.alilmRestockRangin(command)
        val alilmRestockRanking7Products = result.map {
            AlilmRestockRanking7Product.from(
                productId = it.id!!.value, productThumbnailUrl = it.thumbnailUrl
            )
        }

        return ResponseEntity.ok(AlilmRestockRanking7Response(alilmRestockRanking7Products))
    }

    data class AlilmRestockRankingRequest(
        val count: Int
    )

    data class AlilmRestockRanking7Response(
        val productList: List<AlilmRestockRanking7Product>
    )

    data class AlilmRestockRanking7Product(
        val productId: Long,
        val productThumbnailUrl: String,
    ) {

        companion object {
            fun from(productId: Long, productThumbnailUrl: String): AlilmRestockRanking7Product {
                return AlilmRestockRanking7Product(
                    productId = productId,
                    productThumbnailUrl = productThumbnailUrl
                )
            }
        }
    }
}