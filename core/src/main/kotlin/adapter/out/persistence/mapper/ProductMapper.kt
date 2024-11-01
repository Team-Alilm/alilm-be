package org.team_alilm.adapter.out.persistence.mapper

import org.springframework.stereotype.Component
import org.team_alilm.adapter.out.persistence.entity.ProductJpaEntity
import org.team_alilm.domain.Product

@Component
class ProductMapper {

    fun mapToJpaEntity(product: Product): ProductJpaEntity {
        return ProductJpaEntity(
            id = product.id?.value,
            number = product.number,
            name = product.name,
            brand = product.brand,
            imageUrl = product.imageUrl,
            store = product.store,
            category = product.category,
            price = product.price,
            firstOption = product.firstOption,
            secondOption = product.secondOption,
            thirdOption = product.thirdOption
        )
    }

    fun mapToDomainEntityOrNull(productJpaEntity: ProductJpaEntity?): Product? {
        productJpaEntity ?: return null

        return Product(
            id = Product.ProductId(productJpaEntity.id!!),
            number = productJpaEntity.number,
            name = productJpaEntity.name,
            brand = productJpaEntity.brand,
            imageUrl = productJpaEntity.imageUrl,
            store = productJpaEntity.store,
            category = productJpaEntity.category,
            price = productJpaEntity.price,
            firstOption = productJpaEntity.firstOption,
            secondOption = productJpaEntity.secondOption,
            thirdOption = productJpaEntity.thirdOption
        )
    }

    fun mapToDomainEntity(productJpaEntity: ProductJpaEntity): Product {
        return Product(
            id = Product.ProductId(productJpaEntity.id!!),
            number = productJpaEntity.number,
            name = productJpaEntity.name,
            brand = productJpaEntity.brand,
            imageUrl = productJpaEntity.imageUrl,
            store = productJpaEntity.store,
            category = productJpaEntity.category,
            price = productJpaEntity.price,
            firstOption = productJpaEntity.firstOption,
            secondOption = productJpaEntity.secondOption,
            thirdOption = productJpaEntity.thirdOption
        )
    }

}