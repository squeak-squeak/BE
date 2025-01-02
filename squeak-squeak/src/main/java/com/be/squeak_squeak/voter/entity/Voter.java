package com.be.squeak_squeak.voter.entity;

import com.be.squeak_squeak.groupMember.entity.GroupMember;
import com.be.squeak_squeak.vote.entity.Vote;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "voter")
@AllArgsConstructor
@NoArgsConstructor
public class Voter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isVoted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id")
    private Vote vote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_member_id")
    private GroupMember groupMember;
}
