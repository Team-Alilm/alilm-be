package org.team_alilm.global.util

enum class StringContextHolder(
    val value: String
) {

    MUSINSA_OPTION_API_URL("https://goods-detail.musinsa.com/api2/goods/%s/options?goodsSaleType=SALE"),
    MUSINSA_PRODUCT_HTML_URL("https://store.musinsa.com/app/goods/%s"),
    MUSINSA_PRODUCT_IMAGES_URL("https://goods-detail.musinsa.com/api2/goods/%s/recommends/multi?uuid=detail_goods_attributes_allbrand&limit=3"),

    ABLY_PRODUCT_API_URL("https://api.a-bly.com/api/v2/goods/%s/?channel=0"),
    ABLY_ANONYMOUS_TOKEN("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhbm9ueW1vdXNfaWQiOiIzMjAxNjQ2MzkiLCJpYXQiOjE3MzIwMTg4Mzh9.BdsnayAvbtVgEOoA9zyZm0cWQY3Voliqw1WeO9iyHqw"),
    ABLY_PRODUCT_OPTIONS_API_URL("https://api.a-bly.com/api/v2/goods/%s/options/?depth=%s"),
    ABLY_PRODUCT_RELATED_GOODS_API_URL("https://api.a-bly.com/api/v2/goods/%s/related_goods"),

    CM29_PRODUCT_DETAIL_API_URL("https://bff-api.29cm.co.kr/api/v5/product-detail/%s"),;

    fun get(): String {
        return value
    }
}