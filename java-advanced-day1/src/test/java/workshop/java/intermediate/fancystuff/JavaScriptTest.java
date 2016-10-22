package workshop.java.intermediate.fancystuff;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 *
 */
@Disabled
public class JavaScriptTest {

    private ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

    private void loadClientLambdas(String js) throws Exception {
        engine.eval(js);
    }

    @Test
    public void configurableJSLambds() throws Exception {
        String js = "" +
                "var filter = function(movie) {\n" +
                "    return movie.imdbRating > 8.0;\n" +
                "};" +
                "";
        loadClientLambdas(js);


    }
}
