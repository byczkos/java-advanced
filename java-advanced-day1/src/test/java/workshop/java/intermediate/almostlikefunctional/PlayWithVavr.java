package workshop.java.intermediate.almostlikefunctional;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.match.annotation.Patterns;
import io.vavr.match.annotation.Unapply;

@Patterns
public class PlayWithVavr {

    @Unapply
    public static Tuple2<Double, String> Product($EntryTest.Product product) {
        return Tuple.of(product.getPrice(), product.getVatPL());
    }
}
