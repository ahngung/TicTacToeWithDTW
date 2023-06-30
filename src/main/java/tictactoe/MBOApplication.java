package tictactoe;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MBOApplication extends Application<MBOConfiguration> {

    public static void main(final String[] args) throws Exception {
        new MBOApplication().run(args);
    }

    @Override
    public String getName() {
        return "MBO";
    }

    @Override
    public void initialize(final Bootstrap<MBOConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final MBOConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
