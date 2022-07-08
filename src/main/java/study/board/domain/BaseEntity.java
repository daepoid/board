package study.board.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.UUID;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity extends BaseTimeEntity{

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;

    public BaseEntity() {
        super();
        String uuid = UUID.randomUUID().toString();
        this.createdBy = uuid;
        this.lastModifiedBy = uuid;
    }

    public BaseEntity(String createdBy) {
        super();
        this.createdBy = createdBy;
        this.lastModifiedBy = createdBy;
    }

    public BaseEntity(LocalDateTime createdDate, String createdBy) {
        super(createdDate);
        this.createdBy = createdBy;
        this.lastModifiedBy = createdBy;
    }

    public void changeCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void changeLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
}
