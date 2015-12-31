package cucumber.runtime.model;

import java.util.List;

import gherkin.formatter.model.Comment;
import gherkin.formatter.model.DataTableRow;
import gherkin.formatter.model.DocString;
import gherkin.formatter.model.Step;

import static cucumber.runtime.model.ParameterizedStepContainer.replacePlaceHolders;

/**
 * @author Jeremy Comte
 */
public class ParameterizedStep extends Step {

    public static enum Type {

        Start, Parameterized, End
    }

    private final Type type;

    private ParameterizedStep(List<Comment> comments, String keyword, String name, Integer line, List<DataTableRow> rows, DocString docString, Type type) {
        super(comments, keyword, name, line, rows, docString);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String getName() {
        switch (type) {
            case Start:
                return super.getName() + " {";
            case End:
                return "}";
            default:
                return super.getName();
        }
    }

    public String getOriginalName() {
        return super.getName();
    }

    @Override
    public String getKeyword() {
        switch (type) {
            case Parameterized:
                return "  " + super.getKeyword();
            case End:
                return "";
            default:
                return super.getKeyword();
        }
    }

    public String getOriginalKeyword() {
        return super.getKeyword();
    }

    public static ParameterizedStep startOf(Step step) {
        return new ParameterizedStep(step.getComments(), step.getKeyword(), step.getName(), step.getLine(), step.getRows(), step.getDocString(), Type.Start);
    }

    public static ParameterizedStep parameterize(Step step, String[] names, Object[] parameters) {
        return new ParameterizedStep(step.getComments(), step.getKeyword(), replacePlaceHolders(step.getName(), names, parameters), step.getLine(),
            step.getRows(), step.getDocString(), Type.Parameterized);
    }

    public static ParameterizedStep endOf(Step step) {
        return new ParameterizedStep(step.getComments(), step.getKeyword(), step.getName(), step.getLine(), step.getRows(), step.getDocString(), Type.End);
    }

}
