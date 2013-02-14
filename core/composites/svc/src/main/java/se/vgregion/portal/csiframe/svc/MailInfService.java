package se.vgregion.portal.csiframe.svc;

public interface MailInfService {

    /**
     * There are several mail servers that the user might be registred on. This method finds the symbolic name of
     * the server.
     *
     * @param userId
     *            vgrid id that is used as id to lookup what mail server the user have.
     * @return name of the server.
     */
    String findServerName(String userId);

}
