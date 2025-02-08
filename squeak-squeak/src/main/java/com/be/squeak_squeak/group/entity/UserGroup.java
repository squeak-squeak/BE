package com.be.squeak_squeak.group.entity;

import com.be.squeak_squeak.groupMember.entity.GroupMember;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_group")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String image;

    private String description;

    private int totalMemberCount;

    private String inviteCode;

    @OneToMany(mappedBy = "userGroup", cascade = CascadeType.ALL)
    private List<GroupMember> groupMembers = new ArrayList<>();

    public void updateGroup(String name, String description){
        this.name = name;
        this.description = description;
    }

    public void updateGroupImage(String image) {
        this.image = image;
    }
}
