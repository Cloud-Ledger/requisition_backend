package cloudledger.requisition_system.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDate;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
    value = {"created_at", "updated_at"},
    allowGetters = true
)
@Getter
@Setter

public abstract class DateAudit implements Serializable {

  @JsonIgnore
  @CreatedDate
  @Column(name = "CREATED_AT")
  private LocalDate createdAt;

  @JsonIgnore
  @CreatedBy
  @Column(name = "CREATED_BY")
  private String createdBy;


  @JsonIgnore
  @LastModifiedDate
  @Column(name = "UPDATED_AT")
  private LocalDate updatedAt;

  @JsonIgnore
  @LastModifiedBy
  @Column(name = "UPDATED_BY")
  private String updatedBy;


  @JsonIgnore
  @Column(name = "DELETED")
  private  Boolean Deleted=false;

}
