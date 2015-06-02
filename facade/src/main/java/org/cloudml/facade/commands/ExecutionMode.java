package org.cloudml.facade.commands;

/**
 * Created by Maksym on 02.06.2015.
 */
public class ExecutionMode extends CloudMlCommand {

    private boolean parallel;

    public ExecutionMode(Boolean parallel){
        this.parallel = parallel;
    }

    public Boolean isParallel(){
        return this.parallel;
    }

    @Override
    public void execute(CommandHandler target) {
        target.handle(this);
    }

    @Override
    public String toString() {
        return String.format("ExecutionMode");
    }
}
