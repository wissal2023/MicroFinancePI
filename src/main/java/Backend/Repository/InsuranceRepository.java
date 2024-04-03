package Backend.Repository;

import Backend.Entity.Contract;
import Backend.Entity.Insurance;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceRepository extends JpaRepository<Insurance,Long> {
}
