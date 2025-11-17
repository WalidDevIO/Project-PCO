package com.ubo.paco.events;

/**
 * Spécification pour les classes qui reçoivent événements destinés aux vues
 */
public interface ViewEventReceiver {
    /**
     * Réception d'un MoveEvent (un élément mobile a bougé)
     * @param event l'événement
     */
    void onMove(MoveEvent event);

    /**
     * Réception d'un StartSyncEvent (un élément mobile démarre une synchronisation)
     * @param event l'événement
     */
    void onSyncStart(StartSyncEvent event);

    /**
     * Réception d'un EndSyncEvent (un élement mobile finit une synchronisation)
     * @param event l'événement
     */
    void onSyncEnd(EndSyncEvent event);
}
