package ru.khimich.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.khimich.deal.entity.Application;

public interface AppRepository extends JpaRepository<Application, Long> {
}
