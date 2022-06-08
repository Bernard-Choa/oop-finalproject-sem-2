package externals;

public class ComplexNum {
    private double re;   // Real component
    private double im;   // Imaginary component

    // Create new ComplexNum object with default parameters
    public ComplexNum() {
        this.re = 0;
        this.im = 0;
    }

    // Create new ComplexNumNum object with the provided real and imaginary components
    public ComplexNum(double real, double imag) {
        this.re = real;
        this.im = imag;
    }

    // Return string representation of the invoked ComplexNum object
    public String toString() {
        if (im == 0) return re + "";
        if (re == 0) return im + "i";
        if (im <  0) return re + " - " + (-im) + "i";
        return re + " + " + im + "i";
    }

    // Returns a new ComplexNumNum object whose value is (this + that)
    public ComplexNum plus(ComplexNum that) {
        double real = this.re + that.re;
        double imag = this.im + that.im;
        return new ComplexNum(real, imag);
    }

    // Returns a new ComplexNum object whose value is (this - that)
    public ComplexNum minus(ComplexNum that) {
        double real = this.re - that.re;
        double imag = this.im - that.im;
        return new ComplexNum(real, imag);
    }

    // Returns a new ComplexNum object whose value is (this * that)
    public ComplexNum times(ComplexNum that) {
        double real = this.re * that.re - this.im * that.im;
        double imag = this.re * that.im + this.im * that.re;
        return new ComplexNum(real, imag);
    }

    // Returns a new ComplexNum object whose value is the reciprocal of this
    public ComplexNum reciprocal() {
        double scale = re*re + im*im;
        return new ComplexNum(re / scale, -im / scale);
    }

    // Returns a new ComplexNum object whose value is (this / that)
    public ComplexNum divides(ComplexNum that) {
        return this.times(that.reciprocal());
    }

    // Returns the real component
    public double re() { return this.re; }

    // Returns the imaginary component
    public double im() { return this.im; }

    // Returns a new ComplexNum object whose value is (this ^ that)
    public ComplexNum raised(int that) {
        ComplexNum pow = new ComplexNum(1,0);
        for (int i = 1; i <= that; i++) {
            pow = pow.times(this);
        }
        return pow;
    }
}
