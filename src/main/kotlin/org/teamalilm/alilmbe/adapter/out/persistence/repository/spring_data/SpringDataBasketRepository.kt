package org.teamalilm.alilmbe.adapter.out.persistence.repository.spring_data

import org.springframework.data.jpa.repository.JpaRepository
import org.teamalilm.alilmbe.adapter.out.persistence.entity.BasketJpaEntity

interface SpringDataBasketRepository : JpaRepository<BasketJpaEntity, Long> {

    fun findByMemberJpaEntityIdAndProductJpaEntityId(
        memberJpaEntityId: Long,
        productJpaEntityId: Long
    ): BasketJpaEntity?

}
