package cd.go.plugin.base.test_helper.annotations;

import cd.go.plugin.base.ResourceReader;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.util.Arrays;
import java.util.stream.Stream;

public class JsonSourceProvider implements ArgumentsProvider, AnnotationConsumer<JsonSource> {
    private String[] jsonFiles;

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(Arguments.of(Arrays.stream(jsonFiles)
                .map(ResourceReader::readResource).toArray()));
    }

    @Override
    public void accept(JsonSource jsonSource) {
        jsonFiles = jsonSource.jsonFiles();
    }
}