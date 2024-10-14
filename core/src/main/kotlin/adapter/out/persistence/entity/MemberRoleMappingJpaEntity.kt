package org.team_alilm.adapter.out.persistence.entity

import jakarta.persistence.*
import org.team_alilm.global.jpa.base.BaseEntity

@Entity
@Table(name = "member_role_mapping")
class MemberRoleMappingJpaEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val memberJpaEntity: MemberJpaEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    val roleJpaEntity: RoleJpaEntity,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) : BaseEntity()