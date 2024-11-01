package org.team_alilm.adapter.out.persistence.repository.spring_data

import org.springframework.data.jpa.repository.JpaRepository
import org.team_alilm.adapter.out.persistence.entity.ProductImageJpaEntity

interface SpringDataProductImageRepository : JpaRepository<ProductImageJpaEntity, Long> {
}