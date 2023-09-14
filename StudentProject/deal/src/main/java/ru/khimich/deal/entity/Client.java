package ru.khimich.deal.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.khimich.deal.dto.EmploymentDTO;
import ru.khimich.deal.entity.enums.Gender;
import ru.khimich.deal.entity.enums.MaritalStatus;
import ru.khimich.deal.model.Passport;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long client_id;
    @Column(name = "last_name")
    private String last_name;
    @Column(name = "first_name")
    private String first_name;
    @Column(name = "middle_name")
    private String middle_name;
    @Column(name = "birth_date")
    private LocalDate birth_date;
    @Column(name = "email")
    private String email;
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name = "marital_status")
    @Enumerated(EnumType.STRING)
    private MaritalStatus marital_status;
    @Column(name = "dependent_amount")
    private Integer dependent_amount;
    //@Type(JsonType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "passport", columnDefinition = "jsonb")
    private Passport passport;
    //@Type(JsonType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "employment", columnDefinition = "jsonb")
    //private Employment employment;
    private EmploymentDTO employment;
    @Column(name = "account")
    private String account;
}
