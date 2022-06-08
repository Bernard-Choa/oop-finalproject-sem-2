package externals;

public class RootsOfUnity extends ComplexNum {
    private double k;
    private double n;
    private ComplexNum z;

    public RootsOfUnity(int k,int n) {
        this.k = k;
        this.n = n;
        ComplexNum z;
        double x = Math.cos(2.0 * Math.PI / n);
        double y = Math.sin(2.0 * Math.PI / n);
        // e^(pi*i) = cos(pi) + i sin(pi)
        ComplexNum t = new ComplexNum(x, y);

        z = t.raised(k);
        // kth point of the nth root of unity
        this.z = z;
    }

    // Returns the kth point
    public double k() { return this.k;}

    // Returns the nth root
    public double n() { return this.n;}

    // Returns the ComplexNum z at the kth point of the nth root
    public ComplexNum z() { return this.z;}
}

