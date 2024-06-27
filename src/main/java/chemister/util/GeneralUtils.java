package chemister.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class GeneralUtils {
    public static String arrToString(Object[] arr) {
        if (arr == null)
            return null;
        if (arr.length == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length - 1; ++i) {
            sb.append(arr[i]).append(", ");
        }
        sb.append(arr[arr.length - 1]);
        return sb.toString();
    }

    public static <T> boolean arrContains(T[] arr, T val) {
        for (T arrVal : arr)
            if (Objects.equals(val, arrVal)) return true;
        return false;
    }

    public static String removePrefix(String ID) {
        return ID.substring(ID.indexOf(":") + 1);
    }

    public static CardGroup filterCardsForDiscovery(Predicate<AbstractCard> filter) {
        CardGroup retVal = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        CardLibrary.getAllCards().stream().filter(filter).forEach(c -> retVal.addToTop(c.makeCopy()));
        return retVal;
    }

    public static CardGroup filterCardsForDiscovery(ArrayList<AbstractCard> list, Predicate<AbstractCard> filter) {
        CardGroup retVal = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        list.stream().filter(filter).forEach(c -> retVal.addToTop(c.makeCopy()));
        return retVal;
    }

    public static ArrayList<AbstractCard> createCardsForDiscovery(CardGroup list, int amt) {
        ArrayList<AbstractCard> retVal = new ArrayList<>(list.group);

        while (retVal.size() > amt) {
            retVal.remove(AbstractDungeon.cardRng.random(retVal.size() - 1));
        }
        for (int i = 0; i < retVal.size(); ++i) {
            retVal.set(i, retVal.get(i).makeCopy());
        }
        return retVal;
    }

    public static void forAllInCombatCards(Consumer<AbstractCard> action, boolean includeLimbo) {
        Set<AbstractCard> acted = new HashSet<>();
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (acted.add(c)) {
                action.accept(c);
            }
        }
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (acted.add(c)) {
                action.accept(c);
            }
        }
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (acted.add(c)) {
                action.accept(c);
            }
        }
        for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
            if (acted.add(c)) {
                action.accept(c);
            }
        }
        if (includeLimbo) {
            for (AbstractCard c : AbstractDungeon.player.limbo.group) {
                if (acted.add(c)) {
                    action.accept(c);
                }
            }
        }
    }

    public static int getLogicalCardCost(AbstractCard c) {
        if (!c.freeToPlay()) {
            if (c.cost <= -2) {
                return 0;
            } else if (c.cost == -1) {
                return EnergyPanel.totalCount;
            }
            return c.costForTurn;
        }
        return 0;
    }

    public static <T> int count(Collection<? extends T> stuff, T check) {
        int amt = 0;
        for (T obj : stuff) {
            if (Objects.equals(obj, check))
                ++amt;
        }
        return amt;
    }
    public static <T> int countLayered(Collection<? extends Collection<? extends T>> stuff, T check) {
        int amt = 0;
        for (Collection<? extends T> inner : stuff) {
            for (T obj : inner) {
                if (Objects.equals(obj, check))
                    ++amt;
            }
        }
        return amt;
    }
}
