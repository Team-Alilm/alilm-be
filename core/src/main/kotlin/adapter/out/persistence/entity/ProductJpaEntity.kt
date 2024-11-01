package org.team_alilm.adapter.out.persistence.entity

import jakarta.persistence.*
import org.team_alilm.domain.Product
import org.team_alilm.global.jpa.base.BaseTimeEntity

@Entity
@Table(
    name = "product",
    uniqueConstraints = [UniqueConstraint(
        name = "tag_key_number_size_color",
        columnNames = ["store", "number", "first_option", "second_option", "third_option"]
    )]
)
open class ProductJpaEntity(
    @Column(nullable = false)
    val number :Long,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val brand: String,

    @Column(nullable = false)
    val imageUrl: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val store: Product.Store,

    @Column(nullable = false)
    val category: String,

    @Column(nullable = false)
    val price: Int,

    @Column(nullable = false)
    val firstOption: String,

    @Column
    val secondOption: String?,

    @Column
    val thirdOption: String?,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) : BaseTimeEntity()