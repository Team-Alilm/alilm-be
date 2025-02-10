package org.team_alilm.adapter.out.persistence.repository.product

import org.team_alilm.adapter.out.persistence.entity.ProductJpaEntity

data class ProductAndWaitingCount(
    val productJpaEntity: ProductJpaEntity,
    val waitingCount: Long
)
