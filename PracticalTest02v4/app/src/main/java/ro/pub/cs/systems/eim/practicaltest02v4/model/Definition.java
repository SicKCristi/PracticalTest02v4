/// /////////////////////////////////////////////////////////////////////////////////////

// CERINTA 3.a) Sa se gestiunea obiectului local cu informațiile solicitate anterior

/// ////////////////////////////////////////////////////////////////////////////////////
package ro.pub.cs.systems.eim.practicaltest02v4.model;

import android.support.annotation.NonNull;

// Informații
public class Definition {

    // Informatiile pe care le preluam de pe site
    private final String word; /// Cuvântul utilizatorului
    private final String definition; /// Definiția cuvântului dat de utilizator

    /// Constructor
    public Definition(String word, String definition){
        this.word = word;
        this.definition = definition;
    }

    ///  Getteri
    public String getWord() {
        return word;
    }

    public String getDefinition() {
        return definition;
    }

    /// Metoda toString
    @NonNull
    @Override
    public String toString(){
        return word + " : " + definition;
    }
}
