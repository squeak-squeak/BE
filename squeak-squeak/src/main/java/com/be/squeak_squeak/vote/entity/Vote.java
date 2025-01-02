package com.be.squeak_squeak.vote.entity;

import com.be.squeak_squeak.group.entity.UserGroup;
import com.be.squeak_squeak.groupMember.entity.GroupMember;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "vote")
@AllArgsConstructor
@NoArgsConstructor
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private VoteStatus status;

    private int voteCount;

    // 투표 생성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_member_id")
    private GroupMember groupMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_group_id")
    private UserGroup userGroup;
}
