package tobase64.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tobase64.domain.Diff;

/**
 * {@link JpaRepository} for {@link Diff}s.
 */
@Repository
public interface IDiffRepository extends JpaRepository<Diff, Long> {

}
