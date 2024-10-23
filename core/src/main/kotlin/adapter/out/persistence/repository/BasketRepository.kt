package org.team_alilm.adapter.out.persistence.repository

import jakarta.persistence.Tuple
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.team_alilm.adapter.out.persistence.entity.BasketJpaEntity

interface BasketRepository : JpaRepository<BasketJpaEntity, Long> {

    @Query("""
            SELECT 
                p as productJpaEntity, 
                COUNT(b) as waitingCount
            FROM 
                ProductJpaEntity p
            JOIN 
                BasketJpaEntity b
                ON b.productId = p.id
            WHERE 
                b.isDelete = false
                AND b.isHidden = false
                AND p.isDelete = false
            GROUP BY 
                p.id
            ORDER BY 
                COUNT(b) DESC
    """)
    fun loadBasketSlice(pageRequest: PageRequest): Slice<Tuple>

    @Query("""
        SELECT 
            b as basketJpaEntity, 
            p as productJpaEntity, 
            COUNT(otherBaskets) as waitingCount
        FROM 
            BasketJpaEntity b
        JOIN 
            ProductJpaEntity p
            ON b.productId = p.id
        LEFT JOIN 
            BasketJpaEntity otherBaskets
            ON otherBaskets.productId = b.productId 
            AND otherBaskets.isDelete = false
        WHERE 
            b.isDelete = false
            AND b.memberId = :memberId
        GROUP BY 
            b.productId, 
            b.id
        ORDER BY
            b.createdDate DESC
    """)
    fun myBasketList(memberId: Long): List<Tuple>

    @Query("""
        SELECT 
            b as basketJpaEntity
        FROM
            BasketJpaEntity b
        JOIN
            ProductJpaEntity p
            ON b.productId = p.id
        WHERE
            b.isDelete = false
            AND p.isDelete = false
            AND b.isAlilm = false
            AND p.number = :productNumber
    """)
    fun findByProductNumber(productNumber: Number): List<BasketJpaEntity>
}

