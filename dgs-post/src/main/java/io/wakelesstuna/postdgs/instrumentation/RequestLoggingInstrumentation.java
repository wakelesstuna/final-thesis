package io.wakelesstuna.postdgs.instrumentation;

import com.netflix.graphql.dgs.DgsComponent;
import graphql.ExecutionResult;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.SimpleInstrumentationContext;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLNonNull;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLOutputType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

/**
 * This class handle the pre logging for each request that comes in to the server.
 * It takes the execution id that the graphql server provides to every request and
 * puts it into the {@link MDC} map for the {@link Slf4j} logger.
 * In the application.properties file we overrides the normal behavior of the {@link Slf4j}
 * logging pattern and puts the execution id first in every logging massage.
 *
 * @author oscar.steen.forss
 */
@Slf4j
@DgsComponent
@RequiredArgsConstructor
public class RequestLoggingInstrumentation extends SimpleInstrumentation {

    public static final String EXECUTION_ID = "execution_id";

    private final Clock clock;

    @Override
    public InstrumentationContext<ExecutionResult> beginExecution(
            InstrumentationExecutionParameters parameters) {
        var start = Instant.now(clock);
        // Add the execution ID to the NIO thread
        MDC.put(EXECUTION_ID, parameters.getExecutionInput().getExecutionId().toString());

        log.info("Query: {} with variables: {}", parameters.getQuery(), parameters.getVariables());
        return SimpleInstrumentationContext.whenCompleted((executionResult, throwable) -> {
            var duration = Duration.between(start, Instant.now(clock));
            if (throwable == null) {
                log.info("Completed successfully in: {}", duration);
            } else {
                log.warn("Failed in: {}", duration, throwable);
            }
            // If we have async resolvers, this callback can occur in the thread-pool and not the NIO thread.
            // In that case, the LoggingListener will be used as a fallback to clear the NIO thread.
            //MDC.clear();
        });
    }


    @Override
    public DataFetcher<?> instrumentDataFetcher(DataFetcher<?> dataFetcher, InstrumentationFieldFetchParameters parameters) {
        // We only care about user code
        if (parameters.isTrivialDataFetcher()) {
            return dataFetcher;
        }

        return environment -> {
            long startTime = System.currentTimeMillis();
            Object result = dataFetcher.get(environment);
            if (result instanceof CompletableFuture) {
                ((CompletableFuture<?>) result).whenComplete((r, ex) -> {
                    long totalTime = System.currentTimeMillis() - startTime;
                    log.info("Async datafetcher {} took {}ms", findDataFetcherTag(parameters), totalTime);
                });
            } else {
                long totalTime = System.currentTimeMillis() - startTime;
                log.info("Datafetcher {} took {}ms", findDataFetcherTag(parameters), totalTime);
            }
            return result;
        };
    }

    private String findDataFetcherTag(InstrumentationFieldFetchParameters parameters) {
        GraphQLOutputType type = parameters.getExecutionStepInfo().getParent().getType();
        GraphQLObjectType parent;
        if (type instanceof GraphQLNonNull) {
            parent = (GraphQLObjectType) ((GraphQLNonNull) type).getWrappedType();
        } else {
            parent = (GraphQLObjectType) type;
        }

        return parent.getName() + "." + parameters.getExecutionStepInfo().getPath().getSegmentName();
    }
}
