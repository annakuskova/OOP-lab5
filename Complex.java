public class Complex {
    public double re;
    public double im;

    public Complex(double re, double im){
        this.re = re;
        this.im = im;
    }

    public double getSquaredModule() {
        return (this.re * this.re + this.im * this.im);
    }

    public void makeSquaredInPoint(double x, double y) {
        double real = (re * re) - (im * im) + x;
        double imagine = 2 * re * im + y;

        re = real;
        im = imagine;
    }

    public void makeSquaredWithConjInPoint(double x, double y) {
        double real = (re * re) - (im * im) + x;
        double imagine =  - 2 * re * im + y;

        re = real;
        im = imagine;
    }

    public void makeSquaredWithAbsInPoint(double x, double y) {
        double real = (re * re) - (im * im) + x;
        double imagine = 2 * Math.abs(re) * Math.abs(im) + y;

        re = real;
        im = imagine;
    }
}
