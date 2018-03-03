package edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.email;

import edu.cmu.cs.mvelezce.Feature;
import edu.cmu.cs.mvelezce.evaluation.approaches.family.featuremodel.FeatureModel;
import edu.cmu.cs.mvelezce.evaluation.approaches.family.featuremodel.email.EmailFM;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.email.EmailAdapter;

import java.io.IOException;
import java.util.Set;

public class BFEmailAdapter extends EmailAdapter {

    private FeatureModel featureModel = new EmailFM();

    public BFEmailAdapter(String programName, String entryPoint, String dir) {
        super(programName, entryPoint, dir);
    }

    @Override
    public void execute(Set<String> configuration, int iteration) throws IOException, InterruptedException {
        if(!this.featureModel.isValidProduct(configuration)) {
            return ;
        }

        String[] args = this.configurationAsMainArguments(configuration);
        String[] newArgs = new String[args.length + 1];

        newArgs[0] = iteration + "";
        System.arraycopy(args, 0, newArgs, 1, args.length);

        this.execute(BFEmailMain.BF_EMAIL_MAIN, newArgs);
    }

}
