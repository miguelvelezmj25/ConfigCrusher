package edu.cmu.cs.mvelezce.tool.execute.java.adapter;

import java.util.Set;

public class DefaultAdapter extends BaseAdapter {

    public DefaultAdapter() {
        super(null, null, null, null);
    }

    @Override
    public void execute(Set<String> configuration) {
        throw new UnsupportedOperationException();
    }
}
