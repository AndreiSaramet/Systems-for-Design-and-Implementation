package ro.ubb.opera.client.ui;

import ro.ubb.opera.client.controller.ClientController;
import ro.ubb.opera.client.controller.exceptions.ControllerException;
import ro.ubb.opera.common.controller.Controller;
import ro.ubb.opera.common.domain.*;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ClientConsole {
    private final ClientController controller;
    private final Scanner scanner;

    public ClientConsole(ClientController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        this.printWelcomeMessage();
        this.runLoop(this::getMainOptions, this::printMainMenu);
        this.printGoodbyeMessage();
    }

    private void runComposerMenu() {
        this.runLoop(this::getComposerOptions, this::printComposerMenu);
    }

    private void runOperaMenu() {
        this.runLoop(this::getOperaOptions, this::printOperaMenu);
    }

    private void runOrchestraMenu() {
        this.runLoop(this::getOrchestraOptions, this::printOrchestraMenu);
    }

    private void runVenueMenu() {
        this.runLoop(this::getVenueOptions, this::printVenueMenu);
    }

    private void runRehearsalMenu() {
        this.runLoop(this::getRehearsalOptions, this::printRehearsalMenu);
    }

    private void runLoop(Callable<Map<Integer, Runnable>> getOptions, Runnable printMenu) {
        try {
            Map<Integer, Runnable> options = getOptions.call();
            while (true) {
                printMenu.run();
                int option = this.getOption(options.size());
                if (option == 0) {
                    return;
                }
                if (option == -1) {
                    this.printErrorMessage();
                    continue;
                }
                options.get(option).run();
            }
        } catch (Exception e) {
            System.out.println("An exception occurred" + e.getMessage());
        }
    }

    private Map<Integer, Runnable> getMainOptions() {
        Map<Integer, Runnable> mainOptions = new HashMap<>();
        mainOptions.put(1, this::runComposerMenu);
        mainOptions.put(2, this::runOperaMenu);
        mainOptions.put(3, this::runOrchestraMenu);
        mainOptions.put(4, this::runVenueMenu);
        mainOptions.put(5, this::runRehearsalMenu);
        return mainOptions;
    }

    private Map<Integer, Runnable> getComposerOptions() {
        Map<Integer, Runnable> options = new HashMap<>();
        options.put(1, this::addComposer);
        options.put(2, this::displayComposers);
        options.put(3, this::displayComposerByName);
        options.put(4, this::displayComposersByNationality);
        options.put(5, this::displayComposersByMusicalPeriod);
        options.put(6, this::updateComposer);
        options.put(7, this::deleteComposer);
        return options;
    }

    private Map<Integer, Runnable> getOperaOptions() {
        Map<Integer, Runnable> options = new HashMap<>();
        options.put(1, this::addOpera);
        options.put(2, this::displayOperas);
        options.put(3, this::displayOperaByTitle);
        options.put(4, this::displayOperasByLanguage);
        options.put(5, this::displayOperasByComposer);
        options.put(6, this::updateOpera);
        options.put(7, this::deleteOpera);
        return options;
    }


    private Map<Integer, Runnable> getOrchestraOptions() {
        Map<Integer, Runnable> options = new HashMap<>();
        options.put(1, this::addOrchestra);
        options.put(2, this::displayOrchestras);
        options.put(3, this::displayOrchestraByName);
        options.put(4, this::displayOrchestrasByConductor);
        options.put(5, this::updateOrchestra);
        options.put(6, this::deleteOrchestra);
        return options;
    }

    private Map<Integer, Runnable> getVenueOptions() {
        Map<Integer, Runnable> options = new HashMap<>();
        options.put(1, this::addVenue);
        options.put(2, this::displayVenues);
        options.put(3, this::displayVenuesBySeatsNo);
        options.put(4, this::displayVenuesByFloor);
        options.put(5, this::updateVenue);
        options.put(6, this::deleteVenue);
        return options;
    }

    private Map<Integer, Runnable> getRehearsalOptions() {
        Map<Integer, Runnable> options = new HashMap<>();
        options.put(1, this::addRehearsal);
        options.put(2, this::displayRehearsals);
        options.put(3, this::displayRehearsalsByOrchestra);
        options.put(4, this::displayRehearsalsByVenue);
        options.put(5, this::updateRehearsal);
        options.put(6, this::deleteRehearsal);
        return options;
    }

    private void printMainMenu() {
        System.out.println("""
                Main MENU
                Options
                1. Composer Menu
                2. Opera Menu
                3. Orchestra Menu
                4. Venue Menu
                5. Rehearsal Menu
                x. Exit the application
                """);
    }

    private void printComposerMenu() {
        System.out.println("""
                Composer MENU
                Options
                1. Add a composer
                2. Display all composers
                3. Display composer with a given name
                4. Display composers by nationality
                5. Display composers by musical period
                6. Update a composer
                7. Delete a composer
                r. Return to Main Menu
                """);
    }

    private void printOperaMenu() {
        System.out.println("""
                Opera MENU
                Options
                1. Add an opera
                2. Display all operas
                3. Display opera with a given title
                4. Display operas by language
                5. Display operas by composer
                6. Update an opera
                7. Delete an opera
                r. Return to Main Menu
                """);
    }

    private void printOrchestraMenu() {
        System.out.println("""
                Orchestra MENU
                Options
                1. Add an orchestra
                2. Display all orchestras
                3. Display orchestra with a given name
                4. Display orchestras by conductor
                5. Update an orchestra
                6. Delete an orchestra
                r. Return to Main Menu
                """);
    }

    private void printVenueMenu() {
        System.out.println("""
                Venue MENU
                Options
                1. Add a venue
                2. Display all venues
                3. Display venues by number of seats
                4. Display venues by floor
                5. Update a venue
                6. Delete a venue
                r. Return to Main Menu
                """);
    }

    private void printRehearsalMenu() {
        System.out.println("""
                Rehearsal MENU
                Options
                1. Add a rehearsal
                2. Display all rehearsals
                3. Display rehearsals by orchestra
                4. Display rehearsals by venue
                5. Update a rehearsal
                6. Delete a rehearsal
                r. Return to Main Menu
                """);
    }

    private void printWelcomeMessage() {
        System.out.println("Welcome to the Opera Management Application");
    }

    private void printGoodbyeMessage() {
        System.out.println("Thank you for using this application!\n" +
                "Have a nice day! Goodbye.");
    }

    private void printErrorMessage() {
        System.out.println("Ypu introduced an INVALID option. Try again!");
    }

    private void printInvalidValue(String objectName) {
        System.out.println("You introduced an invalid value for the " + objectName);
    }

    private int getOption(int maxValue) {
        System.out.print("Your option: ");
        String option = this.scanner.nextLine();
        if ("x".equals(option) || "r".equals(option))
            return 0;
        try {
            int intOption = Integer.parseInt(option);
            if (intOption >= 1 && intOption <= maxValue) {
                return intOption;
            }
            return -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private Integer readId(String entityName) {
        System.out.print(entityName + " id: ");
        try {
            return Integer.parseInt(this.scanner.nextLine());
        } catch (NumberFormatException e) {
            this.printInvalidValue(entityName + " id");
            return this.readId(entityName);
        }
    }

    private String readName(String entityName) {
        System.out.print(entityName + " name: ");
        return this.scanner.nextLine();
    }

    private String readName(String entityName, String currentName) {
        System.out.println("The current " + entityName + "  name is: " + currentName);
        return this.readName(entityName);
    }

    private String readNationality(String entityName) {
        System.out.print(entityName + " nationality: ");
        return this.scanner.nextLine();
    }

    private String readNationality(String entityName, String currentNationality) {
        System.out.println("The current " + entityName + "  nationality is: " + currentNationality);
        return this.readNationality(entityName);
    }

    private String readMusicalPeriod(String entityName) {
        System.out.print(entityName + " musical period: ");
        return this.scanner.nextLine();
    }

    private String readMusicalPeriod(String entityName, String currentMusicalPeriod) {
        System.out.println("The current " + entityName + "  musical period is: " + currentMusicalPeriod);
        return this.readMusicalPeriod(entityName);
    }

    private String readConductor(String entityName) {
        System.out.print(entityName + " conductor name: ");
        return this.scanner.nextLine();
    }

    private String readConductor(String entityName, String currentConductorName) {
        System.out.println("The current " + entityName + " conductor name is: " + currentConductorName);
        return this.readConductor(entityName);
    }

    private Integer readNoSeats(String entityName) {
        System.out.print(entityName + " number of seats: ");
        try {
            return Integer.parseInt(this.scanner.nextLine());
        } catch (NumberFormatException e) {
            this.printInvalidValue(entityName + " number of seats");
            return readNoSeats(entityName);
        }
    }

    private Integer readComposerId(String entityName) {
        this.controller.getAllComposers().forEach(System.out::println);
        return this.readId(entityName + " composer id: ");
    }

    private Integer readComposerId(String entityName, Integer currentComposerId) {
        System.out.println("The current " + entityName + " composer is: \n" + this.controller.findComposerById(currentComposerId));
        return this.readComposerId(entityName);
    }

    private Integer readOrchestraId(String entityName) {
        this.controller.getAllOrchestras().forEach(System.out::println);
        return this.readId(entityName + " orchestra id: ");
    }

    private Integer readOrchestraId(String entityName, Integer currentOrchestraId) {
        System.out.println("The current " + entityName + " orchestra is: \n" + this.controller.findOrchestraById(currentOrchestraId));
        return this.readOrchestraId(entityName);
    }

    private Integer readVenueId(String entityName) {
        this.controller.getAllVenues().forEach(System.out::println);
        return this.readId(entityName + " venue id: ");
    }

    private Integer readVenueId(String entityName, Integer currentVenueId) {
        System.out.println("The current " + entityName + " venue is: \n" + this.controller.findVenueById(currentVenueId));
        return this.readVenueId(entityName);
    }

    private Integer readNoSeats(String entityName, Integer currentNoSeats) {
        System.out.println("The current " + entityName + " number of seats is: " + currentNoSeats.toString());
        return this.readNoSeats(entityName);
    }

    private Integer readFloorNo(String entityName) {
        System.out.print(entityName + " floor number: ");
        try {
            return Integer.parseInt(this.scanner.nextLine());
        } catch (NumberFormatException e) {
            this.printInvalidValue(entityName + " floor number");
            return this.readFloorNo(entityName);
        }
    }

    private Integer readFloorNo(String entityName, Integer currentFloorNo) {
        System.out.println("The current " + entityName + " floor number is: " + currentFloorNo.toString());
        return this.readFloorNo(entityName);
    }

    private String readTitle(String entityName) {
        System.out.print(entityName + " title is: ");
        return this.scanner.nextLine();
    }

    private String readTitle(String entityName, String currentTitle) {
        System.out.println("The current " + entityName + " title is: " + currentTitle);
        return this.readTitle(entityName);
    }

    private String readLanguage(String entityName) {
        System.out.print(entityName + " language is: ");
        return this.scanner.nextLine();
    }

    private String readLanguage(String entityName, String currentLanguage) {
        System.out.println("The current " + entityName + " language is: " + currentLanguage);
        return this.readLanguage(entityName);
    }

    private void addComposer() {
        try {
            Composer composer = this.controller.addComposer(this.readName(Controller.COMPOSER_ENTITY_NAME), this.readNationality(Controller.COMPOSER_ENTITY_NAME), this.readMusicalPeriod(Controller.COMPOSER_ENTITY_NAME));
            System.out.println("You successfully added the composer: \n" + composer.toString());
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException);
        }
    }

    private void addOpera() {
        try {
            Opera opera = this.controller.addOpera(this.readTitle(Controller.OPERA_ENTITY_NAME), this.readLanguage(Controller.OPERA_ENTITY_NAME), this.readComposerId(Controller.OPERA_ENTITY_NAME));
            System.out.println("You successfully added the opera: \n" + opera.toString());
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException);
        }
    }

    private void addOrchestra() {
        try {
            Orchestra orchestra = this.controller.addOrchestra(this.readName(Controller.ORCHESTRA_ENTITY_NAME), this.readConductor(Controller.ORCHESTRA_ENTITY_NAME));
            System.out.println("You successfully added the orchestra: \n" + orchestra.toString());
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException);
        }
    }

    private void addVenue() {
        try {
            Venue venue = this.controller.addVenue(this.readNoSeats(Controller.VENUE_ENTITY_NAME), this.readFloorNo(Controller.VENUE_ENTITY_NAME));
            System.out.println("You successfully added the venue: \n" + venue.toString());
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException);
        }
    }

    private void addRehearsal() {
        try {
            Rehearsal rehearsal = this.controller.addRehearsal(this.readOrchestraId(Controller.REHEARSAL_ENTITY_NAME), this.readVenueId(Controller.REHEARSAL_ENTITY_NAME));
            System.out.println("You successfully added the rehearsal: \n" + rehearsal.toString());
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException);
        }
    }

    private void displayComposers() {
        try {
            System.out.println(this.controller.getAllComposers().stream().map(Objects::toString).collect(Collectors.joining("\n")));
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException);
        }
    }

    private void displayOperas() {
        try {
            System.out.println(this.controller.getAllOperas().stream().map(pair -> pair.getKey().toString() + "\n<->\n" + pair.getValue().toString()).collect(Collectors.joining("\n")));
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException);
        }
    }

    private void displayOrchestras() {
        try {
            System.out.println(this.controller.getAllOrchestras().stream().map(Objects::toString).collect(Collectors.joining("\n")));
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException);
        }
    }

    private void displayVenues() {
        try {
            System.out.println(this.controller.getAllVenues().stream().map(Objects::toString).collect(Collectors.joining("\n")));
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException);
        }
    }

    private void displayRehearsals() {
        try {
            System.out.println(this.controller.getAllRehearsals().stream().map(pair -> pair.getKey().toString() + "\n<->\n" + pair.getValue().toString()).collect(Collectors.joining("\n")));
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException);
        }
    }

    private void displayComposerByName() {
        String name = this.readName(Controller.COMPOSER_ENTITY_NAME);
        try {
            Composer composer = this.controller.findComposerByName(name);
            if (composer == null) {
                System.out.println("There does not exist a composer with the given name");
            } else {
                System.out.println(composer);
            }
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException);
        }
    }

    private void displayOperaByTitle() {
        String title = this.readTitle(Controller.OPERA_ENTITY_NAME);
        try {
            Map.Entry<Opera, Composer> opera = this.controller.findOperaByTitle(title);
            if (opera == null) {
                System.out.println("There does not exist an opera with the given title");
            } else {
                System.out.println(opera.getKey() + "\n<->\n" + opera.getValue());
            }
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException);
        }

    }

    private void displayOrchestraByName() {
        String name = this.readName(Controller.ORCHESTRA_ENTITY_NAME);
        try {
            Orchestra orchestra = this.controller.findOrchestraByName(name);
            if (orchestra == null) {
                System.out.println("There does not exist an orchestra with the given name");
            } else {
                System.out.println(orchestra);
            }
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException);
        }
    }

    private void displayComposersByNationality() {
        String nationality = this.readNationality(Controller.COMPOSER_ENTITY_NAME);
        try {
            System.out.println(this.controller.findComposersByNationality(nationality).stream().map(Objects::toString).collect(Collectors.joining("\n")));
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred" + controllerException);
        }
    }

    private void displayComposersByMusicalPeriod() {
        String musicalPeriod = this.readMusicalPeriod(Controller.COMPOSER_ENTITY_NAME);
        try {
            System.out.println(this.controller.findComposersByMusicalPeriod(musicalPeriod).stream().map(Objects::toString).collect(Collectors.joining("\n")));
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException);
        }
    }

    private void displayOperasByLanguage() {
        String language = this.readLanguage(Controller.OPERA_ENTITY_NAME);
        try {
            System.out.println(this.controller.findOperasByLanguage(language).stream().map(pair -> pair.getKey().toString() + "\n<->\n" + pair.getValue().toString()).collect(Collectors.joining("\n")));
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException);
        }
    }

    private void displayOrchestrasByConductor() {
        String conductor = this.readConductor(Controller.ORCHESTRA_ENTITY_NAME);
        try {
            System.out.println(this.controller.findOrchestrasByConductor(conductor).stream().map(Objects::toString).collect(Collectors.joining("\n")));
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException);
        }
    }

    private void displayVenuesBySeatsNo() {
        Integer seatsNo = this.readNoSeats(Controller.VENUE_ENTITY_NAME);
        try {
            System.out.println(this.controller.findVenuesBySeatsNo(seatsNo).stream().map(Objects::toString).collect(Collectors.joining("\n")));
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException);
        }
    }

    private void displayVenuesByFloor() {
        Integer floor = this.readFloorNo(Controller.VENUE_ENTITY_NAME);
        try {
            System.out.println(this.controller.findVenuesByFloor(floor).stream().map(Objects::toString).collect(Collectors.joining("\n")));
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException);
        }
    }

    private void displayOperasByComposer() {
        Integer composerId = this.readComposerId(Controller.OPERA_ENTITY_NAME);
        try {
            System.out.println(this.controller.findOperasByComposer(composerId).getKey() + "\n->\n" + this.controller.findOperasByComposer(composerId).getValue().stream().map(Objects::toString).collect(Collectors.joining("\n")));
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred" + controllerException);
        }
    }

    private void displayRehearsalsByOrchestra() {
        Integer orchestraId = this.readOrchestraId(Controller.REHEARSAL_ENTITY_NAME);
        try {
            System.out.println(this.controller.findRehearsalsByOrchestra(orchestraId).getKey() + "\n->\n" + this.controller.findRehearsalsByOrchestra(orchestraId).getValue().stream().map(Objects::toString).collect(Collectors.joining("\n")));
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred" + controllerException);
        }
    }

    private void displayRehearsalsByVenue() {
        Integer venueId = this.readVenueId(Controller.REHEARSAL_ENTITY_NAME);
        try {
            System.out.println(this.controller.findRehearsalsByVenue(venueId).getKey() + "\n->\n" + this.controller.findRehearsalsByVenue(venueId).getValue().stream().map(Objects::toString).collect(Collectors.joining("\n")));
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred" + controllerException);
        }
    }

    private void updateComposer() {
        Integer id = this.readId(Controller.COMPOSER_ENTITY_NAME);
        try {
            Composer composer = this.controller.findComposerById(id);
            composer = this.controller.updateComposer(id, this.readName(Controller.COMPOSER_ENTITY_NAME, composer.getName()), this.readNationality(Controller.COMPOSER_ENTITY_NAME, composer.getNationality()), this.readMusicalPeriod(Controller.COMPOSER_ENTITY_NAME, composer.getMusicalPeriod()));
            System.out.println("You successfully updated the composer: \n" + composer.toString());
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException.getMessage());
        }
    }

    private void updateOpera() {
        Integer id = this.readId(Controller.OPERA_ENTITY_NAME);
        try {
            Opera opera = this.controller.findOperaById(id).getKey();
            opera = this.controller.updateOpera(id, this.readTitle(Controller.OPERA_ENTITY_NAME, opera.getTitle()), this.readLanguage(Controller.OPERA_ENTITY_NAME, opera.getLanguage()), this.readComposerId(Controller.OPERA_ENTITY_NAME, opera.getComposerId()));
            System.out.println("You successfully updated the opera: \n" + opera.toString());
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException.getMessage());
        }
    }

    private void updateOrchestra() {
        Integer id = this.readId(Controller.ORCHESTRA_ENTITY_NAME);
        try {
            Orchestra orchestra = this.controller.findOrchestraById(id);
            orchestra = this.controller.updateOrchestra(id, this.readName(Controller.ORCHESTRA_ENTITY_NAME, orchestra.getName()), this.readConductor(Controller.ORCHESTRA_ENTITY_NAME, orchestra.getConductor()));
            System.out.println("You successfully updated the orchestra: \n" + orchestra.toString());
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException.getMessage());
        }
    }

    private void updateVenue() {
        Integer id = this.readId(Controller.VENUE_ENTITY_NAME);
        try {
            Venue venue = this.controller.findVenueById(id);
            venue = this.controller.updateVenue(id, this.readNoSeats(Controller.VENUE_ENTITY_NAME, venue.getNumberOfSeats()), this.readFloorNo(Controller.VENUE_ENTITY_NAME, venue.getFloor()));
            System.out.println("You successfully updated the venue: \n" + venue.toString());
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException.getMessage());
        }
    }

    private void updateRehearsal() {
        Integer id = this.readId(Controller.REHEARSAL_ENTITY_NAME);
        try {
            Rehearsal rehearsal = new Rehearsal(id, this.controller.findRehearsalById(id).getKey().getId(), this.controller.findRehearsalById(id).getValue().getId());
            rehearsal = this.controller.updateRehearsal(id, this.readOrchestraId(Controller.REHEARSAL_ENTITY_NAME, rehearsal.getOrchestraId()), this.readVenueId(Controller.REHEARSAL_ENTITY_NAME, rehearsal.getVenueId()));
            System.out.println("You successfully updated the rehearsal: \n" + rehearsal.toString());
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException.getMessage());
        }
    }

    private void deleteComposer() {
        Integer id = this.readId(Controller.COMPOSER_ENTITY_NAME);
        try {
            Composer composer = this.controller.deleteComposer(id);
            System.out.println("You successfully deleted the composer: \n" + composer.toString());
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException);
        }
    }

    private void deleteOpera() {
        Integer id = this.readId(Controller.OPERA_ENTITY_NAME);
        try {
            Opera opera = this.controller.deleteOpera(id);
            System.out.println("You successfully deleted the opera: \n" + opera.toString());
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException.getMessage());
        }
    }

    private void deleteOrchestra() {
        Integer id = this.readId(Controller.ORCHESTRA_ENTITY_NAME);
        try {
            Orchestra orchestra = this.controller.deleteOrchestra(id);
            System.out.println("You successfully deleted the orchestra: \n" + orchestra.toString());
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException.getMessage());
        }
    }

    private void deleteVenue() {
        Integer id = this.readId(Controller.VENUE_ENTITY_NAME);
        try {
            Venue venue = this.controller.deleteVenue(id);
            System.out.println("You successfully deleted the venue: \n" + venue.toString());
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException.getMessage());
        }
    }

    private void deleteRehearsal() {
        Integer id = this.readId(Controller.REHEARSAL_ENTITY_NAME);
        try {
            Rehearsal rehearsal = this.controller.deleteRehearsal(id);
            System.out.println("You successfully deleted the rehearsal: \n" + rehearsal.toString());
        } catch (ControllerException controllerException) {
            System.out.println("An exception occurred " + controllerException.getMessage());
        }
    }
}
