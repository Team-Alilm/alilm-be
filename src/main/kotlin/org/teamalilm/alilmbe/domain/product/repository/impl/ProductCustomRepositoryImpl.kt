package org.teamalilm.alilmbe.domain.product.repository.impl

import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import org.teamalilm.alilmbe.domain.basket.entity.QBasket.basket
import org.teamalilm.alilmbe.domain.member.entity.QMember.member
import org.teamalilm.alilmbe.domain.product.entity.Product
import org.teamalilm.alilmbe.domain.product.entity.QProduct.product
import org.teamalilm.alilmbe.domain.product.repository.ProductCustomRepository

class ProductCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : ProductCustomRepository {

    override fun productList(query: ProductListQuery): Slice<ProductListProjection> {
        val content = jpaQueryFactory
            .select(
                Projections.fields(
                    ProductListProjection::class.java,
                    product.id,
                    product.name,
                    product.brand,
                    product.imageUrl,
                    product.price,
                    product.category,
                    product.productInfo,
                    basket.member.count().`as`("waitingCount"),
                    basket.createdDate.min().`as`("oldestCreationTime")
                )
            )
            .from(basket)
            .leftJoin(basket.product, product)
            .leftJoin(basket.member, member)
            .groupBy(basket.product.id)
            .orderBy(basket.member.id.count().desc(), product.name.asc())
            .offset(query.pageRequest.offset)
            .limit(query.pageRequest.pageSize.toLong() + 1)
            .fetch()

        val hasNext = content.size > query.pageRequest.pageSize

        if (hasNext) {
            content.removeAt(content.size - 1)
        }

        return SliceImpl(
            content,
            query.pageRequest,
            hasNext
        )
    }

    data class ProductListQuery(
        val pageRequest: PageRequest
    )

    data class ProductListProjection(
        val id: Long,
        val name: String,
        val brand: String,
        val imageUrl: String,
        val price: Int,
        val category: String,
        val productInfo: Product.ProductInfo,
        val waitingCount: Long,
        val oldestCreationTime: Long,
    )
}