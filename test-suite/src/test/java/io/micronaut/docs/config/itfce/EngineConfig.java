package io.micronaut.docs.config.itfce;

// tag::imports[]
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.bind.annotation.Bindable;
import javax.validation.constraints.*;
import java.util.Optional;
// end::imports[]

// tag::class[]
@ConfigurationProperties("my.engine") // <1>
public interface EngineConfig {

    @Bindable(defaultValue = "Ford") // <2>
    @NotBlank // <3>
    String getManufacturer();

    @Min(1L)
    int getCylinders();

    @NotNull
    CrankShaft getCrankShaft(); // <4>

    @ConfigurationProperties("crank-shaft")
    interface CrankShaft { // <5>
        Optional<Double> getRodLength(); // <6>
    }
}
// end::class[]

