import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    @Null(message = "{}")
    private Long id;

    @NotBlank(message = "{}")
    @Size(min = 3, max = 100, message = "{}")
    private String name;

    @NotBlank(message = "")
    @Pattern(regexp = "^PROD-\\d{4}$", message = "{}")
    private String code;

    @NotNull(message = "{}")
    @DecimalMin(value = "0.01", message = "{}")
    private BigDecimal price;

    @Min(value = 0, message = "{}")
    private Integer stock;
}