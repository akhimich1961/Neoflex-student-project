package ru.khimich.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.khimich.deal.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
