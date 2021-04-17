package de.primeapi.primeplugins.bungeeapi.sql;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

@Getter @AllArgsConstructor
public class DatabaseTask<T> {

    private CompletableFuture<T> future;

    public void submit(Consumer<T> consumer){
        future.handle((unused, throwable) -> {
            if(throwable != null) {
                throwable.printStackTrace();
                return null;
            }
            return unused;
        }).thenAcceptAsync(consumer);
    }



    public T complete(){
        return future.join();
    }
}
