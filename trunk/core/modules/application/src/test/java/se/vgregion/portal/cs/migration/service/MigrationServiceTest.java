package se.vgregion.portal.cs.migration.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import se.vgregion.portal.cs.domain.UserSiteCredential;
import se.vgregion.portal.cs.util.AesCtrCryptoUtilImpl;
import se.vgregion.portal.cs.util.CryptoUtilImpl;

import java.io.File;
import java.net.URL;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Patrik Bergström
 */
@ContextConfiguration({
        "classpath:migration-jpa-infrastructure-test.xml",
        "classpath:crypto-util-test.xml"})
public class MigrationServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    private String keyFilePath;

    @Value("${keyFilePath}")
    public void setKeyFilePath(String keyFilePath) {
        this.keyFilePath = keyFilePath;
    }

    @Autowired
    private MigrationService migrationService;
    @Autowired
    private CryptoUtilImpl ecbCryptoUtil;
    private AesCtrCryptoUtilImpl ctrCryptoUtil;

    private final String password = "v3ryS#Cret";

    @Before
    public void setUp() throws Exception {
        ctrCryptoUtil = new AesCtrCryptoUtilImpl();
        URL resource = this.getClass().getClassLoader().getResource(keyFilePath);
        ctrCryptoUtil.setKeyFile(new File(resource.getPath()));

        migrationService.setCtrCryptoUtil(ctrCryptoUtil);
    }

    @Test
    public void testMigrateEcbToCtr() throws Exception {
        String enc1 = ecbCryptoUtil.encrypt(password);

        UserSiteCredential usc1 = new UserSiteCredential("uid", "asdf");

        usc1.setSitePassword(enc1);
        migrationService.merge(usc1);

        migrationService.migrateEcbToCtr();

        Collection<UserSiteCredential> all = migrationService.findAll();
        UserSiteCredential credential = all.iterator().next();

        assertEquals(password, ctrCryptoUtil.decrypt(credential.getSitePassword()));
    }

    @Test
    public void testMigrateCtr2Ecb() throws Exception {
        //this may be done after the db is migrated to ctr from ecb so we do testMigrateEcbToCtr first
        testMigrateEcbToCtr();

        migrationService.migrateCtr2Ecb();

        Collection<UserSiteCredential> all = migrationService.findAll();
        UserSiteCredential credential = all.iterator().next();

        assertEquals(password, ecbCryptoUtil.decrypt(credential.getSitePassword()));
    }

    @Test
    public void testMigrateAndUpdateKey() throws Exception {
        //this may be done after we have migrated to ctr so we do testMigrateEcbToCtr first
        testMigrateEcbToCtr();

        File file = migrationService.migrateAndUpdateKey();

        assertTrue(file.exists());

        //fetch the user
        Collection<UserSiteCredential> all = migrationService.findAll();
        UserSiteCredential credential = all.iterator().next();

        //verify we can decrypt with the new key
        AesCtrCryptoUtilImpl newCryptoUtil = new AesCtrCryptoUtilImpl();
        newCryptoUtil.setKeyFile(file);

        String decrypt = newCryptoUtil.decrypt(credential.getSitePassword());

        assertEquals(password, decrypt);

        //leave no trace after the test but we need the file for testUndoMigrateAndUpdateKey
        file.deleteOnExit();
    }

    @Test
    public void testUndoMigrateAndUpdateKey() throws Exception {
        //here we need to do have an updated db and key
        testMigrateAndUpdateKey();

        migrationService.undoMigrateAndUpdateKey();

        //fetch the user
        Collection<UserSiteCredential> all = migrationService.findAll();
        UserSiteCredential credential = all.iterator().next();

        //verify we can decrypt with our "old" AesCtrCryptoUtilImpl
        String decrypt = ctrCryptoUtil.decrypt(credential.getSitePassword());

        assertEquals(password, decrypt);
    }

}
