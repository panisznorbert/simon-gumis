package panisz.norbert.simongumis;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.templatemodel.ModelEncoder;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

public class LocalTimeConverter implements ModelEncoder<LocalTime, String>, Converter<String, LocalTime> {


    @Override
    public String encode(LocalTime modelValue) {
        return convertIfNotNull(modelValue, DateTimeFormatter
                .ofPattern("h:mm a", Locale.US)::format);
    }

    @Override
    public LocalTime decode(String presentationValue) {
        return convertIfNotNull(presentationValue, p -> LocalTime.parse(p, DateTimeFormatter
                .ofPattern("h:mm a", Locale.US)));
    }

    @Override
    public Result<LocalTime> convertToModel(String value, ValueContext context) {
        try {
            return Result.ok(decode(value));
        } catch (DateTimeParseException e) {
            return Result.error("Invalid time");
        }
    }

    @Override
    public String convertToPresentation(LocalTime value, ValueContext context) {
        return encode(value);
    }

    public static <S, T> T convertIfNotNull(S source, Function<S, T> converter) {
        return convertIfNotNull(source, converter, () -> null);
    }
    public static <S, T> T convertIfNotNull(S source, Function<S, T> converter, Supplier<T> nullValueSupplier) {
        return source != null ? converter.apply(source) : nullValueSupplier.get();
    }

    public static <T> ItemLabelGenerator<T> createItemLabelGenerator(Function<T, String> converter) {
        return item -> convertIfNotNull(item, converter, () -> "");
    }
}
