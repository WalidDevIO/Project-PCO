package com.ubo.paco.graphicsElement.PositionStrategy;

import com.ubo.paco.model.ElementMobile;
import nicellipse.component.NiRectangle;

import java.awt.*;

/**
 * Stratégie de positionnement des composants d'une vue par rapport à son élément du modèle.
 */
public interface PositionStrategy {
    /**
     * Positionne les éléments
     * @param child le composant graphique d'un satellite ou d'une balise
     * @param sync le composant graphique des ondes de synchronisation
     * @param parent la vue qui représente l'objet et ses ondes éventuelles
     * @param model l'objet du modèle
     */
    void position(Component child, Component sync, NiRectangle parent, ElementMobile model);
}