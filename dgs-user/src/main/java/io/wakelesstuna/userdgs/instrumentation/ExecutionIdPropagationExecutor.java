package io.wakelesstuna.userdgs.instrumentation;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;

import java.util.concurrent.Executor;

import static io.wakelesstuna.userdgs.instrumentation.RequestLoggingInstrumentation.EXECUTION_ID;


/**
 * This class is used to wrap the thread pools of the data loaders.
 * It wraps all the threads in the execution pool and puts the execution id
 * into the {@link MDC} map.
 *
 * @author oscar.steen.forss
 */
@RequiredArgsConstructor(staticName = "wrap")
public class ExecutionIdPropagationExecutor implements Executor {

    private final Executor delegate;

    @Override
    public void execute(@NotNull Runnable command) {
        var correlationId = MDC.get(EXECUTION_ID);
        delegate.execute(() -> {
            try {
                MDC.put(EXECUTION_ID, correlationId);
                command.run();
            } finally {
                MDC.remove(EXECUTION_ID);
            }
        });
    }

}
