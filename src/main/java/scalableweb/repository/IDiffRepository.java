package scalableweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import scalableweb.domain.Diff;

/**
 * {@link JpaRepository} for {@link Diff}s.
 */
@Repository
public interface IDiffRepository extends JpaRepository<Diff, Long> {

}
