package app.learn.integrations.google;

public class IntegrationService {
    public void addIntegration(Integration integration) {
        IntegrationDAO integrationDAO = new IntegrationDAO();
        integrationDAO.add(integration);
    }

}
