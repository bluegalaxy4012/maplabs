import java.math.BigDecimal;

public class NumarComplex {

    public float real;
    public float imaginar;

    @Override
    public String toString() {
        if (imaginar == 0)
            return String.valueOf(real);

        if (real == 0)
            return String.valueOf(imaginar) + "i";

        if (imaginar >= 0)
            return real + "+" + imaginar + "i";

        //new BigDecimal().setScale()

        return String.valueOf(real) + String.valueOf(imaginar) + "i";
    }

    public NumarComplex(float _real, float _imaginar) {
        real = _real;
        imaginar = _imaginar;
    }

}
