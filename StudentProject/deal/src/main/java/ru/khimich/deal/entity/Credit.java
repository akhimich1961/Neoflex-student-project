package ru.khimich.deal.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.khimich.deal.dto.PaymentScheduleElement;
import ru.khimich.deal.entity.enums.CreditStatus;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "credit")
public class Credit {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "credit_id")
    private Long credit_id;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "term")
    private Integer term;
    @Column(name = "monthly_payment")
    private BigDecimal monthly_payment;
    @Column(name = "rate")
    private BigDecimal rate;
    @Column(name = "psk")
    private BigDecimal psk;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payment_schedule", columnDefinition = "jsonb")
    private List<PaymentScheduleElement> payment_schedule;
    @Column(name = "insurance_enable")
    private Boolean insurance_enable;
    @Column(name = "salary_client")
    private Boolean salary_client;
    @Column(name = "credit_status")
    @Enumerated(EnumType.STRING)
    private CreditStatus credit_status;
}
