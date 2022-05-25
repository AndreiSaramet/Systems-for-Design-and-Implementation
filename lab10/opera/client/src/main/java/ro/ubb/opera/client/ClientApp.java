package ro.ubb.opera.client;

import ro.ubb.opera.client.controller.ClientController;
import ro.ubb.opera.client.service.*;
import ro.ubb.opera.client.tcp.TcpClient;
import ro.ubb.opera.client.ui.ClientConsole;
import ro.ubb.opera.common.service.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientApp {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        TcpClient tcpClient = new TcpClient();
        ComposerService composerService = new ComposerServiceClient(executorService, tcpClient);
        OperaService operaService = new OperaServiceClient(executorService, tcpClient);
        OrchestraService orchestraService = new OrchestraServiceClient(executorService, tcpClient);
        VenueService venueService = new VenueServiceClient(executorService, tcpClient);
        RehearsalService rehearsalService = new RehearsalServiceClient(executorService, tcpClient);
        ClientController controller = new ClientController(composerService, operaService, orchestraService, venueService, rehearsalService);
        ClientConsole console = new ClientConsole(controller);

        console.run();

        executorService.shutdown();
    }
}
