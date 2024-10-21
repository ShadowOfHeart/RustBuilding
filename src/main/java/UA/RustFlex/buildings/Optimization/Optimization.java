package UA.RustFlex.buildings.Optimization;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Optimization extends JavaPlugin {

    private ExecutorService executorService;
    private static final int THREAD_POOL_SIZE = 4; // Ограничиваем пул потоков до 4 для баланса производительности

    @Override
    public void onEnable() {
        executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE); // Используем фиксированный пул потоков
        getLogger().info("Optimization plugin enabled with " + THREAD_POOL_SIZE + " threads.");
    }

    @Override
    public void onDisable() {
        shutdownExecutorService();
        getLogger().info("Optimization plugin disabled and threads shut down.");
    }

    private void shutdownExecutorService() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                getLogger().warning("ExecutorService did not terminate in time, forcing shutdown.");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            getLogger().severe("Thread interrupted during shutdown, forcing shutdown.");
            executorService.shutdownNow();
            Thread.currentThread().interrupt(); // Сброс флага прерывания после завершения
        }
    }

    public void runAsyncTask(Runnable task) {
        executorService.submit(() -> {
            try {
                task.run();
            } catch (Exception e) {
                getLogger().severe("Error during async task execution: " + e.getMessage());
            }
        });
    }

    public void performHeavyOperation() {
        runAsyncTask(() -> {
            simulateHeavyTask(); // Тяжёлая задача
            returnToMainThread();
        });
    }

    private void simulateHeavyTask() {
        try {
            getLogger().info("Starting heavy task...");
            Thread.sleep(2000); // Симуляция длительной операции
        } catch (InterruptedException e) {
            getLogger().warning("Heavy task interrupted.");
            Thread.currentThread().interrupt(); // Сброс флага прерывания
        }
    }

    private void returnToMainThread() {
        // Возвращаемся к основному потоку для последующих действий
        getServer().getScheduler().runTask(this, () -> {
            getLogger().info("Heavy task completed on main thread.");
        });
    }
}
