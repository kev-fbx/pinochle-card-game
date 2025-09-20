package utils;

import Game.Pinochle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PropertyParser {

    /** Retrieve the fixed cutthroat moves from the property file */
    public static List<List<String>> parseCutthroatMoves() {
        List<List<String>> playerAutoMovements = new ArrayList<>();
        String player0AutoMovement = Pinochle.getGameRef().getProperties().getProperty("players.0.final_cards");
        String player1AutoMovement = Pinochle.getGameRef().getProperties().getProperty("players.1.final_cards");

        String[] playerMovements = new String[]{"", ""};
        if (player0AutoMovement != null) {
            playerMovements[0] = player0AutoMovement;
        }

        if (player1AutoMovement != null) {
            playerMovements[1] = player1AutoMovement;
        }

        for (String movementString : playerMovements) {
            List<String> movements = Arrays.asList(movementString.split(","));
            playerAutoMovements.add(movements);
        }
        return playerAutoMovements;
    }

    /** Retrieve the fixed cutthroat moves from the property file */
    public static List<List<String>> parseCutthroatDistribution() {
        List<List<String>> playerAutoMovements = new ArrayList<>();
        String player0AutoMovement = Pinochle.getGameRef().getProperties().getProperty("players.0.extra_cards");
        String player1AutoMovement = Pinochle.getGameRef().getProperties().getProperty("players.1.extra_cards");

        String[] playerMovements = new String[]{"", ""};
        if (player0AutoMovement != null) {
            playerMovements[0] = player0AutoMovement;
        }

        if (player1AutoMovement != null) {
            playerMovements[1] = player1AutoMovement;
        }

        for (String movementString : playerMovements) {
            List<String> movements = Arrays.asList(movementString.split(","));
            playerAutoMovements.add(movements);
        }
        return playerAutoMovements;
    }


    /** Retrieve the fixed trick taking moves from the property file */
    public static List<List<String>> setupPlayerAutoMovements() {
        List<List<String>> playerAutoMovements = new ArrayList<>();
        String player0AutoMovement = Pinochle.getGameRef().getProperties().getProperty("players.0.cardsPlayed");
        String player1AutoMovement = Pinochle.getGameRef().getProperties().getProperty("players.1.cardsPlayed");

        String[] playerMovements = new String[]{"", ""};
        if (player0AutoMovement != null) {
            playerMovements[0] = player0AutoMovement;
        }

        if (player1AutoMovement != null) {
            playerMovements[1] = player1AutoMovement;
        }

        for (String movementString : playerMovements) {
            List<String> movements = Arrays.asList(movementString.split(","));
            playerAutoMovements.add(movements);
        }
        return playerAutoMovements;
    }

    /** Retreive the bidding information from the properties file */
    public static List<Integer> parseBids(String bidsString) {
        if (bidsString == null || bidsString.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(bidsString.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
