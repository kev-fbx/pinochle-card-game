package meld;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import meld.combinations.*;

/**
 * Factory class for instantiating meld strategies.
 *
 * @author Kevin Tran
 */
class MeldFactory {
    private static MeldFactory instance = null;

    private MeldFactory() {
    }

    static MeldFactory getInstance() {
        if (instance == null) {
            instance = new MeldFactory();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

     List<IMeld> createAllMelds() {
        List<IMeld> melds = new ArrayList<>();

        melds.add(new AceRun());
        melds.add(new AceRunExtraKing());
        melds.add(new AceRunExtraQueen());
        melds.add(new RoyalMarriage());
        melds.add(new Dix());
        melds.add(new AceRunRoyalMarriage());
        melds.add(new DoubleRun());
        melds.add(new CommonMarriage());
        melds.add(new PinochleMeld());
        melds.add(new DoublePinochle());
        melds.add(new AcesAround());
        melds.add(new JacksAbound());

        // Puts highest scoring melds first, ensuring best melds are given priority.
        melds.sort(Comparator.comparingInt(IMeld::getScore).reversed());
        return melds;
    }
}