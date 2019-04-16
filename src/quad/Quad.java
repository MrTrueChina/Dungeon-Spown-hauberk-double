package quad;

public class Quad {
    private QuadType _type;

    public Quad(QuadType type) {
        _type = type;
    }
    
    public QuadType getType() {
        return _type;
    }

    public void setType(QuadType type) {
        _type = type;
    }
}
