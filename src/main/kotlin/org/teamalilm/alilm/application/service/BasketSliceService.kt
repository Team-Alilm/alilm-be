package org.teamalilm.alilm.application.service

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.teamalilm.alilm.application.port.`in`.use_case.BasketSliceUseCase
import org.teamalilm.alilm.application.port.`in`.use_case.BasketSliceUseCase.*
import org.teamalilm.alilm.application.port.out.LoadSliceBasketPort

@Service
@Transactional(readOnly = true)
class BasketSliceService (
    private val loadProductSlicePort: LoadSliceBasketPort
) : BasketSliceUseCase {

    override fun basketSlice(command: BasketListCommand): Slice<BasketListResult> {

        val basketCountProjectionSlice = loadProductSlicePort.loadBasketSlice(
            PageRequest.of(
                command.page,
                command.size,
            )
        )

        return basketCountProjectionSlice.map {
            BasketListResult(
                id = it.product.id?.value!!,
                number = it.product.number,
                name = it.product.name,
                brand = it.product.brand,
                imageUrl = it.product.imageUrl,
                store = it.product.store.name,
                price = it.product.price,
                category = it.product.category,
                firstOption = it.product.firstOption,
                secondOption = it.product.secondOption,
                thirdOption = it.product.thirdOption,
                waitingCount = it.waitingCount
            )
        }
    }

}