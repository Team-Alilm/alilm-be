package org.team_alilm.adapter.out.persistence.adapter

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.team_alilm.adapter.out.persistence.mapper.ProductMapper
import org.team_alilm.adapter.out.persistence.repository.ProductRepository
import org.team_alilm.adapter.out.persistence.repository.spring_data.SpringDataProductRepository
import org.team_alilm.application.port.out.AddProductPort
import org.team_alilm.application.port.out.LoadProductPort
import org.team_alilm.application.port.out.LoadProductsInBasketsPort
import org.team_alilm.domain.Product

@Component
class ProductAdapter(
    private val springDataProductRepository: SpringDataProductRepository,
    private val productRepository: ProductRepository,
    private val productMapper: ProductMapper,
) : AddProductPort, LoadProductPort, LoadProductsInBasketsPort {

    private val log = LoggerFactory.getLogger(ProductAdapter::class.java)

    override fun addProduct(product: Product) : Product {
        return productMapper
            .mapToDomainEntity(
                springDataProductRepository.save(
                    productMapper.mapToJpaEntity(product)
                )
            )
    }

    override fun loadProduct(
        number: Long,
        store: Product.Store,
        firstOption: String,
        secondOption: String?,
        thirdOption: String?,
    ): Product? {
        val productJpaEntity = productRepository.findByNumberAndStoreAndFirstOptionAndSecondOptionAndThirdOption(
            number = number,
            store = store,
            firstOption = firstOption,
            secondOption = secondOption,
            thirdOption = thirdOption
        )

        return productMapper.mapToDomainEntityOrNull(productJpaEntity)
    }

    override fun loadProduct(productId: Product.ProductId): Product? {
        val productJpaEntity = springDataProductRepository.findByIdAndIsDeleteFalse(productId.value)

        return productMapper.mapToDomainEntityOrNull(productJpaEntity)
    }

    override fun loadProductsInBaskets(): List<Product> {
        return try {
            productRepository.findProductsInBaskets().map {
                productMapper.mapToDomainEntity(it)
            }
        } catch (e: Exception) {
            log.error("Failed to load products in baskets", e)
            emptyList()
        }
    }

}