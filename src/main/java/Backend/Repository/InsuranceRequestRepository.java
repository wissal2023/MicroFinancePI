package Backend.Repository;

import Backend.Entity.Contract;
import Backend.Entity.InsuranceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceRequestRepository extends JpaRepository<InsuranceRequest,Long> {
}
