package edu.cmu.cs.mvelezce.tool.execute.java.adapter.email;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class EmailAdapter extends BaseAdapter {

    public EmailAdapter() {
        this(null, null, null);
    }

    public EmailAdapter(String programName, String entryPoint, String dir) {
        super(programName, entryPoint, dir, EmailAdapter.getEmailOptions());
    }

    public static List<String> getEmailOptions() {
        String[] options = {
                "BASE",
                "KEYS",
                "ENCRYPT",
                "AUTORESPONDER",
                "ADDRESSBOOK",
                "SIGN",
                "FORWARD",
                "VERIFY",
                "DECRYPT"
        };

        return Arrays.asList(options);
    }

    @Override
    public void execute(Set<String> configuration, int iteration) throws IOException, InterruptedException {
        String[] args = this.configurationAsMainArguments(configuration);
        String[] newArgs = new String[args.length + 1];

        newArgs[0] = iteration + "";
        System.arraycopy(args, 0, newArgs, 1, args.length);

        this.execute(EmailMain.EMAIL_MAIN, newArgs);
    }

}
