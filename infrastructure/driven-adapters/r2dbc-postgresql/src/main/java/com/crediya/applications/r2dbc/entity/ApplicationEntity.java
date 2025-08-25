package com.crediya.applications.r2dbc.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("applications")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ApplicationEntity {

  @Id private Long applicationId;
  private Long amount;
  private Integer deadline;
  private String email;
  private Integer applicationStatusId;

  @ManyToOne
  @JoinColumn(name = "loan_type_id")
  private LoanTypeEntity loanType;
}
