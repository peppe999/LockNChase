package application.ai;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("distanzeGenerate")
public class AIDistances {
    @Param(0)
    private Integer sourceNode;

    @Param(1)
    private Integer destinationNode;

    @Param(2)
    private Integer distance;

    public AIDistances() {}

    public Integer getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(Integer sourceNode) {
        this.sourceNode = sourceNode;
    }

    public Integer getDestinationNode() {
        return destinationNode;
    }

    public void setDestinationNode(Integer destinationNode) {
        this.destinationNode = destinationNode;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
}
