package se.vgregion.portal.cs.domain.persistence.jpa;

import org.springframework.stereotype.Repository;
import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.portal.cs.domain.SiteKey;
import se.vgregion.portal.cs.domain.UserSiteCredential;
import se.vgregion.portal.cs.domain.persistence.SiteKeyRepository;

import javax.persistence.Query;
import java.util.List;

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

    @Override
    public SiteKey findBySiteKey(String siteKey) {
        String queryString = "SELECT s FROM SiteKey s WHERE s.siteKey = :siteKey";
        Query query = entityManager.createQuery(queryString).setParameter("siteKey", siteKey);

        return (SiteKey) query.getSingleResult();
    }
}
