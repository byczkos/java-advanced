package workshop.java.intermediate.almostlikefunctional;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class $EntryTest {

    // Task 1. make PriceService more Object Oriented
    // rewrite PriceService handling VAT (Value Added Tax) calculation,
    // try to use Object Oriented style of programming while keeping service interface unchanged

    // Task 2. make PriceService more Functional (your imagination of functional)
    // rewrite PriceService handling VAT (Value Added Tax) calculation,
    // try to use Functional style of programming while keeping service interface unchanged

    // REMEMBER: never calculate money related stuff using double or float... :)

    @AllArgsConstructor
    public static class PriceService {

        private final ProductDao dao;

        public double calculatePrice(long productId) {
            Product product = dao.getById(productId);
            if ("A".equals(product.getVatPL())) {
                return product.getPrice() * 1.05;
            } else if ("B".equals(product.getVatPL())) {
                return product.getPrice() * 1.08;
            } else if ("C".equals(product.getVatPL())) {
                return product.getPrice() * 1.15;
            } else {
                return product.getPrice() * 1.23;
            }
        }
    }

    public interface ProductDao {
        Product getById(long productId);
    }

    @Value
    public static class Product {
        long id;
        double price;
        String vatPL;
    }

    @Test
    void check5PercentVAT() {
        PriceService service = serviceWithProduct(new Product(13, 10.0, "A"));

        double taxed = service.calculatePrice(13);

        Assertions.assertThat(taxed).isCloseTo(10.5, Assertions.offset(0.009));
    }

    @Test
    void check8PercentVAT() {
        PriceService service = serviceWithProduct(new Product(13, 10.0, "B"));

        double taxed = service.calculatePrice(13);

        Assertions.assertThat(taxed).isCloseTo(10.8, Assertions.offset(0.009));
    }

    @Test
    void check15PercentVAT() {
        PriceService service = serviceWithProduct(new Product(13, 10.0, "C"));

        double taxed = service.calculatePrice(13);

        Assertions.assertThat(taxed).isCloseTo(11.5, Assertions.offset(0.009));
    }

    @Test
    void checkDefaultPercentVAT() {
        PriceService service = serviceWithProduct(new Product(13, 10.0, "X"));

        double taxed = service.calculatePrice(13);

        Assertions.assertThat(taxed).isCloseTo(12.3, Assertions.offset(0.009));
    }

    private PriceService serviceWithProduct(Product product) {
        ProductDao dao = Mockito.mock(ProductDao.class);
        Mockito.when(dao.getById(product.getId())).thenReturn(product);
        return new PriceService(dao);
    }
}
