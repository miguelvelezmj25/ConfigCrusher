package edu.cmu.cs.mvelezce.tool.instrumentation.java.soot.config;

import soot.options.Options;

public class UserSootConfig {

    private UserSootConfig() {}

    public static void setUserSootOptions(Options options) {
        options.set_output_format(Options.output_format_none);

        Options.v().setPhaseOption("jb", "use-original-names:true");
        Options.v().set_keep_line_number(true);
        Options.v().set_keep_offset(true);
        Options.v().set_coffi(true);
        Options.v().set_ignore_classpath_errors(true);
    }

}
