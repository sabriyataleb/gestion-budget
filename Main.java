import java.util.Scanner;

class SousBudget {
    private String nom;
    private double montantInitial;
    private double montantRestant;

    public SousBudget(String nom, double montantInitial) {
        this.nom = nom;
        this.montantInitial = montantInitial;
        this.montantRestant = montantInitial;
    }

    public String getNom() {
        return nom;
    }

    public double getMontantInitial() {
        return montantInitial;
    }

    public double getMontantRestant() {
        return montantRestant;
    }

    public boolean enregistrerDepense(double montant) {
        if (montant <= 0) {
            System.out.println("La dépense doit être supérieure à 0.");
            return false;
        }
        if (montant > montantRestant) {
            System.out.println("Budget restant insuffisant pour cette dépense !");
            return false;
        }
        montantRestant -= montant;
        return true;
    }
}

class Budget {
    private double budgetTotal;
    private SousBudget[] sousBudgets = new SousBudget[10];
    private int nbSousBudgets = 0;

    public Budget(double budgetTotal) {
        this.budgetTotal = budgetTotal;
    }

    private double totalSousBudgets() {
        double total = 0;
        for (int i = 0; i < nbSousBudgets; i++) {
            total += sousBudgets[i].getMontantInitial();
        }
        return total;
    }

    public void ajouterSousBudget(String nom, double montant) {
        if (nom.trim().isEmpty()) {
            System.out.println("Le nom ne peut pas être vide.");
            return;
        }
        if (montant <= 0) {
            System.out.println("Le montant doit être supérieur à 0.");
            return;
        }
        if (trouverSousBudget(nom) != null) {
            System.out.println("Un sous-budget portant ce nom existe déjà.");
            return;
        }
        if (nbSousBudgets >= sousBudgets.length) {
            System.out.println("Limite maximale de sous-budgets atteinte.");
            return;
        }

        double totalActuel = totalSousBudgets();

        if (totalActuel + montant > budgetTotal) {
            System.out.println("Ajout du sous-budget impossible : dépassement du budget total.");
            System.out.printf("Montant restant disponible : %.2f €%n", budgetTotal - totalActuel);
            return;
        }

        sousBudgets[nbSousBudgets] = new SousBudget(nom, montant);
        nbSousBudgets++;
        System.out.println("Sous-budget ajouté !");
    }

    public void supprimerSousBudget(String nom) {
        for (int i = 0; i < nbSousBudgets; i++) {
            if (sousBudgets[i].getNom().equalsIgnoreCase(nom)) {
                for (int j = i; j < nbSousBudgets - 1; j++) {
                    sousBudgets[j] = sousBudgets[j + 1];
                }

                sousBudgets[nbSousBudgets - 1] = null;
                nbSousBudgets--;

                System.out.println("Sous-budget supprimé.");
                return;
            }
        }
        System.out.println("Sous-budget introuvable.");
    }

    public SousBudget trouverSousBudget(String nom) {
        for (int i = 0; i < nbSousBudgets; i++) {
            if (sousBudgets[i].getNom().equalsIgnoreCase(nom)) {
                return sousBudgets[i];
            }
        }
        return null;
    }

    public void afficherSousBudgets() {
        System.out.println("LISTE DES SOUS-BUDGETS");

        if (nbSousBudgets == 0) {
            System.out.println("Aucun sous-budget créé.");
            return;
        }

        for (int i = 0; i < nbSousBudgets; i++) {
            SousBudget sb = sousBudgets[i];
            System.out.printf("Catégorie : %s | Montant initial : %.2f € | Montant restant : %.2f €%n",
                    sb.getNom(), sb.getMontantInitial(), sb.getMontantRestant());
        }
    }
}

public class Main {

    // Lit un nombre décimal en redemandant tant que la saisie est invalide
    private static double lireDouble(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            try {
                return Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
        }
    }

    // Lit un entier en redemandant tant que la saisie est invalide
    private static int lireEntier(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre entier valide.");
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        double budgetTotal;
        do {
            budgetTotal = lireDouble(scanner, "Entrez votre budget total : ");
            if (budgetTotal <= 0) {
                System.out.println("Le budget total doit être supérieur à 0.");
            }
        } while (budgetTotal <= 0);

        Budget budget = new Budget(budgetTotal);

        int choix = -1;

        while (choix != 0) {
            System.out.println("\n======= MENU =======");
            System.out.println("1 - Ajouter un sous-budget");
            System.out.println("2 - Supprimer un sous-budget");
            System.out.println("3 - Enregistrer une dépense");
            System.out.println("4 - Afficher les sous-budgets");
            System.out.println("0 - Quitter");

            choix = lireEntier(scanner, "Choix : ");

            switch (choix) {
                case 1:
                    System.out.print("Nom du sous-budget : ");
                    String nom = scanner.nextLine();
                    double montant = lireDouble(scanner, "Montant du sous-budget : ");
                    budget.ajouterSousBudget(nom, montant);
                    break;

                case 2:
                    System.out.print("Nom du sous-budget à supprimer : ");
                    String nomASupprimer = scanner.nextLine();
                    budget.supprimerSousBudget(nomASupprimer);
                    break;

                case 3:
                    System.out.print("Sous-budget concerné : ");
                    String nomRecherche = scanner.nextLine();
                    SousBudget sb = budget.trouverSousBudget(nomRecherche);

                    if (sb != null) {
                        double depense = lireDouble(scanner, "Montant de la dépense : ");
                        if (sb.enregistrerDepense(depense)) {
                            System.out.println("Dépense enregistrée.");
                            System.out.printf("Budget restant dans le sous-budget : %.2f €%n",
                                    sb.getMontantRestant());
                        }
                    } else {
                        System.out.println("Sous-budget introuvable.");
                    }
                    break;

                case 4:
                    budget.afficherSousBudgets();
                    break;

                case 0:
                    System.out.println("Au revoir !");
                    break;

                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }

        scanner.close();
    }
}
