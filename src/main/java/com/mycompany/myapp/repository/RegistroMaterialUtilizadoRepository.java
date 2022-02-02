package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.RegistroMaterialUtilizado;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RegistroMaterialUtilizado entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegistroMaterialUtilizadoRepository extends JpaRepository<RegistroMaterialUtilizado, Long> {}
