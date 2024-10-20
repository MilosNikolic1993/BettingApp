import java.util.*;

public class BettingApp {
    // Map to store users and their balances
    private static Map<String, Double> users = new HashMap<>();
    private static double userBalance = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Define random teams and their strengths for betting
        Team[] teams = {
                new Team("Red Dragons", 80),
                new Team("Blue Sharks", 75),
                new Team("Golden Eagles", 90),
                new Team("Green Bears", 85),
                new Team("Silver Foxes", 70)
        };

        // User registration
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        registerUser(username);

        // User sets initial balance
        System.out.print("Enter initial amount in account: ");
        userBalance = scanner.nextDouble();

        while (true) {
            System.out.println("\nWelcome to Mozzart Betting!");
            System.out.println("Your current balance: " + userBalance + " dinars");
            System.out.println("Choose a team to bet on:");

            // Display teams and their odds
            for (int i = 0; i < teams.length; i++) {
                System.out.println((i + 1) + ". " + teams[i].getName() + " - Odds: " + calculateOdds(teams[i]));
            }

            // Select team for betting
            int choice;
            do {
                System.out.print("Enter team number (1-5): ");
                choice = scanner.nextInt();
            } while (choice < 1 || choice > 5);

            // Enter bet amount
            double betAmount;
            do {
                System.out.print("Enter bet amount: ");
                betAmount = scanner.nextDouble();
                if (betAmount > userBalance) {
                    System.out.println("You do not have enough funds for this bet!");
                }
            } while (betAmount > userBalance);

            // Simulate match
            Team selectedTeam = teams[choice - 1];
            Match match = new Match(teams[0], teams[1]); // You can expand for random team selection
            boolean win = match.simulate(selectedTeam, betAmount);

            // Update balance based on outcome
            if (win) {
                userBalance += betAmount * calculateOdds(selectedTeam) - betAmount; // Update balance if user wins
                System.out.println("Congratulations! Your new balance is: " + userBalance + " dinars.");
            } else {
                userBalance -= betAmount; // Deduct the bet amount
                System.out.println("Unfortunately, you lost your bet. Your new balance is: " + userBalance + " dinars.");

                // Check if balance is zero
                if (userBalance <= 0) {
                    System.out.println("You do not have enough funds to continue betting.");
                    System.out.print("Would you like to top up your balance? (yes/no): ");
                    String response = scanner.next();
                    if (response.equalsIgnoreCase("yes")) {
                        System.out.print("Enter amount to deposit: ");
                        userBalance += scanner.nextDouble();
                        System.out.println("Your new balance is: " + userBalance + " dinars.");
                    } else {
                        break; // Exit application
                    }
                }
            }

            // Ask if the user wants to continue betting
            System.out.print("Do you want to continue betting? (yes/no): ");
            String continueBetting = scanner.next();
            if (!continueBetting.equalsIgnoreCase("yes")) {
                System.out.println("Thank you for using the application! Have a nice day!");
                break;
            }
        }

        scanner.close();
    }

    // Register the user
    private static void registerUser(String username) {
        if (!users.containsKey(username)) {
            users.put(username, userBalance);
            System.out.println("User " + username + " has been registered.");
        } else {
            System.out.println("User " + username + " already exists.");
        }
    }

    // Calculate odds based on team strength
    private static double calculateOdds(Team team) {
        return 1.5 + (team.getStrength() / 100.0) * 2.5; // Example of odds calculation
    }
}

// Class representing a team
class Team {
    private String name;
    private int strength;

    public Team(String name, int strength) {
        this.name = name;
        this.strength = strength;
    }

    public String getName() {
        return name;
    }

    public int getStrength() {
        return strength;
    }
}

// Class for simulating a match
class Match {
    private Team teamA;
    private Team teamB;

    public Match(Team teamA, Team teamB) {
        this.teamA = teamA;
        this.teamB = teamB;
    }

    // Simulate the match and determine the winner
    public boolean simulate(Team selectedTeam, double betAmount) {
        Random random = new Random();

        // Limit number of goals to a reasonable range (0-5 goals)
        int scoreA = random.nextInt(6); // 0 to 5 goals for team A
        int scoreB = random.nextInt(6); // 0 to 5 goals for team B

        System.out.println("Match result: " + teamA.getName() + " " + scoreA + " - " + scoreB + " " + teamB.getName());

        if (scoreA > scoreB) {
            System.out.println(teamA.getName() + " has won!");
            return selectedTeam == teamA; // Returns true if user won
        } else if (scoreA < scoreB) {
            System.out.println(teamB.getName() + " has won!");
            return selectedTeam == teamB; // Returns true if user won
        } else {
            System.out.println("The match has ended in a draw!");
            System.out.println("Your bet has been refunded.");
            return false; // Draw
        }
    }
}