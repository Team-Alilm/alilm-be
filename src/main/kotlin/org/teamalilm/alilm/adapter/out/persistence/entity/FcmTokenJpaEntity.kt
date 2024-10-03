package org.teamalilm.alilm.adapter.out.persistence.entity

import jakarta.persistence.*
import org.teamalilm.alilm.global.jpa.base.BaseTimeEntity

@Entity
@Table(name = "fcm_token")
class FcmTokenJpaEntity(
    @Column(nullable = false, unique = true)
    val token: String,

    @ManyToOne(fetch = FetchType.LAZY)
    val member: MemberJpaEntity,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) : BaseTimeEntity()