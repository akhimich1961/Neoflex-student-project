package ru.khimich.deal.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.khimich.deal.dto.ApplicationStatusHistoryDTO;
import ru.khimich.deal.dto.LoanOfferDTO;
import ru.khimich.deal.entity.enums.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "application")
public class Application {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long application_id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    private Client client_id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_id", referencedColumnName = "credit_id")
    private Credit credit_id;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
    @Column(name = "creation_date")
    private LocalDateTime creation_date;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "applied_offer", columnDefinition = "jsonb")
    private LoanOfferDTO applied_offer;
    @Column(name = "sign_date")
    private LocalDateTime sign_date;
    @Column(name = "ses_code")
    private Integer ses_code;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "status_history", columnDefinition = "jsonb")
    private List<ApplicationStatusHistoryDTO> status_history;
}
