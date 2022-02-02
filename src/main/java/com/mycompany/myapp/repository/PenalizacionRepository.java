package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Penalizacion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Penalizacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PenalizacionRepository extends JpaRepository<Penalizacion, Long> {}
