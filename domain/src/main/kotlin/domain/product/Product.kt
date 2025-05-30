package domain.product

import util.StringContextHolder

class Product (
    val id: Long? = null,
    val storeNumber: Long = 0,
    val name: String = "",
    val brand: String = "",
    val thumbnailUrl: String = "",
    val firstCategory: String = "",
    val secondCategory: String? = null,
    val price: Int = 0,
    val store: Store = Store.NONE,
    val firstOption: String? = null,
    val secondOption: String? = null,
    val thirdOption: String? = null,
    val createdDate: Long = 0,
    val lastModifiedDate: Long = 0,
) {

    fun getManagedCode(): String? {
        val parts = listOfNotNull(firstOption, secondOption, thirdOption)
            .filter { it.isNotEmpty() }

        return when (parts.size) {
            3 -> "${parts[0]}^${parts[1]}^${parts[2]}"
            2 -> "${parts[0]}^${parts[1]}"
            1 -> parts[0]
            else -> null
        }
    }

    fun toSlackMessage(): String = """
        {
            "text":"${this.name} 상품이 재 입고 되었습니다.
        
                상품명: ${this.name}
                상품번호: ${this.storeNumber}
                상품 옵션1: ${this.firstOption}
                상품 옵션2: ${this.secondOption}
                상품 옵션3: ${this.thirdOption}
                상품 구매링크 : ${getStoreUrl()}
                바구니에서 삭제되었습니다.
                "
        }
    """.trimIndent()

    fun getStoreUrl(): String {
        return when (store) {
            Store.MUSINSA -> StringContextHolder.MUSINSA_PRODUCT_URL.get().format(storeNumber)
            Store.CM29 -> StringContextHolder.CM29_PRODUCT_URL.get().format(storeNumber)
            Store.ZIGZAG -> StringContextHolder.ZIGZAG_PRODUCT_URL.get().format(storeNumber)
            else -> ""
        }
    }

    fun localServiceUrl() : String {
        return when (store) {
            Store.MUSINSA -> StringContextHolder.LOCAL_HOST.get().format(id!!)
            Store.CM29 -> StringContextHolder.LOCAL_HOST.get().format(id!!)
            Store.ZIGZAG -> StringContextHolder.LOCAL_HOST.get().format(id!!)
            else -> ""
        }
    }

    fun getZigzagOptionName () : String {
        return if (this.firstOption?.isNotBlank() == true && this.secondOption?.isNotBlank() == true && this.thirdOption?.isNotBlank() == true) {
            "${firstOption}/${secondOption}/${thirdOption}"
        } else if (firstOption?.isNotBlank() == true && secondOption?.isNotBlank() == true) {
            "${firstOption}/${secondOption}"
        } else {
            firstOption ?: ""
        }
    }
}