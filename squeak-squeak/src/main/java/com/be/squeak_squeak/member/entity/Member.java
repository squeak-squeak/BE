package com.be.squeak_squeak.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "member")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String image;

    private String email;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialUuid;

    @Builder.Default
    private int totalGroup = 0;

    @Builder.Default
    private int totalVote = 0;
}