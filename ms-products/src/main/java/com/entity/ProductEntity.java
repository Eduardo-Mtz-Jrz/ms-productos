import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Null(message = "{product.id.null}")
    private Long id;

    @NotBlank(message = "{product.name.notblank}")
    @Size(min = 3, max = 100, message = "{product.name.size}")
    private String name;

    @NotBlank(message = "{product.code.notblank}")
    @Pattern(regexp = "^PROD-\\d{4}$", message = "{product.code.pattern}")
    private String code;

    @NotNull(message = "{product.price.notnull}")
    @DecimalMin(value = "0.01", message = "{product.price.min}")
    private Float price;

    @Min(value = 0, message = "{product.stock.min}")
    private Integer stock;

    @NotBlank(message = "{product.category.notblank}")
    @Size(min = 3, max = 50, message = "{product.category.size}")
    private String category;
}
}
