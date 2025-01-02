package com.be.squeak_squeak.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;

    private String email;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialUuid;

    private int totalGroup;

    private int totalVote;
}