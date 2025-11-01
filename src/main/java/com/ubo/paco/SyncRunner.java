package com.ubo.paco;


import com.ubo.paco.model.ElementMobile;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import java.util.concurrent.TimeUnit;

public class SyncRunner {
    private final ExecutorService executor;

    public SyncRunner(int threads) {
        this.executor = Executors.newFixedThreadPool(threads);
    }

    // Retourne true si la sync a été soumise
    public boolean submitSync(ElementMobile em) {
        if (em == null) return false;
        if (!em.tryStartSync()) return false;

        executor.submit(() -> {
            try {
                em.sync();
            } finally {
                // s'assure que le drapeau est libéré même si sync() échoue/interrompt
                em.setInSync(false);
            }
        });
        return true;
    }

    // Soumet deux syncs (utilise submitSync pour éviter les doubles)
    public void runTwoSyncs(ElementMobile a, ElementMobile b) {
        submitSync(a);
        submitSync(b);
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
