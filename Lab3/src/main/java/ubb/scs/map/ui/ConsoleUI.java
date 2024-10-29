package ubb.scs.map.ui;

import ubb.scs.map.domain.validators.ServiceException;
import ubb.scs.map.domain.validators.ValidationException;
import ubb.scs.map.service.PrietenieService;
import ubb.scs.map.service.UtilizatorService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleUI {
    private final UtilizatorService utilizatorService;
    private final PrietenieService prietenieService;
    private final Scanner scanner;

    public ConsoleUI(UtilizatorService utilizatorService, PrietenieService prietenieService) {
        this.utilizatorService = utilizatorService;
        this.prietenieService = prietenieService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println();
            System.out.println("1 Adauga utilizator");
            System.out.println("2 Elimina utilizator");
            System.out.println("3 Adauga cerere prietenie");
            System.out.println("4 Elimina prietenie");
            System.out.println("5 Afiseaza numar comunitati");
            System.out.println("6 Afiseaza cea mai sociabila comunitate");
            System.out.println("0 Oprire");
            System.out.print("Optiune: ");
            try {
                int option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        addUtilizator();
                        break;
                    case 2:
                        removeUtilizator();
                        break;
                    case 3:
                        addCererePrietenie();
                        break;
                    case 4:
                        removePrietenie();
                        break;
                    case 5:
                        afisareNumarComunitati();
                        break;
                    case 6:
                        afisareCeaMaiSociabilaComunitate();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Optiune invalida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Optiunea trebuie sa fie un numar.");
                scanner.nextLine();
            }
        }
    }

    private void addUtilizator() {
        try {
            System.out.print("Introdu prenumele: ");
            String firstName = scanner.nextLine();
            System.out.print("Introdu numele de familie: ");
            String lastName = scanner.nextLine();
            utilizatorService.addUtilizator(firstName, lastName);
            System.out.println("Utilizator adaugat.");
        } catch (ValidationException | ServiceException | IllegalArgumentException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    private void removeUtilizator() {
        try {
            System.out.print("Introdu ID-ul utilizatorului de eliminat: ");
            Long id = scanner.nextLong();
            scanner.nextLine();

            prietenieService.removePrietenii(id);
            utilizatorService.removeUtilizator(id);
            System.out.println("Utilizator eliminat.");
        } catch (ServiceException | IllegalArgumentException e) {
            System.out.println("Eroare: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("ID trebuie sa fie numar.");
            scanner.nextLine();
        }
    }

    private void addCererePrietenie() {
        try {
            System.out.print("ID care trimite cerere de prietenie: ");
            Long id1 = scanner.nextLong();
            System.out.print("ID caruia ii e adresata: ");
            Long id2 = scanner.nextLong();
            scanner.nextLine();

            prietenieService.addCererePrietenie(id1, id2);
            System.out.println("Cerere de prietenie adaugata.");
        } catch (ValidationException | ServiceException | IllegalArgumentException e) {
            System.out.println("Eroare: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("ID trebuie sa fie numar.");
            scanner.nextLine();
        }
    }

    private void removePrietenie() {
        try {
            System.out.print("ID care elimina o (cerere de) prietenie: ");
            Long id1 = scanner.nextLong();
            System.out.print("ID care e eliminat ca prieten: ");
            Long id2 = scanner.nextLong();
            scanner.nextLine();

            prietenieService.removePrietenie(id1, id2);
            System.out.println("Prietenie eliminata.");
        } catch (ServiceException | IllegalArgumentException e) {
            System.out.println("Eroare: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("ID trebuie sa fie numar.");
            scanner.nextLine();
        }
    }

    private void afisareNumarComunitati() {
        System.out.println("Numarul de comunitati este: " + prietenieService.getNumarComunitati());
    }

    private void afisareCeaMaiSociabilaComunitate() {
        System.out.println("Cea mai sociabila comunitate este formata din utilizatorii: " + prietenieService.getCeaMaiSociabilaComunitate());
    }
}