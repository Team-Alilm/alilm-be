package org.team_alilm.adapter.out.persistence.entity

import jakarta.persistence.*
import org.team_alilm.global.jpa.base.BaseTimeEntity

@Entity
@Table(name = "product_image",
    indexes = [
        Index(
            name = "idx_product_id",
            columnList = "product_id"
        )
    ]
)
class ProductImageJpaEntity(

    @Column(nullable = false)
    val productId: Long,

    @Column(nullable = false, unique = true)
    val imageUrl: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) : BaseTimeEntity()
