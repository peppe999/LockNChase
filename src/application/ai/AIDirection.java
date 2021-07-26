package application.ai;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;
import it.unical.mat.embasp.languages.asp.SymbolicConstant;

import java.util.List;

@Id("direction")
public class AIDirection {
    @Param(0)
    private SymbolicConstant value;

    public AIDirection() {}

    public SymbolicConstant getValue() {
        return value;
    }

    public void setValue(SymbolicConstant value) {
        this.value = value;
    }
}
