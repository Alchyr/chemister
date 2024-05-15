package chemister.cards;

import chemister.character.Chemister;

public interface InfuseCard {
    Chemister.Flasks[] getFlasks();
    //Flasks flash when hovering
    //When dragging a flask card, preview main effects given by the infusions on the card
    //Show horizontally in rows above card, flask + flask effects
}
