package ru.khimich.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.khimich.deal.entity.Credit;

public interface CreditRepository extends JpaRepository<Credit, Long> {
}
