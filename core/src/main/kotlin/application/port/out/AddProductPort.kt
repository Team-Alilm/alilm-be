package org.team_alilm.application.port.out

import org.team_alilm.domain.Product

interface AddProductPort {

    fun addProduct(product: Product): Product

}
