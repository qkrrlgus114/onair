    package com.b302.zizon.domain.user.entity;

    import com.b302.zizon.domain.user.dto.UserDTO;
    import lombok.Builder;
    import lombok.Getter;
    import lombok.ToString;
    import org.springframework.data.annotation.CreatedDate;
    import org.springframework.data.annotation.LastModifiedDate;
    import org.springframework.data.jpa.domain.support.AuditingEntityListener;

    import javax.persistence.*;
    import java.time.LocalDateTime;

    @Entity
    @Getter
    @ToString
    @EntityListeners(AuditingEntityListener.class)
    @Builder
    public class User {

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long userId;
        @Column(nullable = false)
        private String accountType;
        @Column(nullable = false)
        private String email;
        @Column(nullable = false, unique = true)
        private String nickname;
        @Column(nullable = false)
        private String profileImage;
        @Column(nullable = false)
        private String privateAccess;
        @CreatedDate
        @Column(nullable = false)
        private LocalDateTime registDate;
        @LastModifiedDate
        @Column(nullable = false)
        private LocalDateTime lastLoginDate;
        @Column(nullable = false)
        private boolean deleteCheck;
        @Column(nullable = false)
        private String accessToken;
        @Column(nullable = false)
        private boolean createCheck;


        public User() {
        }

        public User(Long userId, String accountType, String email, String nickname, String profileImage, String privateAccess, LocalDateTime registDate, LocalDateTime lastLoginDate, boolean deleteCheck, String accessToken, boolean createCheck) {
            this.userId = userId;
            this.accountType = accountType;
            this.email = email;
            this.nickname = nickname;
            this.profileImage = profileImage;
            this.privateAccess = privateAccess;
            this.registDate = registDate;
            this.lastLoginDate = lastLoginDate;
            this.deleteCheck = deleteCheck;
            this.accessToken = accessToken;
            this.createCheck = createCheck;
        }

        public void updateNickname(String newNickname) {
            this.nickname = newNickname;
        }

        public void updateprofileImage(String profileImage){
            this.profileImage = profileImage;
        }

        public UserDTO toUserDTO(){
            return UserDTO.builder()
                    .userId(this.userId)
                    .accountType(this.accountType)
                    .email(this.email)
                    .nickname(this.nickname)
                    .profileImage(this.profileImage)
                    .build();
        }

        public void updateLastLoginDate() {
            this.lastLoginDate = LocalDateTime.now();
        }

        public void updateAccessToken(String accessToken){
            this.accessToken = accessToken;
        }

        public void updatePrivateAccessToken(String privateAccessToken){
            this.privateAccess = privateAccessToken;
        }

        // 온캐스트 생성하면 true로 변경
        public void updateCreateCheck(){
            this.createCheck = true;
        }


    }
