package org.team_alilm.application.port.`in`.use_case

import org.team_alilm.domain.Basket
import org.team_alilm.domain.Member
import org.team_alilm.domain.Product

interface MyBasketsUseCase {

    fun myBasket(command: MyBasketCommand) : List<MyBasketsResult>

    data class MyBasketCommand(
        val member: Member
    )

    data class MyBasketsResult(
        val id: Long,
        val number: Long,
        val name: String,
        val brand: String,
        val imageUrl: String,
        val store: String,
        val price: Int,
        val category: String,
        val firstOption: String,
        val secondOption: String?,
        val thirdOption: String?,
        val isHidden: Boolean,
        val waitingCount: Long
    ) {

        companion object {
            fun from(basket: Basket, product: Product, waitingCount: Long) : MyBasketsResult {
                return MyBasketsResult(
                    id = basket.id?.value!!,
                    number = product.number,
                    name = product.name,
                    brand = product.brand,
                    imageUrl = product.imageUrl,
                    store = product.store.name,
                    price = product.price,
                    category = product.category,
                    firstOption = product.firstOption,
                    secondOption = product.secondOption,
                    thirdOption = product.thirdOption,
                    isHidden = basket.isHidden,
                    waitingCount = waitingCount
                )
            }
        }

    }

}