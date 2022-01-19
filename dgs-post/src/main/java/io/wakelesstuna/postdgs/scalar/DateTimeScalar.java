package io.wakelesstuna.postdgs.scalar;

import com.netflix.graphql.dgs.DgsScalar;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * graphql-java provides optional scalars in the graphql-java-extended-scalars library.
 * We can wire a scalar from this library by adding the scalar to the RuntimeWiring.
 *
 * @author oscar.steen.forss
 */
@DgsScalar(name="DateTime")
public class DateTimeScalar implements Coercing<LocalDateTime, String> {
    @Override
    public String serialize(@NotNull Object dataFetcherResult) throws CoercingSerializeException {
        if (dataFetcherResult instanceof LocalDateTime) {
            return ((LocalDateTime) dataFetcherResult).format(DateTimeFormatter.ISO_DATE_TIME);
        } else {
            throw new CoercingSerializeException("Not a valid DateTime");
        }
    }

    @NotNull
    @Override
    public LocalDateTime parseValue(Object input) throws CoercingParseValueException {
        return LocalDateTime.parse(input.toString(), DateTimeFormatter.ISO_DATE_TIME);
    }

    @NotNull
    @Override
    public LocalDateTime parseLiteral(Object input) throws CoercingParseLiteralException {
        if (input instanceof StringValue) {
            return LocalDateTime.parse(((StringValue) input).getValue(), DateTimeFormatter.ISO_DATE_TIME);
        }

        throw new CoercingParseLiteralException("Value is not a valid ISO date time");
    }
}
