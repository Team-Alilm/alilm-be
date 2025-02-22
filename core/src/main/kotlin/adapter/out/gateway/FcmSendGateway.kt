package org.team_alilm.adapter.out.gateway

import com.google.firebase.messaging.*
import domain.FcmToken
import domain.product.Product
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FcmSendGateway(
    private val firebaseMessaging: FirebaseMessaging
) {

    private val log = LoggerFactory.getLogger(FcmSendGateway::class.java)

    fun sendFcmMessage(
        product: Product,
        fcmToken: FcmToken,
        platform: String // "ios", "android", "web"
    ) {
        val title = "[${product.name}] 상품이 재입고 되었습니다!"
        val options = listOfNotNull(product.firstOption, product.secondOption, product.thirdOption)
            .joinToString(" / ")
        val body = """
        ${if (options.isNotBlank()) "option : $options" else ""}
        지금 바로 확인해보세요.
    """.trimIndent()

        val messageBuilder = Message.builder()
            .setToken(fcmToken.token)
            .putData("click_action", product.localServiceUrl())

        when (platform) {
            "web" -> {
                messageBuilder.setWebpushConfig(
                    WebpushConfig.builder()
                        .setNotification(
                            WebpushNotification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .setImage(product.thumbnailUrl)
                                .build()
                        )
                        .build()
                )
            }
            "android" -> {
                messageBuilder.setAndroidConfig(
                    AndroidConfig.builder()
                        .setNotification(
                            AndroidNotification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .setImage(product.thumbnailUrl)
                                .build()
                        )
                        .build()
                )
            }
            "ios" -> {
                messageBuilder.setApnsConfig(
                    ApnsConfig.builder()
                        .setAps(
                            Aps.builder()
                                .setAlert(
                                    ApsAlert.builder()
                                        .setTitle(title)
                                        .setBody(body)
                                        .build()
                                )
                                .setBadge(1)
                                .setSound("default")
                                .build()
                        )
                        .build()
                )
            }
        }

        val message = messageBuilder.build()

        try {
            firebaseMessaging.send(message)
        } catch (e: Exception) {
            log.error("Failed to send message: $message")
            log.error("Error: $e")
            return
        }

        log.info("Successfully sent message: $message")
    }

}
