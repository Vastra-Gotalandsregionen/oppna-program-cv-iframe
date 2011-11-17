package se.vgregion.portal.cs.migration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import se.vgregion.portal.cs.migration.service.MigrationService;
import se.vgregion.portal.cs.util.AesCtrCryptoUtilImpl;

import java.io.File;
import java.util.logging.Logger;

/**
 * This is the starting point of a standalone application used for migration of cipher texts from one cipher to
 * another or from one key to another.
 *
 * @author Patrik Bergström
 */
public class PasswordEncryptionMigrator {

    private static final Logger LOGGER = Logger.getLogger(PasswordEncryptionMigrator.class.getName());

    private MigrationService migrationService;

    /**
     * Main method which performs a given migration of cipher texts.
     *
     * @param args Takes exactly one argument which should equal "ecb2ctr", "ctr2ecb", "updatekey" or
     * "undoupdatekey".
     */
    public static void main(String[] args) {

        LOGGER.info("Starting...");

        if (args.length != 1 || !(args[0].equalsIgnoreCase("ecb2ctr") || args[0].equalsIgnoreCase("ctr2ecb")
                || args[0].equalsIgnoreCase("updatekey") || args[0].equalsIgnoreCase("undoupdatekey"))) {
            System.out.println("Add \"ecb2ctr\", \"ctr2ecb\", \"updateKey\" or \"undoUpdateKey\" as input "
                    + "parameter");
            System.exit(0);
        }
        String cmd = args[0];

        PasswordEncryptionMigrator migrator = setupApplicationContext();

        //Do it
        if (cmd.equalsIgnoreCase("ecb2ctr")) {
            migrator.migrateEcbToCtr();
        } else if (cmd.equalsIgnoreCase("ctr2ecb")) {
            migrator.migrateCtr2Ecb();
        } else if (cmd.equalsIgnoreCase("updatekey")) {
            File newKeyFile = migrator.migrateAndUpdateKey();
            System.out.println("New key file: " + newKeyFile.getAbsolutePath());
        } else if (cmd.equalsIgnoreCase("undoupdatekey")) {
            migrator.undoMigrateAndUpdateKey();
        } else {
            throw new IllegalArgumentException("wrong arguments");
        }
    }

    private static PasswordEncryptionMigrator setupApplicationContext() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                "classpath:migration-jpa-infrastructure.xml",
                "classpath:crypto-util.xml");

        MigrationService service = ctx.getBean(MigrationService.class);

        String path = PasswordEncryptionMigrator.class.getClassLoader().getResource(service.getKeyFilePath())
                .getPath();
        AesCtrCryptoUtilImpl aesCtrCryptoUtil = new AesCtrCryptoUtilImpl();
        aesCtrCryptoUtil.setKeyFile(new File(path));

        service.setCtrCryptoUtil(aesCtrCryptoUtil);

        PasswordEncryptionMigrator migrator = new PasswordEncryptionMigrator();
        migrator.setMigrationService(service);
        return migrator;
    }

    private void undoMigrateAndUpdateKey() {
        migrationService.undoMigrateAndUpdateKey();
    }

    private File migrateAndUpdateKey() {
        return migrationService.migrateAndUpdateKey();
    }

    private void migrateEcbToCtr() {
        migrationService.migrateEcbToCtr();
    }

    private void migrateCtr2Ecb() {
        migrationService.migrateCtr2Ecb();
    }

    public void setMigrationService(MigrationService credentialService) {
        this.migrationService = credentialService;
    }
}
