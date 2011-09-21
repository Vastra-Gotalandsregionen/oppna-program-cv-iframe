package se.vgregion.portal.csiframe.domain.persistence.jpa;

import org.springframework.stereotype.Repository;
import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.portal.csiframe.domain.SiteKey;
import se.vgregion.portal.csiframe.domain.persistence.SiteKeyRepository;

/**
 * Jpa implementation of SiteKey Repository.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
@Repository
public class JpaSiteKeyRepository extends DefaultJpaRepository<SiteKey, Long> implements SiteKeyRepository {

    @Override
    public void save(SiteKey siteKey) {
        if (siteKey.getId() == null) {
            entityManager.persist(siteKey);
        } else {
            entityManager.merge(siteKey);
        }
    }
}
