package com.soulware.backend.domain.file.entity;

import com.soulware.backend.domain.common.entity.Timestamp;
import com.soulware.backend.domain.post.entity.Post;
import com.soulware.backend.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "files")
@Entity
public class File extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String fileName;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public File(String originalFileName, String fileName, User user) {
        this.originalFileName = originalFileName;
        this.fileName = fileName;
        this.user = user;
    }

    public void updateFileName(String originalFileName, String fileName) {
        this.originalFileName = originalFileName;
        this.fileName = fileName;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
