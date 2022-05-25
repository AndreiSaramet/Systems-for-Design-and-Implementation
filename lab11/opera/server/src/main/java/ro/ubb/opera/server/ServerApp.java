package ro.ubb.opera.server;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ro.ubb.opera.common.domain.*;
import ro.ubb.opera.common.domain.validators.*;
import ro.ubb.opera.common.service.*;
import ro.ubb.opera.common.tcp.Message;
import ro.ubb.opera.server.repository.*;
import ro.ubb.opera.server.service.*;
import ro.ubb.opera.server.tcp.TcpServer;

import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class ServerApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("ro.ubb.opera.server.config");

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        TcpServer tcpServer = new TcpServer(executorService, Service.PORT);

        addComposerHandlers(context, executorService, tcpServer);

        addOperaHandlers(context, executorService, tcpServer);

        addOrchestraHandlers(context, executorService, tcpServer);

        addVenueHandlers(context, executorService, tcpServer);

        addRehearsalHandlers(context, executorService, tcpServer);

        tcpServer.startServer();

        executorService.shutdown();

        context.close();
    }

    private static void addComposerHandlers(AnnotationConfigApplicationContext context, ExecutorService executorService, TcpServer tcpServer) {
        Validator<Composer> composerValidator = new ComposerValidator();
        Repository<Integer, Composer> composerRepository = context.getBean(ComposerRepository.class);
        ComposerService composerService = new ComposerServiceImpl(executorService, composerValidator, composerRepository);

        tcpServer.addHandler("addComposer", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 3) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            Future<Composer> result = composerService.addComposer(stringTokenizer.nextToken(), stringTokenizer.nextToken(), stringTokenizer.nextToken());
            try {
                if (result.get() == null) {
                    return new Message(Message.ERROR, "the composer was not added");
                }
                return new Message(Message.OK, ComposerService.encodeComposer(result.get()));
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("findComposerById", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 1) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            try {
                Future<Composer> result = composerService.findComposerById(Integer.parseInt(stringTokenizer.nextToken()));
                return new Message(Message.OK, ComposerService.encodeComposer(result.get()));
            } catch (NumberFormatException e) {
                return new Message(Message.ERROR, "the given id is not an integer");
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("findComposerByName", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 1) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            Future<Composer> result = composerService.findComposerByName(stringTokenizer.nextToken());
            try {
                return new Message(Message.OK, ComposerService.encodeComposer(result.get()));
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("findComposersByNationality", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 1) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            Future<Set<Composer>> result = composerService.findComposersByNationality(stringTokenizer.nextToken());
            try {
                return new Message(Message.OK, result.get().stream().map(ComposerService::encodeComposer).collect(Collectors.joining(Service.ENTITIES_DELIMITER)));
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("findComposersByMusicalPeriod", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 1) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            Future<Set<Composer>> result = composerService.findComposersByMusicalPeriod(stringTokenizer.nextToken());
            try {
                return new Message(Message.OK, result.get().stream().map(ComposerService::encodeComposer).collect(Collectors.joining(Service.ENTITIES_DELIMITER)));
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("getAllComposers", request -> {
            Future<Set<Composer>> result = composerService.getAllComposers();
            try {
                return new Message(Message.OK, result.get().stream().map(ComposerService::encodeComposer).collect(Collectors.joining(Service.ENTITIES_DELIMITER)));
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });


        tcpServer.addHandler("updateComposer", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 4) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            try {
                Future<Composer> result = composerService.updateComposer(Integer.parseInt(stringTokenizer.nextToken()), stringTokenizer.nextToken(), stringTokenizer.nextToken(), stringTokenizer.nextToken());
                if (result.get() == null) {
                    return new Message(Message.ERROR, "there is no composer with thw given id");
                }
                return new Message(Message.OK, ComposerService.encodeComposer(result.get()));
            } catch (NumberFormatException e) {
                return new Message(Message.ERROR, "the given id is not an integer");
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }

        });

        tcpServer.addHandler("deleteComposer", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 1) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            try {
                Future<Composer> result = composerService.deleteComposer(Integer.parseInt(stringTokenizer.nextToken()));
                if (result.get() == null) {
                    return new Message(Message.ERROR, "there is no composer with thw given id");
                }
                return new Message(Message.OK, ComposerService.encodeComposer(result.get()));
            } catch (NumberFormatException e) {
                return new Message(Message.ERROR, "the given id is not an integer");
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });
    }

    private static void addOperaHandlers(AnnotationConfigApplicationContext context, ExecutorService executorService, TcpServer tcpServer) {
        Validator<Opera> operaValidator = new OperaValidator();
        Repository<Integer, Opera> operaRepository = context.getBean(OperaRepository.class);
        OperaService operaService = new OperaServiceImpl(executorService, operaValidator, operaRepository);

        tcpServer.addHandler("addOpera", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 3) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            try {
                Future<Opera> result = operaService.addOpera(stringTokenizer.nextToken(), stringTokenizer.nextToken(), Integer.parseInt(stringTokenizer.nextToken()));
                if (result.get() == null) {
                    return new Message(Message.ERROR, "the opera was not added");
                }
                return new Message(Message.OK, OperaService.encodeOpera(result.get()));
            } catch (NumberFormatException e) {
                return new Message(Message.ERROR, "the given composer id is not an integer");
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("findOperaById", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 1) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            try {
                Future<Opera> result = operaService.findOperaById(Integer.parseInt(stringTokenizer.nextToken()));
                return new Message(Message.OK, OperaService.encodeOpera(result.get()));
            } catch (NumberFormatException e) {
                return new Message(Message.ERROR, "the given composer id is not an integer");
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("findOperaByTitle", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 1) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            try {
                Future<Opera> result = operaService.findOperaByTitle(stringTokenizer.nextToken());
                return new Message(Message.OK, OperaService.encodeOpera(result.get()));
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("findOperasByLanguage", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 1) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            Future<Set<Opera>> result = operaService.findOperasByLanguage(stringTokenizer.nextToken());
            try {
                return new Message(Message.OK, result.get().stream().map(OperaService::encodeOpera).collect(Collectors.joining(Service.ENTITIES_DELIMITER)));
            } catch (NumberFormatException e) {
                return new Message(Message.ERROR, "the given composer id is not an integer");
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("findOperasByComposer", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 1) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            try {
                Future<Set<Opera>> result = operaService.findOperasByComposer(Integer.parseInt(stringTokenizer.nextToken()));
                return new Message(Message.OK, result.get().stream().map(OperaService::encodeOpera).collect(Collectors.joining(Service.ENTITIES_DELIMITER)));
            } catch (NumberFormatException e) {
                return new Message(Message.ERROR, "the given composer id is not an integer");
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("getAllOperas", request -> {
            Future<Set<Opera>> result = operaService.getAllOperas();
            try {
                return new Message(Message.OK, result.get().stream().map(OperaService::encodeOpera).collect(Collectors.joining(Service.ENTITIES_DELIMITER)));
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("updateOpera", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 4) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            try {
                Future<Opera> result = operaService.updateOpera(Integer.parseInt(stringTokenizer.nextToken()), stringTokenizer.nextToken(), stringTokenizer.nextToken(), Integer.parseInt(stringTokenizer.nextToken()));
                if (result.get() == null) {
                    return new Message(Message.ERROR, "there is no opera withe the given id");
                }
                return new Message(Message.OK, OperaService.encodeOpera(result.get()));
            } catch (NumberFormatException e) {
                return new Message(Message.ERROR, "the given id or composer id is not an integer");
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("deleteOpera", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 1) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            try {
                Future<Opera> result = operaService.deleteOpera(Integer.parseInt(stringTokenizer.nextToken()));
                if (result.get() == null) {
                    return new Message(Message.ERROR, "there is no opera with the given id");
                }
                return new Message(Message.OK, OperaService.encodeOpera(result.get()));
            } catch (NumberFormatException e) {
                return new Message(Message.ERROR, "the given id or composer id is not an integer");
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });
    }

    private static void addOrchestraHandlers(AnnotationConfigApplicationContext context, ExecutorService executorService, TcpServer tcpServer) {
        Validator<Orchestra> orchestraValidator = new OrchestraValidator();
        Repository<Integer, Orchestra> orchestraRepository = context.getBean(OrchestraRepository.class);
        OrchestraService orchestraService = new OrchestraServiceImpl(executorService, orchestraValidator, orchestraRepository);

        tcpServer.addHandler("addOrchestra", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 2) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            Future<Orchestra> result = orchestraService.addOrchestra(stringTokenizer.nextToken(), stringTokenizer.nextToken());
            try {
                if (result.get() == null) {
                    return new Message(Message.ERROR, "the orchestra was not added");
                }
                return new Message(Message.OK, OrchestraService.encodeOrchestra(result.get()));
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("findOrchestraById", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 1) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            try {
                Future<Orchestra> result = orchestraService.findOrchestraById(Integer.parseInt(stringTokenizer.nextToken()));
                return new Message(Message.OK, OrchestraService.encodeOrchestra(result.get()));
            } catch (NumberFormatException e) {
                return new Message(Message.ERROR, "the given id is not an integer");
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("findOrchestraByName", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 1) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            Future<Orchestra> result = orchestraService.findOrchestraByName(stringTokenizer.nextToken());
            try {
                return new Message(Message.OK, OrchestraService.encodeOrchestra(result.get()));
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("findOrchestrasByConductor", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 1) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            Future<Set<Orchestra>> result = orchestraService.findOrchestrasByConductor(stringTokenizer.nextToken());
            try {
                return new Message(Message.OK, result.get().stream().map(OrchestraService::encodeOrchestra).collect(Collectors.joining(Service.ENTITIES_DELIMITER)));
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("getAllOrchestras", request -> {
            Future<Set<Orchestra>> result = orchestraService.getAllOrchestras();
            try {
                return new Message(Message.OK, result.get().stream().map(OrchestraService::encodeOrchestra).collect(Collectors.joining(Service.ENTITIES_DELIMITER)));
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("updateOrchestra", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 3) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            try {
                Future<Orchestra> result = orchestraService.updateOrchestra(Integer.parseInt(stringTokenizer.nextToken()), stringTokenizer.nextToken(), stringTokenizer.nextToken());
                if (result.get() == null) {
                    return new Message(Message.ERROR, "there is no orchestra with the given id");
                }
                return new Message(Message.OK, OrchestraService.encodeOrchestra(result.get()));
            } catch (NumberFormatException e) {
                return new Message(Message.ERROR, "the given id is not an integer");
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("deleteOrchestra", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 1) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            try {
                Future<Orchestra> result = orchestraService.deleteOrchestra(Integer.parseInt(stringTokenizer.nextToken()));
                if (result.get() == null) {
                    return new Message(Message.ERROR, "there is no orchestra with the given id");
                }
                return new Message(Message.OK, OrchestraService.encodeOrchestra(result.get()));
            } catch (NumberFormatException e) {
                return new Message(Message.ERROR, "the given id is not an integer");
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });
    }

    private static void addVenueHandlers(AnnotationConfigApplicationContext context, ExecutorService executorService, TcpServer tcpServer) {
        Validator<Venue> venueValidator = new VenueValidator();
        Repository<Integer, Venue> venueRepository = context.getBean(VenueRepository.class);
        VenueService venueService = new VenueServiceImpl(executorService, venueValidator, venueRepository);

        tcpServer.addHandler("addVenue", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 2) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            try {
                Future<Venue> result = venueService.addVenue(Integer.parseInt(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()));
                if (result.get() == null) {
                    return new Message(Message.ERROR, "the venue was not added");
                }
                return new Message(Message.OK, VenueService.encodeVenue(result.get()));
            } catch (NumberFormatException e) {
                return new Message(Message.ERROR, "the given id is not an integer");
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("findVenueById", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 1) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            try {
                Future<Venue> result = venueService.findVenueById(Integer.parseInt(stringTokenizer.nextToken()));
                return new Message(Message.OK, VenueService.encodeVenue(result.get()));
            } catch (NumberFormatException e) {
                return new Message(Message.ERROR, "the given id is not an integer");
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("findVenuesBySeatsNo", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 1) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            try {
                Future<Set<Venue>> result = venueService.findVenuesBySeatsNo(Integer.parseInt(stringTokenizer.nextToken()));
                return new Message(Message.OK, result.get().stream().map(VenueService::encodeVenue).collect(Collectors.joining(Service.ENTITIES_DELIMITER)));
            } catch (NumberFormatException e) {
                return new Message(Message.ERROR, "the given id is not an integer");
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("findVenuesByFloor", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 1) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            try {
                Future<Set<Venue>> result = venueService.findVenuesByFloor(Integer.parseInt(stringTokenizer.nextToken()));
                return new Message(Message.OK, result.get().stream().map(VenueService::encodeVenue).collect(Collectors.joining(Service.ENTITIES_DELIMITER)));
            } catch (NumberFormatException e) {
                return new Message(Message.ERROR, "the given id is not an integer");
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("getAllVenues", request -> {
            Future<Set<Venue>> result = venueService.getAllVenues();
            try {
                return new Message(Message.OK, result.get().stream().map(VenueService::encodeVenue).collect(Collectors.joining(Service.ENTITIES_DELIMITER)));
            } catch (NumberFormatException e) {
                return new Message(Message.ERROR, "the given id is not an integer");
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("updateVenue", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 3) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            try {
                Future<Venue> result = venueService.updateVenue(Integer.parseInt(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()));
                if (result.get() == null) {
                    return new Message(Message.ERROR, "there is no venue with the given id");
                }
                return new Message(Message.OK, VenueService.encodeVenue(result.get()));
            } catch (NumberFormatException e) {
                return new Message(Message.ERROR, "the given id is not an integer");
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("deleteVenue", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 1) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            try {
                Future<Venue> result = venueService.deleteVenue(Integer.parseInt(stringTokenizer.nextToken()));
                if (result.get() == null) {
                    return new Message(Message.ERROR, "there is no venue with the given id");
                }
                return new Message(Message.OK, VenueService.encodeVenue(result.get()));
            } catch (NumberFormatException e) {
                return new Message(Message.ERROR, "the given id is not an integer");
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

    }

    private static void addRehearsalHandlers(AnnotationConfigApplicationContext context, ExecutorService executorService, TcpServer tcpServer) {
        Validator<Rehearsal> rehearsalValidator = new RehearsalValidator();
        Repository<Integer, Rehearsal> rehearsalRepository = context.getBean(RehearsalRepository.class);
        RehearsalService rehearsalService = new RehearsalServiceImpl(executorService, rehearsalValidator, rehearsalRepository);

        tcpServer.addHandler("addRehearsal", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 2) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            try {
                Future<Rehearsal> result = rehearsalService.addRehearsal(Integer.parseInt(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()));
                if (result.get() == null) {
                    return new Message(Message.ERROR, "the rehearsal was not added");
                }
                return new Message(Message.OK, RehearsalService.encodeRehearsal(result.get()));
            } catch (NumberFormatException e) {
                return new Message(Message.ERROR, "the given id is not an integer");
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("findRehearsalById", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 1) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            try {
                Future<Rehearsal> result = rehearsalService.findRehearsalById(Integer.parseInt(stringTokenizer.nextToken()));
                if (result.get() == null) {
                    return new Message(Message.ERROR, "the rehearsal was not added");
                }
                return new Message(Message.OK, RehearsalService.encodeRehearsal(result.get()));
            } catch (NumberFormatException e) {
                return new Message(Message.ERROR, "the given id is not an integer");
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("findRehearsalsByOrchestra", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 1) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            try {
                Future<Set<Rehearsal>> result = rehearsalService.findRehearsalsByOrchestra(Integer.parseInt(stringTokenizer.nextToken()));
                return new Message(Message.OK, result.get().stream().map(RehearsalService::encodeRehearsal).collect(Collectors.joining(Service.ENTITIES_DELIMITER)));
            } catch (NumberFormatException e) {
                return new Message(Message.ERROR, "the given id is not an integer");
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("findRehearsalsByVenue", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 1) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            try {
                Future<Set<Rehearsal>> result = rehearsalService.findRehearsalsByVenue(Integer.parseInt(stringTokenizer.nextToken()));
                return new Message(Message.OK, result.get().stream().map(RehearsalService::encodeRehearsal).collect(Collectors.joining(Service.ENTITIES_DELIMITER)));
            } catch (NumberFormatException e) {
                return new Message(Message.ERROR, "the given id is not an integer");
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });


        tcpServer.addHandler("getAllRehearsals", request -> {
            Future<Set<Rehearsal>> result = rehearsalService.getAllRehearsals();
            try {
                return new Message(Message.OK, result.get().stream().map(RehearsalService::encodeRehearsal).collect(Collectors.joining(Service.ENTITIES_DELIMITER)));
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("updateRehearsal", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 3) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            try {
                Future<Rehearsal> result = rehearsalService.updateRehearsal(Integer.parseInt(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()));
                if (result.get() == null) {
                    return new Message(Message.ERROR, "there is not rehearsal with the given id");
                }
                return new Message(Message.OK, RehearsalService.encodeRehearsal(result.get()));
            } catch (NumberFormatException e) {
                return new Message(Message.ERROR, "the given id is not an integer");
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler("deleteRehearsal", request -> {
            StringTokenizer stringTokenizer = new StringTokenizer(request.getBody(), Service.TOKENS_DELIMITER);
            if (stringTokenizer.countTokens() != 1) {
                return new Message(Message.ERROR, "too many or not enough parameters provided");
            }
            try {
                Future<Rehearsal> result = rehearsalService.deleteRehearsal(Integer.parseInt(stringTokenizer.nextToken()));
                if (result.get() == null) {
                    return new Message(Message.ERROR, "there is not rehearsal with the given id");
                }
                return new Message(Message.OK, RehearsalService.encodeRehearsal(result.get()));
            } catch (NumberFormatException e) {
                return new Message(Message.ERROR, "the given id is not an integer");
            } catch (InterruptedException | ExecutionException e) {

                return new Message(Message.ERROR, e.getMessage());
            }
        });
    }
}
