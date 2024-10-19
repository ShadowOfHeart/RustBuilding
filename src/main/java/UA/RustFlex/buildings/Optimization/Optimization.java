package UA.RustFlex.buildings.Optimization;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Optimization extends JavaPlugin {

    private ExecutorService executorService;

    @Override
    public void onEnable() {
        executorService = Executors.newCachedThreadPool(); // Используйте кешируемый пул потоков
    }

    @Override
    public void onDisable() {
        shutdownExecutorService();
    }

    private void shutdownExecutorService() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    public void runAsyncTask(Runnable task) {
        executorService.submit(task);
    }

    public void performHeavyOperation() {
        runAsyncTask(() -> {
            simulateHeavyTask();
            // Возврат к основному потоку для выполнения последующих действий
            getServer().getScheduler().runTask(this, () -> {
                getLogger().info("Тяжёлая задача завершена!");
            });
        });
    }

    private void simulateHeavyTask() {
        try {
            Thread.sleep(2000); // Симуляция длительной операции
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Сброс флага прерывания
        }
    }
}
